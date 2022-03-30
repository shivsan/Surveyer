package com.research.surveyor.repositories

import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerOptionRepository: CrudRepository<AnswerOption, Long> {
    fun findByQuestionId(questionId: Long): List<AnswerOption>
}
