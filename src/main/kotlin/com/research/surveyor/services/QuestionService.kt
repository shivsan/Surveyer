package com.research.surveyor.services

import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val questionnaireRepository: QuestionnaireRepository
) {
    fun create(questionRequest: QuestionRequest): Question {
        val questionnaire = questionnaireRepository.findById(questionRequest.questionnaireId)
        return questionRepository.save(questionRequest.toQuestion(questionnaire.get())) // TODO: test this, 404 this
    }

    fun get(questionnaireId: Long, questionId: Long): Question {
        return questionRepository.findById(questionId)
            .orElseThrow { EntityNotFoundException("Could not find question.") }
    }

    fun update(questionToUpdate: QuestionRequest): Question {
        val questionnaire = questionnaireRepository.findById(questionToUpdate.questionnaireId)
        return questionRepository.save(questionToUpdate.toQuestion(questionnaire.get())) // TODO: test this, 404 this
    }
}

private fun QuestionRequest.toQuestion(questionnaire: Questionnaire) =
    Question(this.id, this.questionValue, questionnaire)
