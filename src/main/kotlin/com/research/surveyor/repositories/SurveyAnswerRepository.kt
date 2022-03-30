package com.research.surveyor.repositories

import com.research.surveyor.models.SurveyAnswer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SurveyAnswerRepository: CrudRepository<SurveyAnswer, Long> {
    fun findAllByQuestionId(questionId: Long): List<SurveyAnswer>
}
