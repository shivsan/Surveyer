package com.research.surveyor.controllers.request

import com.research.surveyor.models.SurveyAnswer

data class SurveyAnswersDto(
    val id: Long = 0,
    val questionnaireId: Long,
    val answers: List<SurveyAnswer>,
)
