package com.research.surveyor.services

import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.exceptions.ConflictException
import com.research.surveyor.exceptions.EntityNotFoundException
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.AnswerOptionPercentile
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.QuestionnaireStatus
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

        val questionnaire = questionnaireRepository.findById(surveyAnswersRequest.questionnaireId)
        if (questionnaire.isEmpty)
            throw EntityNotFoundException("Questionnaire does not exist")
        if (questionnaire.get().status != QuestionnaireStatus.PUBLISHED)
            throw ConflictException("Questionnaire is not published.")
        surveyAnswersRequest.answers.forEach { answer ->
            if (!answerOptionRepository.existsByQuestionIdAndId(answer.questionId, answer.answerOptionId)) {
                throw InvalidRequestException("Answer option with id ${answer.answerOptionId} does not match for question with id ${answer.questionId}")
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
        if (!questionnaireRepository.existsById(questionnaireId))
            throw EntityNotFoundException("Could not find questionnaire.")
        if (!questionRepository.existsById(questionId))
            throw EntityNotFoundException("Could not find question.")

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
