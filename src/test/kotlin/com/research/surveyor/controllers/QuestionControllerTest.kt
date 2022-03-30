package com.research.surveyor.controllers

import com.research.surveyor.controllers.request.AnswerOptionRequest
import com.research.surveyor.controllers.request.AnswerOptionResponse
import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.controllers.request.QuestionResponse
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.services.QuestionService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class QuestionControllerTest {
    private val questionService = mockk<QuestionService>()
    private val questionController = QuestionController(questionService)

    @Test
    fun `should create question`() {
        val questionToCreate = fakeQuestionRequest
        every { questionService.create(questionToCreate) } returns fakeQuestion.copy(options = fakeOptions)

        val createdQuestion = questionController.create(fakeQuestionnaire.id, questionToCreate)

        createdQuestion.body `should be equal to` fakeQuestionResponse
    }

    @Test
    fun `should get a question`() {
        val questionToFetch = fakeQuestion
        every {
            questionService.get(
                questionnaireId = fakeQuestionnaire.id,
                questionId = fakeQuestion.id
            )
        } returns questionToFetch.copy(options = fakeOptions)

        val fetchedQuestion = questionController.get(fakeQuestionnaire.id, fakeQuestion.id)

        fetchedQuestion.statusCode `should be equal to` HttpStatus.OK
        fetchedQuestion.body `should be equal to` fakeQuestionResponse
    }

    @Test
    fun `should get all questions`() {
        val questionToFetch = fakeQuestion
        every { questionService.getAll() } returns listOf(questionToFetch.copy(options = fakeOptions))

        val fetchedQuestions = questionController.getAll()

        fetchedQuestions.statusCode `should be equal to` HttpStatus.OK
        fetchedQuestions.body `should be equal to` listOf(fakeQuestionResponse)
    }

    @Test
    fun `should update the question`() {
        val questionToUpdate = fakeQuestion
        every { questionService.update(fakeQuestionRequest) } returns questionToUpdate

        val fetchedQuestion = questionController.update(fakeQuestionnaire.id, fakeQuestion.id, fakeQuestionRequest)

        fetchedQuestion.statusCode `should be equal to` HttpStatus.NO_CONTENT
        verify { questionService.update(fakeQuestionRequest) }
    }

    @Test
    fun `Should delete the question`() {
        every { questionService.delete(fakeQuestionRequest.id) } returns Unit

        questionController.delete(fakeQuestionnaire.id, fakeQuestionRequest.id)

        verify { questionService.delete(fakeQuestionRequest.id) }
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion =
    Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeOptions = listOf(
    AnswerOption(1, "a", "Monday", fakeQuestion),
    AnswerOption(2, "b", "Monday", fakeQuestion),
    AnswerOption(3, "c", "Monday", fakeQuestion),
    AnswerOption(4, "d", "Monday", fakeQuestion)
)
private val fakeOptionsRequest = listOf(
    AnswerOptionRequest(1, "a", "Monday"),
    AnswerOptionRequest(2, "b", "Monday"),
    AnswerOptionRequest(3, "c", "Monday"),
    AnswerOptionRequest(4, "d", "Monday")
)
private val fakeOptionsResponse = listOf(
    AnswerOptionResponse(1, "a", "Monday"),
    AnswerOptionResponse(2, "b", "Monday"),
    AnswerOptionResponse(3, "c", "Monday"),
    AnswerOptionResponse(4, "d", "Monday")
)
private val fakeQuestionRequest =
    QuestionRequest(
        id = 1,
        questionValue = "Question?",
        questionnaireId = fakeQuestionnaire.id,
        options = fakeOptionsRequest
    )
private val fakeQuestionResponse =
    QuestionResponse(
        id = 1,
        questionValue = "Question?",
        questionnaireId = fakeQuestionnaire.id,
        options = fakeOptionsResponse
    )
