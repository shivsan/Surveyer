package com.research.surveyor.services

import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.exceptions.ConflictException
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.AnswerOptionPercentile
import com.research.surveyor.models.Question
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.models.SurveyAnswer
import com.research.surveyor.models.SurveyAnswers
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import com.research.surveyor.repositories.SurveyAnswerRepository
import com.research.surveyor.repositories.SurveyAnswersRepository
import io.mockk.every
import io.mockk.mockk
import java.util.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class SurveyAnswersServiceTest {
    private val questionRepository = mockk<QuestionRepository>()
    private val questionnaireRepository = mockk<QuestionnaireRepository>()
    private val answerOptionRepository = mockk<AnswerOptionRepository>()
    private val surveyAnswersRepository = mockk<SurveyAnswersRepository>()
    private val surveyAnswerRepository = mockk<SurveyAnswerRepository>()
    private val surveyAnswersService = SurveyAnswersService(
        questionRepository,
        questionnaireRepository,
        answerOptionRepository,
        surveyAnswersRepository,
        surveyAnswerRepository
    )

    @Test
    fun `Should create survey answers`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { answerOptionRepository.existsByQuestionIdAndId(any(), any()) } returns true
        every { questionRepository.findByQuestionnaireId(fakeQuestionnaire.id) } returns listOf(
            fakeQuestion1,
            fakeQuestion2
        )
        every { surveyAnswerRepository.saveAll<SurveyAnswer>(any()) } returns listOf(
            fakeAnswer1.copy(id = 1),
            fakeAnswer2.copy(id = 2)
        )
        every { surveyAnswersRepository.save(SurveyAnswers(questionnaireId = fakeQuestionnaire.id)) } returns fakeSurveyAnswers1.copy(
            id = 1
        )

        val createdSurveyAnswers = surveyAnswersService.create(fakeSurveyAnswersRequest)

        createdSurveyAnswers `should be equal to` fakeSurveyAnswersRequest.copy(
            id = 1,
            answers = listOf(fakeAnswer1.copy(id = 1), fakeAnswer2.copy(id = 2))
        )
    }

    @Test
    fun `Should throw exception for bad ids - questionnaire, question, answerOptions`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { answerOptionRepository.existsByQuestionIdAndId(any(), any()) } returns false
        invoking { surveyAnswersService.create(fakeSurveyAnswersRequest) } shouldThrow InvalidRequestException::class

        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.empty()
        every { answerOptionRepository.existsByQuestionIdAndId(any(), any()) } returns true
        invoking { surveyAnswersService.create(fakeSurveyAnswersRequest) } shouldThrow EntityNotFoundException::class
    }

    @Test
    fun `Should not allow duplicate answers`() {
        every { questionRepository.existsById(any()) } returns true
        every { questionnaireRepository.existsById(any()) } returns true
        every { answerOptionRepository.existsById(any()) } returns true

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1, fakeAnswer1)))
        } shouldThrow InvalidRequestException::class
    }

    @Test
    fun `Should not allow incomplete surveys`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { answerOptionRepository.existsByQuestionIdAndId(any(), any()) } returns false
        every { questionnaireRepository.existsById(fakeQuestionnaire.id) } returns true
        every { questionRepository.findByQuestionnaireId(fakeQuestionnaire.id) } returns listOf(
            fakeQuestion1,
            fakeQuestion2
        )

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1)))
        } shouldThrow InvalidRequestException::class
    }

    @Test
    fun `Should not mismatch of questions and answer options`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { answerOptionRepository.existsByQuestionIdAndId(any(), any()) } returns false
        every { questionRepository.findByQuestionnaireId(fakeQuestionnaire.id) } returns listOf(
            fakeQuestion1,
            fakeQuestion2
        )

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1)))
        } shouldThrow InvalidRequestException::class
    }

    @Test
    fun `Should submit survey for questionnaire that is not published`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire.copy(status = QuestionnaireStatus.DRAFT))

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1)))
        } shouldThrow ConflictException::class
    }

    @Test
    fun `Should get stats for all answers of a question`() {
        every { questionnaireRepository.existsById(fakeQuestionnaire.id) } returns true
        every { questionRepository.existsById(fakeQuestion1.id) } returns true
        every { answerOptionRepository.findByQuestionId(fakeQuestion1.id) } returns listOf(
            fakeAnswerOption1.copy(id = 1),
            fakeAnswerOption2.copy(id = 2)
        )
        every { surveyAnswerRepository.findAllByQuestionId(fakeQuestion1.id) } returns listOf(
            fakeAnswer1.copy(answerOptionId = 1)
        )

        val statsForAnswerOptions =
            surveyAnswersService.getStatsForAnswerOptions(fakeQuestionnaire.id, fakeQuestion1.id)

        statsForAnswerOptions `should be equal to` fakeSurveyAnswerStats1
    }

    @Test
    fun `Should throw 404 for non existent questionnaire`() {
        every { questionnaireRepository.existsById(fakeQuestionnaire.id) } returns false

        invoking {
            surveyAnswersService.getStatsForAnswerOptions(
                fakeQuestionnaire.id,
                fakeQuestion1.id
            )
        } shouldThrow EntityNotFoundException::class
    }

    @Test
    fun `Should throw 404 for non existent question`() {
        every { questionnaireRepository.existsById(fakeQuestionnaire.id) } returns true
        every { questionRepository.existsById(fakeQuestion1.id) } returns false

        invoking {
            surveyAnswersService.getStatsForAnswerOptions(
                fakeQuestionnaire.id,
                fakeQuestion1.id
            )
        } shouldThrow EntityNotFoundException::class
    }
}

private val fakeQuestionnaire = Questionnaire(id = 1, title = "New questionnaire", status = QuestionnaireStatus.PUBLISHED)
private val fakeQuestion1 =
    Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeQuestion2 =
    Question(id = 2, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeAnswerOption1 =
    AnswerOption("a", "Monday", fakeQuestion1)
private val fakeAnswerOption2 =
    AnswerOption("a", "Monday", fakeQuestion2)
private val fakeSurveyAnswers1 = SurveyAnswers(questionnaireId = fakeQuestionnaire.id)
private val fakeAnswer1 = SurveyAnswer(
    questionId = fakeQuestion1.id,
    surveyAnswersId = fakeSurveyAnswers1.id,
    answerOptionId = fakeAnswerOption1.id
)
private val fakeAnswer2 = SurveyAnswer(
    questionId = fakeQuestion2.id,
    surveyAnswersId = fakeSurveyAnswers1.id,
    answerOptionId = fakeAnswerOption2.id
)
private val fakeSurveyAnswersRequest = SurveyAnswersDto(
    questionnaireId = 1,
    answers = listOf(SurveyAnswer(questionId = 1, answerOptionId = 1), SurveyAnswer(questionId = 2, answerOptionId = 1))
)
private val fakeSurveyAnswerStats1 =
    QuestionAnswerStats(
        fakeQuestion1.id,
        listOf(AnswerOptionPercentile(1, 1), AnswerOptionPercentile(2, 0)),
        totalVotes = 1
    )
