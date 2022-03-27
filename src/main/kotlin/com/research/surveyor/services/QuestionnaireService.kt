package com.research.surveyor.services

import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.repositories.QuestionnaireRepository

class QuestionnaireService(private val questionnaireRepository: QuestionnaireRepository) {
    fun create(questionnaireToCreate: Questionnaire): Questionnaire {
        if (questionnaireToCreate.status != QuestionnaireStatus.DRAFT)
            throw InvalidRequestException("Only draft questionnaires can be created")

        return questionnaireRepository.save(questionnaireToCreate)
    }

    fun get(questionnaireId: Long): Questionnaire {
        return questionnaireRepository.findById(questionnaireId)
            .orElseThrow { EntityNotFoundException("Could not find questionnaire.") }
    }

    // TODO: Handle status transition
    fun update(questionnaireToUpdate: Questionnaire): Questionnaire {
        return questionnaireRepository.save(questionnaireToUpdate)
    }
}
