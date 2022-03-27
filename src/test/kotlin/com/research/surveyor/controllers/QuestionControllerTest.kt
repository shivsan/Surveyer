package com.research.surveyor.controllers

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
        val questionToCreate = Question(questionValue = "New question", questionnaireId = fakeQuestionnaire.id)
        every { questionService.create(questionToCreate) } returns questionToCreate.copy(id = 1)

        val createdQuestion = questionController.create(fakeQuestionnaire.id, questionToCreate)

        createdQuestion.body `should be equal to` questionToCreate.copy(id = 1)
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
        every { questionService.update(fakeQuestion) } returns questionToUpdate

        val fetchedQuestion = questionController.update(fakeQuestionnaire.id, fakeQuestion.id, fakeQuestion)

        fetchedQuestion.statusCode `should be equal to` HttpStatus.NO_CONTENT
        verify { questionService.update(fakeQuestion) }
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion = Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
