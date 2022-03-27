package com.research.surveyor.services

import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.models.Question
import com.research.surveyor.repositories.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(private val questionRepository: QuestionRepository) {
    fun create(questionToCreate: Question): Question {
        return questionRepository.save(questionToCreate)
    }

    fun get(questionnaireId: Long, questionId: Long): Question {
        return questionRepository.findById(questionId)
            .orElseThrow { EntityNotFoundException("Could not find question.") }
    }

    fun update(questionToUpdate: Question): Question {
        return questionRepository.save(questionToUpdate)
    }
}
