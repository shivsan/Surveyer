package com.research.surveyor.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.services.QuestionnaireService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [QuestionnaireController::class])
@ContextConfiguration
class QuestionnaireControllerIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var questionnaireService: QuestionnaireService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `Should create questionnaire`() {
        every { questionnaireService.create(fakeQuestionnaire) } returns fakeQuestionnaire.copy(id = 1)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/questionnaires")
                .content(objectMapper.writeValueAsBytes(fakeQuestionnaire))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
    }
}

private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
