package com.research.surveyor.models

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity(name = "Question")
data class Question(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val questionValue: String,
    @ManyToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTIONNAIRE_ID")
    val questionnaire: Questionnaire
)
