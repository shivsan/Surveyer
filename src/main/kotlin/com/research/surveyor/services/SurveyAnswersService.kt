package com.research.surveyor.services

import com.research.surveyor.models.QuestionAnswerStats
import com.research.surveyor.models.SurveyAnswers
import org.springframework.stereotype.Service

@Service
class SurveyAnswersService {
    fun create(surveyAnswers: SurveyAnswers): SurveyAnswers {
        TODO("Not yet implemented")
        // TODO: Validate question ids and answer options id
    }

    fun getStatsForAnswerOptions(questionnaireId: Long, questionId: Long): QuestionAnswerStats {
        TODO("Not yet implemented")
    }
}
