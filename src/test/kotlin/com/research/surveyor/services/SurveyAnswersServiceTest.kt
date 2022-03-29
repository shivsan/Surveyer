package com.research.surveyor.services

import com.research.surveyor.controllers.request.Answer
import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.models.SurveyAnswer
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import com.research.surveyor.repositories.SurveyAnswersRepository
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class SurveyAnswersServiceTest {
    private val questionRepository = mockk<QuestionRepository>()
    private val questionnaireRepository = mockk<QuestionnaireRepository>()
    private val answerOptionRepository = mockk<AnswerOptionRepository>()
    private val surveyAnswersRepository = mockk<SurveyAnswersRepository>()
    private val surveyAnswersService = SurveyAnswersService(
        questionRepository,
        questionnaireRepository,
        answerOptionRepository,
        surveyAnswersRepository
    )

    @Test
    internal fun `Should create survey answers`() {
        every { questionRepository.existsById(any()) } returns true
        every { questionnaireRepository.existsById(any()) } returns true
        every { answerOptionRepository.existsById(any()) } returns true
        every { questionRepository.findByQuestionnaireId(fakeQuestionnaire.id) } returns listOf(fakeQuestion1, fakeQuestion2)
        every { surveyAnswersRepository.saveAll<SurveyAnswer>(any()) } returns listOf(fakeSurveyAnswer.copy(id = 1))

        val createdSurveyAnswers = surveyAnswersService.create(fakeSurveyAnswersRequest)

        createdSurveyAnswers `should be equal to` listOf(fakeSurveyAnswer.copy(id = 1))
    }

    @Test
    internal fun `Should throw exception for bad ids`() {
        every { questionRepository.existsById(any()) } returns false
        every { questionnaireRepository.existsById(any()) } returns true
        every { answerOptionRepository.existsById(any()) } returns true

        invoking { surveyAnswersService.create(fakeSurveyAnswersRequest) } shouldThrow EntityNotFoundException::class

        every { questionRepository.existsById(any()) } returns true
        every { questionnaireRepository.existsById(any()) } returns false
        every { answerOptionRepository.existsById(any()) } returns true

        invoking { surveyAnswersService.create(fakeSurveyAnswersRequest) } shouldThrow EntityNotFoundException::class

        every { questionRepository.existsById(any()) } returns true
        every { questionnaireRepository.existsById(any()) } returns true
        every { answerOptionRepository.existsById(any()) } returns false

        invoking { surveyAnswersService.create(fakeSurveyAnswersRequest) } shouldThrow EntityNotFoundException::class
    }

    @Test
    internal fun `Should not allow duplicate answers`() {
        every { questionRepository.existsById(any()) } returns true
        every { questionnaireRepository.existsById(any()) } returns true
        every { answerOptionRepository.existsById(any()) } returns true

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1, fakeAnswer1)))
        } shouldThrow InvalidRequestException::class
    }

    @Test
    internal fun `Should not allow incomplete surveys`() {
        every { questionRepository.findByQuestionnaireId(fakeQuestionnaire.id) } returns listOf(
            fakeQuestion1,
            fakeQuestion2
        )
        every { questionnaireRepository.existsById(fakeQuestionnaire.id) } returns true
        every { answerOptionRepository.existsById(any()) } returns true

        invoking {
            surveyAnswersService.create(fakeSurveyAnswersRequest.copy(answers = listOf(fakeAnswer1, fakeAnswer1)))
        } shouldThrow InvalidRequestException::class
    }
}

private val fakeQuestionnaire = Questionnaire(id = 1, title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion1 =
    Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeQuestion2 =
    Question(id = 2, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeAnswer1 = Answer(questionId = fakeQuestion1.id, 1)
private val fakeAnswer2 = Answer(questionId = fakeQuestion2.id, 1)
private val fakeSurveyAnswer = SurveyAnswer(
    questionnaireId = fakeQuestionnaire.id,
    questionId = 1,
    answerOptionId = 1
)
private val fakeSurveyAnswersRequest = SurveyAnswersDto(
    questionnaireId = 1,
    answers = listOf(Answer(questionId = 1, answerOptionId = 1), Answer(questionId = 2, answerOptionId = 1))
)
