package com.research.surveyor.services

import com.research.surveyor.controllers.request.AnswerOptionRequest
import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val answerOptionRepository: AnswerOptionRepository
) {
    fun create(questionRequest: QuestionRequest): Question {
        val questionnaire =
            questionnaireRepository.findById(questionRequest.questionnaireId)// TODO: test this, 404 this
        if (questionnaire.isEmpty)
            throw EntityNotFoundException("Could not find questionnaire")
        val question = questionRequest.toQuestion()
        val savedQuestion = questionRepository.save(question)
        val savedAnswerOptions =
            answerOptionRepository.saveAll(questionRequest.options.map { option -> option.toAnswerOption(savedQuestion) })
        return questionRepository.findById(savedQuestion.id).get().copy(options = savedAnswerOptions.toList())
    }

    fun get(questionnaireId: Long, questionId: Long): Question {
        return questionRepository.findById(questionId)
            .orElseThrow { EntityNotFoundException("Could not find question.") }
    }

    fun update(questionToUpdate: QuestionRequest): Question {
        val questionnaire = questionnaireRepository.findById(questionToUpdate.questionnaireId)
        if (questionnaire.isEmpty)
            throw EntityNotFoundException("Could not find questionnaire")

        if (questionRepository.findById(questionToUpdate.id).isEmpty)
            throw EntityNotFoundException("Could not find question.")

        val question = questionToUpdate.toQuestion()
        val savedQuestion = questionRepository.save(question)
        // TODO: Move to a persistence layer
        answerOptionRepository.deleteAll(answerOptionRepository.findByQuestion(savedQuestion))
        val savedAnswerOptions =
            answerOptionRepository.saveAll(questionToUpdate.options.map { option -> option.toAnswerOption(savedQuestion) })
        return questionRepository.findById(savedQuestion.id).get().copy(options = savedAnswerOptions.toList())
    }
}

private fun QuestionRequest.toQuestion() =
    Question(this.id, this.questionValue, this.questionnaireId)

private fun AnswerOptionRequest.toAnswerOption(question: Question) =
    AnswerOption(this.id, this.optionIndex, this.option, question)
