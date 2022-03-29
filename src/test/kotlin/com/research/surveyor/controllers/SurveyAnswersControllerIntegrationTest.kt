package com.research.surveyor.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.research.surveyor.controllers.request.Answer
import com.research.surveyor.controllers.request.SurveyAnswersDto
import com.research.surveyor.models.AnswerOptionPercentile
import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswer
import com.research.surveyor.services.SurveyAnswersService
import io.mockk.every
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
@WebMvcTest(controllers = [SurveyAnswersController::class])
@ContextConfiguration
internal class SurveyAnswersControllerIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var surveyAnswersService: SurveyAnswersService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `Should create a survey response for a survey`() {
        every { surveyAnswersService.create(fakeSurveyAnswersRequest) } returns listOf(fakeSurveyAnswers.copy(id = 1))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/survey-answers")
                .content(objectMapper.writeValueAsBytes(fakeSurveyAnswersRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(objectMapper.writeValueAsString(fakeSurveyAnswersRequest.copy(questionnaireId = 1)))
            )
    }

    @Test
    internal fun `Should get stats for an answer`() {
        every { surveyAnswersService.getStatsForAnswerOptions(1, 1) } returns fakeQuestionAnswerStats

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/questionnaires/1/questions/1/answer-options/stats")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(fakeQuestionAnswerStats))
            )
    }
}

private val fakeSurveyAnswers = SurveyAnswer(
    questionnaireId = 1,
    questionId = 1,
    answerOptionId = 1
)
private val fakeSurveyAnswersRequest = SurveyAnswersDto(
    questionnaireId = 1,
    answers = listOf(Answer(questionId = 1, answerOptionId = 1))
)

private val fakeQuestionAnswerStats = QuestionAnswerStats(
    questionId = 1,
    totalVotes = 100,
    answerOptionPercentiles = listOf(
        AnswerOptionPercentile(1, 20),
        AnswerOptionPercentile(2, 20),
        AnswerOptionPercentile(3, 20),
        AnswerOptionPercentile(4, 20),
        AnswerOptionPercentile(5, 20)
    )
)
