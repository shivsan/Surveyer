package com.research.surveyor.controllers

import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.models.Question
import com.research.surveyor.services.QuestionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class QuestionController(private val questionService: QuestionService) {

    @PostMapping("/questionnaires/{questionnaireId}/questions")
    fun create(
        @PathVariable("questionnaireId") questionnaireId: Long,
        @RequestBody questionRequest: QuestionRequest
    ): ResponseEntity<Question> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(questionService.create(questionRequest.copy(questionnaireId = questionnaireId)))
    }

    @PutMapping("/questionnaires/{questionnaireId}/questions/{id}")
    fun update(@PathVariable questionnaireId: Long,@PathVariable id: Long, @RequestBody questionRequest: QuestionRequest): ResponseEntity<Unit> {
        questionService.update(questionRequest.copy(id = id, questionnaireId = questionnaireId))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/questionnaires/{questionnaireId}/questions/{id}")
    fun get(@PathVariable questionnaireId: Long, @PathVariable id: Long): ResponseEntity<Question> {
        return ResponseEntity.ok(questionService.get(questionId = id, questionnaireId = questionnaireId))
    }
}
