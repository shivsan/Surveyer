package com.research.surveyor.controllers

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/questionnaires")
class QuestionnaireController(private val questionnaireService: QuestionnaireService) {

    @PostMapping
    fun create(@RequestBody questionnaire: Questionnaire): ResponseEntity<Questionnaire> {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionnaireService.create(questionnaire))
    }

    fun update() {

    }

    fun get() {

    }
}
