package com.research.surveyor.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "SurveyAnswer")
data class SurveyAnswer(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int = 0,
    val questionnaireId: Long,
    val questionId: Long,
    val answerOptionId: Long
)
