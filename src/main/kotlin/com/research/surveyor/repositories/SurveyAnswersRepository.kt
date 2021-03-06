package com.research.surveyor.repositories

import com.research.surveyor.models.SurveyAnswers
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SurveyAnswersRepository: CrudRepository<SurveyAnswers, Long>
