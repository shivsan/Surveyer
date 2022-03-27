package com.research.surveyor.controllers

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.services.QuestionnaireService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class QuestionnaireControllerTest {
    private val questionnaireService = mockk<QuestionnaireService>()
    private val questionnaireController = QuestionnaireController(questionnaireService)

    @Test
    fun `should create questionnaire`() {
        val questionnaireToCreate = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
        every { questionnaireService.create(questionnaireToCreate) } returns questionnaireToCreate.copy(id = 1)

        val createdQuestionnaire = questionnaireController.create(questionnaireToCreate)

        createdQuestionnaire.body `should be equal to` questionnaireToCreate.copy(id = 1)
    }

    @Test
    fun `should get a questionnaire`() {
        val questionnaireToFetch = fakeQuestionnaire
        every { questionnaireService.get(fakeQuestionnaire.id) } returns questionnaireToFetch

        val fetchedQuestionnaire = questionnaireController.get(fakeQuestionnaire.id)

        fetchedQuestionnaire.body `should be equal to` questionnaireToFetch
    }

    @Test
    fun `should update the questionnaire`() {
        val questionnaireToUpdate = fakeQuestionnaire
        every { questionnaireService.update(fakeQuestionnaire) } returns questionnaireToUpdate

        val fetchedQuestionnaire = questionnaireController.update(fakeQuestionnaire.id, fakeQuestionnaire)

        fetchedQuestionnaire.statusCode `should be equal to` HttpStatus.NO_CONTENT
        verify { questionnaireService.update(fakeQuestionnaire) }
    }
}

private val fakeQuestionnaire = Questionnaire(id = 1, title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
