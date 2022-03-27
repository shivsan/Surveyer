package com.research.surveyor.models

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "Questionnaire")
data class Questionnaire(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val title: String,
    val status: QuestionnaireStatus,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "questionnaire", fetch = FetchType.EAGER)
    val questions: List<Question> = emptyList()
)

enum class QuestionnaireStatus {
    DRAFT,
    PUBLISHED,
    CLOSED
}
