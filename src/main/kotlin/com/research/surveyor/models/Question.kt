package com.research.surveyor.models

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity(name = "Question")
data class Question(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val questionValue: String,
    // TODO: add a question ordering
    val questionnaireId: Long,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "question", fetch = FetchType.LAZY)
    val options: List<AnswerOption> = emptyList()
)

@Entity(name = "AnswerOption")
data class AnswerOption(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long = 0,
    val optionIndex: String, // TODO: Validate ordering
    val option: String,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val question: Question
) {
    constructor(
        optionIndex: String, // TODO: Validate ordering
        option: String,
        question: Question
    ) : this(0, optionIndex, option, question)
}
