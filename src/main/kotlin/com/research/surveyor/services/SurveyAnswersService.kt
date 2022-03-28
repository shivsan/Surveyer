package com.research.surveyor.services

import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswers
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import com.research.surveyor.repositories.SurveyAnswersRepository
import org.springframework.stereotype.Service

@Service
class SurveyAnswersService(
    private val questionRepository: QuestionRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val answerOptionRepository: AnswerOptionRepository,
    private val surveyAnswersRepository: SurveyAnswersRepository
) {
    fun create(surveyAnswers: SurveyAnswers): SurveyAnswers {
        validate(surveyAnswers)

        return surveyAnswersRepository.save(surveyAnswers)
    }

    private fun validate(surveyAnswers: SurveyAnswers) {
        if (hasDuplicateAnswers(surveyAnswers))
            throw InvalidRequestException("Duplicate answer for the same question")

        if (!questionnaireRepository.existsById(surveyAnswers.questionnaireId))
            throw EntityNotFoundException("Questionnaire does not exist")
        surveyAnswers.answers.forEach { answer ->
            if (!questionRepository.existsById(answer.questionId)) {
                throw EntityNotFoundException("Question with id ${answer.questionId} does not exist")
            }
            if (!answerOptionRepository.existsById(answer.answerOptionId)) {
                throw EntityNotFoundException("Answer option with id ${answer.answerOptionId} does not exist")
            }
        }
        val surveyQuestions = questionRepository.findByQuestionnaireId(surveyAnswers.questionnaireId)
        surveyQuestions.forEach { surveyQuestion ->
            if (surveyAnswers.answers.none { answer -> answer.questionId == surveyQuestion.id })
                throw InvalidRequestException("The survey is incomplete.")
        }
    }

    private fun hasDuplicateAnswers(surveyAnswers: SurveyAnswers) = surveyAnswers.answers
        .groupBy { answer -> answer.questionId }
        .toList()
        .any { questionIdWithAnswersGrouping -> questionIdWithAnswersGrouping.second.size > 1 }

    fun getStatsForAnswerOptions(questionnaireId: Long, questionId: Long): QuestionAnswerStats {
        TODO("Not yet implemented")
    }
}
