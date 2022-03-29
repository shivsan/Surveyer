package com.research.surveyor.services

import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswer
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
    fun create(surveyAnswersRequest: SurveyAnswersDto): List<SurveyAnswer> {
        validate(surveyAnswersRequest)

        return surveyAnswersRepository.saveAll(surveyAnswersRequest.toSurveyAnswers()).toList()
    }

    private fun validate(surveyAnswersRequest: SurveyAnswersDto) {
        if (hasDuplicateAnswers(surveyAnswersRequest))
            throw InvalidRequestException("Duplicate answer for the same question")

        if (!questionnaireRepository.existsById(surveyAnswersRequest.questionnaireId))
            throw EntityNotFoundException("Questionnaire does not exist")
        surveyAnswersRequest.answers.forEach { answer ->
            if (!questionRepository.existsById(answer.questionId)) {
                throw EntityNotFoundException("Question with id ${answer.questionId} does not exist")
            }
            if (!answerOptionRepository.existsById(answer.answerOptionId)) {
                throw EntityNotFoundException("Answer option with id ${answer.answerOptionId} does not exist")
            }
        }
        val surveyQuestions = questionRepository.findByQuestionnaireId(surveyAnswersRequest.questionnaireId)
        surveyQuestions.forEach { surveyQuestion ->
            if (surveyAnswersRequest.answers.none { answer -> answer.questionId == surveyQuestion.id })
                throw InvalidRequestException("The survey is incomplete.")
        }
    }

    private fun hasDuplicateAnswers(surveyAnswers: SurveyAnswersDto) = surveyAnswers.answers
        .groupBy { answer -> answer.questionId }
        .toList()
        .any { questionIdWithAnswersGrouping -> questionIdWithAnswersGrouping.second.size > 1 }

    fun getStatsForAnswerOptions(questionnaireId: Long, questionId: Long): QuestionAnswerStats {
        TODO("Not yet implemented")
    }
}

private fun SurveyAnswersDto.toSurveyAnswers(): List<SurveyAnswer> {
    return this.answers.map { answer ->
        SurveyAnswer(
            questionnaireId = this.questionnaireId,
            answerOptionId = answer.answerOptionId,
            questionId = answer.questionId
        )
    }
}
