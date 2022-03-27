package com.research.surveyor.controllers

import com.research.surveyor.controllers.request.QuestionRequest
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
        every { questionService.create(questionToCreate) } returns fakeQuestion

        val createdQuestion = questionController.create(fakeQuestionnaire.id, questionToCreate)

        createdQuestion.body `should be equal to` fakeQuestion.copy(id = 1)
    }

    @Test
    fun `should get a question`() {
        val questionToFetch = fakeQuestion
        every { questionService.get(questionnaireId = fakeQuestionnaire.id, questionId = fakeQuestion.id) } returns questionToFetch

        val fetchedQuestion = questionController.get(fakeQuestionnaire.id, fakeQuestion.id)

        fetchedQuestion.body `should be equal to` questionToFetch
    }

    @Test
    fun `should update the question`() {
        val questionToUpdate = fakeQuestion
        every { questionService.update(fakeQuestionRequest) } returns questionToUpdate

        val fetchedQuestion = questionController.update(fakeQuestionnaire.id, fakeQuestion.id, fakeQuestionRequest)

        fetchedQuestion.statusCode `should be equal to` HttpStatus.NO_CONTENT
        verify { questionService.update(fakeQuestionRequest) }
    }
}

private val questionValue = "New question"
private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion = Question(id = 1, questionValue = "Question?", questionnaire = fakeQuestionnaire)
private val fakeQuestionRequest = QuestionRequest(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
