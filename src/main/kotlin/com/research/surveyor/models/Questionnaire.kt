package com.research.surveyor.models

data class Questionnaire(
    val id: Int = 0,
    val title: String,
    val status: QuestionnaireStatus
)

enum class QuestionnaireStatus {
    DRAFT,
    PUBLISHED,
    CLOSED
}
