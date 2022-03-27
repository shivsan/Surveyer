package com.research.surveyor.controllers

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.services.QuestionnaireService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class QuestionnaireControllerTest {
    private val questionnaireService = mockk<QuestionnaireService>()
    private val questionnaireController = QuestionnaireController(questionnaireService)

    @Test
    fun `should create questionnaire`() {
        val questionnaireToCreate = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
        every { questionnaireService.create(questionnaireToCreate) } returns questionnaireToCreate.copy(id = 1)

        val createdQuestionnaire = questionnaireController.create(questionnaireToCreate)

        createdQuestionnaire `should be equal to` questionnaireToCreate.copy(id = 1)
    }
}
