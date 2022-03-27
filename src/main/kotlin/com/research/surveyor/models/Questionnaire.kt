package com.research.surveyor.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "Questionnaire")
data class Questionnaire(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val title: String,
    val status: QuestionnaireStatus
)

enum class QuestionnaireStatus {
    DRAFT,
    PUBLISHED,
    CLOSED
}
