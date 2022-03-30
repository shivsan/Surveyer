package com.research.surveyor.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "SurveyAnswers")
data class SurveyAnswers(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val questionnaireId: Long,
)

@Entity(name = "SurveyAnswer")
data class SurveyAnswer(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val surveyAnswersId: Long = 0,
    val questionId: Long,
    val answerOptionId: Long
)
