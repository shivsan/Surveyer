package com.research.surveyor.repositories

import com.research.surveyor.models.Questionnaire
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionnaireRepository: CrudRepository<Questionnaire, Long>
