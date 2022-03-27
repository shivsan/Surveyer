package com.research.surveyor.controllers

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.services.QuestionnaireService

class QuestionnaireController(private val questionnaireService: QuestionnaireService) {
    fun create(questionnaire: Questionnaire): Questionnaire {
        return questionnaireService.create(questionnaire)
    }

    fun update() {

    }

    fun get() {

    }
}
