package com.research.surveyor.repositories

import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository: CrudRepository<Question, Long> {
    fun findByQuestionnaireId(questionnaireId: Long): List<Question>
}
