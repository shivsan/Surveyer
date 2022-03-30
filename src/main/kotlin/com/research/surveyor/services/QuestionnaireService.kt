package com.research.surveyor.services

import com.research.surveyor.exceptions.ConflictException
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.repositories.QuestionnaireRepository
import org.springframework.stereotype.Service

@Service
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

    fun update(questionnaireToUpdate: Questionnaire): Questionnaire {
        val questionnaire = questionnaireRepository.findById(questionnaireToUpdate.id)
        if(questionnaire.isEmpty)
            throw EntityNotFoundException("Could not find questionnaire.")

        if (questionnaire.get().status != QuestionnaireStatus.DRAFT)
            throw ConflictException("Cannot update questionnaires not in DRAFT.")

        return questionnaireRepository.save(questionnaireToUpdate)
    }
}
