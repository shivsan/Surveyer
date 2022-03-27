package com.research.surveyor.services

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.repositories.QuestionnaireRepository
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class QuestionnaireServiceTest {
    private val questionnaireRepository = mockk<QuestionnaireRepository>()
    private val questionnaireService = QuestionnaireService(questionnaireRepository)

    @Test
    fun `should create questionnaire`() {
        every { questionnaireRepository.save(fakeQuestionnaire) } returns fakeQuestionnaire.copy(id = 1)

        val createdQuestionnaire = questionnaireService.create(fakeQuestionnaire)

        createdQuestionnaire `should be equal to` fakeQuestionnaire.copy(id = 1)
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
