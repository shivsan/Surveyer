package com.research.surveyor.services

import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.AnswerOptionPercentile
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswer
import com.research.surveyor.models.SurveyAnswers
import com.research.surveyor.repositories.AnswerOptionRepository
import com.research.surveyor.repositories.QuestionRepository
import com.research.surveyor.repositories.QuestionnaireRepository
import com.research.surveyor.repositories.SurveyAnswerRepository
import com.research.surveyor.repositories.SurveyAnswersRepository
import org.springframework.stereotype.Service

@Service
class SurveyAnswersService(
    private val questionRepository: QuestionRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val answerOptionRepository: AnswerOptionRepository,
    private val surveyAnswersRepository: SurveyAnswersRepository,
    private val surveyAnswerRepository: SurveyAnswerRepository
) {
    fun create(surveyAnswersRequest: SurveyAnswersDto): SurveyAnswersDto {
        validate(surveyAnswersRequest)
        val savedSurveyAnswers =
            surveyAnswersRepository.save(SurveyAnswers(questionnaireId = surveyAnswersRequest.questionnaireId))
        return SurveyAnswersDto(
            savedSurveyAnswers.id,
            savedSurveyAnswers.questionnaireId,
            surveyAnswerRepository.saveAll(surveyAnswersRequest.toSurveyAnswers(savedSurveyAnswers)).toList()
        )
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
        val allSurveyAnswers = surveyAnswerRepository.findAllByQuestionId(questionId)
        val allAnswerOptions = answerOptionRepository.findByQuestionId(questionId)
        return QuestionAnswerStats(
            questionId,
            allAnswerOptions.map { answerOption ->
                AnswerOptionPercentile(
                    answerOption.id,
                    allSurveyAnswers
                        .filter { surveyAnswer -> surveyAnswer.answerOptionId == answerOption.id }
                        .size.toLong()
                )
            },
            allSurveyAnswers.size.toLong()
        )
    }
}

private fun SurveyAnswersDto.toSurveyAnswers(savedSurveyAnswers: SurveyAnswers): List<SurveyAnswer> {
    return this.answers.map { answer ->
        SurveyAnswer(
            surveyAnswersId = savedSurveyAnswers.id,
            questionId = answer.questionId,
            answerOptionId = answer.answerOptionId
        )
    }
}
