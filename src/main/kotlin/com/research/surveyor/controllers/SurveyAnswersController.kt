package com.research.surveyor.controllers

import com.research.surveyor.controllers.request.Answer
import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswer
import com.research.surveyor.services.SurveyAnswersService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SurveyAnswersController(private val surveyAnswersService: SurveyAnswersService) {
    @PostMapping("/survey-answers")
    fun create(@RequestBody surveyAnswersRequest: SurveyAnswersDto): ResponseEntity<SurveyAnswersDto> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(surveyAnswersService.create(surveyAnswersRequest).toSurveyAnswersDto())
    }

    @GetMapping("/questionnaires/{questionnaireId}/questions/{questionId}/answer-options/stats")
    fun getStatsForAnswerOptions(
        @PathVariable("questionnaireId") questionnaireId: Long,
        @PathVariable("questionId") questionId: Long,
    ): ResponseEntity<QuestionAnswerStats> {
        return ResponseEntity.ok(surveyAnswersService.getStatsForAnswerOptions(questionnaireId, questionId))
    }
}

private fun List<SurveyAnswer>.toSurveyAnswersDto(): SurveyAnswersDto {
    return SurveyAnswersDto(
        this[0].questionnaireId,
        answers = this.map { surveyAnswer -> Answer(surveyAnswer.questionId, surveyAnswer.answerOptionId) })
}
