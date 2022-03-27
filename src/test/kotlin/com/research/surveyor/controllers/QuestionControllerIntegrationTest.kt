package com.research.surveyor.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.research.surveyor.exceptions.InvalidRequestException
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.services.QuestionService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [QuestionController::class])
@ContextConfiguration
class QuestionControllerIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var questionService: QuestionService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `Should create question`() {
        every { questionService.create(fakeQuestion) } returns fakeQuestion.copy(id = 1)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/questionnaires/${fakeQuestionnaire.id}/questions")
                .content(objectMapper.writeValueAsBytes(fakeQuestion))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(fakeQuestion.copy(id = 1)))
            )
    }

    @Test
    internal fun `Should update question`() {
        every { questionService.update(fakeQuestion) } returns fakeQuestion.copy(questionValue = "New title")

        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/questionnaires/${fakeQuestionnaire.id}/questions/${fakeQuestion.id}")
                .content(objectMapper.writeValueAsBytes(fakeQuestion))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        verify { questionService.update(fakeQuestion.copy(questionValue = "New question")) }
    }

    @Test
    internal fun `Should get 400 if question request is invalid`() {
        every { questionService.create(fakeQuestion) } throws InvalidRequestException("")

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/questionnaires/${fakeQuestionnaire.id}/questions")
                .content(objectMapper.writeValueAsBytes(fakeQuestion))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        // TODO: Assert content
    }

    @Test
    internal fun `Should get question by id`() {
        every { questionService.get(fakeQuestionnaire.id, fakeQuestion.id) } returns fakeQuestion

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/questionnaires/${fakeQuestionnaire.id}/questions/${fakeQuestion.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(fakeQuestion)))
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion = Question(questionValue = "New question", questionnaireId = fakeQuestionnaire.id)
