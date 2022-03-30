package com.research.surveyor.services

import com.research.surveyor.exceptions.ConflictException
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class QuestionnaireServiceTest {
    private val questionnaireRepository = mockk<QuestionnaireRepository>()
    private val questionRepository = mockk<QuestionRepository>()
    private val questionnaireService = QuestionnaireService(questionnaireRepository, questionRepository)

    @Test
    fun `should create questionnaire`() {
        every { questionnaireRepository.save(fakeQuestionnaire) } returns fakeQuestionnaire.copy(id = 1)

        val createdQuestionnaire = questionnaireService.create(fakeQuestionnaire)

        createdQuestionnaire `should be equal to` fakeQuestionnaire.copy(id = 1)
    }

    @Test
    fun `should not allow to create non-DRAFT questionnaires`() {
        val questionnaireToCreate = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.CLOSED)

        invoking { questionnaireService.create(questionnaireToCreate) } shouldThrow InvalidRequestException("Only draft questionnaires can be created")

        verify(exactly = 0) { questionnaireRepository.save(any()) }
    }

    @Test
    fun `should update questionnaire`() {
        every { questionnaireRepository.findById(1) } returns Optional.of(fakeQuestionnaire)
        every { questionnaireRepository.save(fakeQuestionnaire.copy(1)) } returns fakeQuestionnaire.copy(title = "afaf")

        val updatedQuestionnaire = questionnaireService.update(fakeQuestionnaire.copy(1))

        updatedQuestionnaire `should be equal to` fakeQuestionnaire.copy(title = "afaf")
    }

    @Test
    internal fun `Should get the questionnaire`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)

        val fetchedQuestionnaire = questionnaireService.get(fakeQuestionnaire.id)

        fetchedQuestionnaire `should be equal to` fakeQuestionnaire
    }

    @Test
    internal fun `Should throw 404 for non existent questionnaire`() {
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.empty()

        invoking { questionnaireService.get(fakeQuestionnaire.id) } shouldThrow EntityNotFoundException::class
    }

    @Test
    internal fun `Should throw 404 for non existent questionnaire, when updating`() {
        every { questionnaireRepository.findById(1) } returns Optional.empty()

        invoking { questionnaireService.update(fakeQuestionnaire.copy(id = 1)) } shouldThrow EntityNotFoundException::class
    }

    @Test
    internal fun `Should not allow to update a published questionnaire`() {
        val fakeQuestionnaireWithId = fakeQuestionnaire.copy(1)
        every { questionnaireRepository.findById(fakeQuestionnaireWithId.id) } returns Optional.of(
            fakeQuestionnaireWithId.copy(status = QuestionnaireStatus.PUBLISHED)
        )

        invoking { questionnaireService.update(fakeQuestionnaireWithId) } shouldThrow ConflictException::class
    }

    @Test
    fun `should not publish an empty questionnaire`() {
        val questionnaireToUpdate = fakeQuestionnaire.copy(status = QuestionnaireStatus.PUBLISHED)
        every { questionnaireRepository.findById(fakeQuestionnaire.id) } returns Optional.of(fakeQuestionnaire)
        every { questionRepository.findByQuestionnaireId(questionnaireToUpdate.id) } returns emptyList()

        invoking { questionnaireService.update(questionnaireToUpdate) } shouldThrow InvalidRequestException::class
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
