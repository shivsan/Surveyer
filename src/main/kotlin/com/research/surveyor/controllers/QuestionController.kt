package com.research.surveyor.controllers

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
        @RequestBody question: Question
    ): ResponseEntity<Question> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(questionService.create(question.copy(questionnaireId = questionnaireId)))
    }

    @PutMapping("/questionnaires/{questionnaireId}/questions/{id}")
    fun update(@PathVariable questionnaireId: Long,@PathVariable id: Long, @RequestBody question: Question): ResponseEntity<Unit> {
        questionService.update(question.copy(id = id, questionnaireId = questionnaireId))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/questionnaires/{questionnaireId}/questions/{id}")
    fun get(@PathVariable questionnaireId: Long, @PathVariable id: Long): ResponseEntity<Question> {
        return ResponseEntity.ok(questionService.get(questionId = id, questionnaireId = questionnaireId))
    }
}
