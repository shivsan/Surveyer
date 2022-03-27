package com.research.surveyor.services

import com.research.surveyor.controllers.request.AnswerOptionRequest
import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import io.mockk.every
import io.mockk.mockk
import java.util.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class QuestionServiceTest {
    private val questionRepository = mockk<QuestionRepository>()
    private val questionnaireRepository = mockk<QuestionnaireRepository>()
    private val answerOptionRepository = mockk<AnswerOptionRepository>()
    private val questionService = QuestionService(questionRepository, questionnaireRepository, answerOptionRepository)

    @Test
    fun `should create question`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { questionRepository.save(fakeQuestion) } returns fakeQuestion
        every { answerOptionRepository.saveAll<AnswerOption>(any()) } returns fakeOptions
        every { questionRepository.findById(fakeQuestion.id) } returns Optional.of(fakeQuestion)

        val createdQuestion = questionService.create(fakeQuestionRequest)

        createdQuestion `should be equal to` fakeQuestion.copy(options = fakeOptions)
    }

    // TODO: Add 404 for updating question
    @Test
    fun `should update question`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { questionRepository.save(fakeQuestion.copy(questionValue = "afaf")) } returns fakeQuestion
        every { answerOptionRepository.findByQuestion(fakeQuestion) } returns fakeOptions
        every { answerOptionRepository.deleteAll(any()) } returns Unit
        every { answerOptionRepository.saveAll<AnswerOption>(any()) } returns fakeOptions // TODO: Match this properly
        every { questionRepository.findById(fakeQuestion.id) } returns Optional.of(fakeQuestion)

        val updatedQuestion = questionService.update(fakeQuestionRequest.copy(questionValue = "afaf"))

        updatedQuestion `should be equal to` fakeQuestion.copy(options = fakeOptions)
    }

    @Test
    internal fun `Should get the question`() {
        every { questionRepository.findById(fakeQuestion.id) } returns Optional.of(fakeQuestion)

        val fetchedQuestion = questionService.get(questionId = fakeQuestion.id, questionnaireId = fakeQuestionnaire.id)

        fetchedQuestion `should be equal to` fakeQuestion
    }

    @Test
    internal fun `Should throw 404 for non existent question`() {
        every { questionRepository.findById(fakeQuestion.id) } returns Optional.empty()

        invoking {
            questionService.get(
                fakeQuestionnaire.id,
                fakeQuestion.id
            )
        } shouldThrow EntityNotFoundException("Could not find question.")
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion =
    Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeOptions = listOf(
    AnswerOption("a", "Monday", fakeQuestion),
    AnswerOption("b", "Monday", fakeQuestion),
    AnswerOption("c", "Monday", fakeQuestion),
    AnswerOption("d", "Monday", fakeQuestion)
)
private val fakeOptionsRequest = listOf(
    AnswerOptionRequest(1, "a", "Monday"),
    AnswerOptionRequest(2, "b", "Monday"),
    AnswerOptionRequest(3, "c", "Monday"),
    AnswerOptionRequest(4, "d", "Monday")
)
private val fakeQuestionRequest =
    QuestionRequest(
        id = 1,
        questionValue = "Question?",
        questionnaireId = fakeQuestionnaire.id,
        options = fakeOptionsRequest
    )
