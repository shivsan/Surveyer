package com.research.surveyor.services

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.repositories.QuestionnaireRepository

class QuestionnaireService(private val questionnaireRepository: QuestionnaireRepository) {
    fun create(questionnaireToCreate: Questionnaire): Questionnaire {
        return questionnaireRepository.save(questionnaireToCreate)
    }
}
