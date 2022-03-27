package com.research.surveyor.repositories

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class QuestionnaireRepositoryTest {

    @Autowired
    private lateinit var questionnaireRepository: QuestionnaireRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    internal fun `should save the questionnaire`() {
        val savedQuestionnaire = questionnaireRepository.save(fakeQuestionnaire)

        savedQuestionnaire.id `should not be` 0

        // TODO: Improve fetching. Can't expect to get the id as 1, unless the db is cleared.
        savedQuestionnaire `should be equal to` fakeQuestionnaire.copy(id = savedQuestionnaire.id)
    }

    @Test
    internal fun `should get the questionnaire`() {
        val savedQuestionnaire = questionnaireRepository.save(fakeQuestionnaire.copy(title = "New questionnaire."))

        val fetchedQuestionnaire = questionnaireRepository.findById(savedQuestionnaire.id)

        fetchedQuestionnaire.get() `should be equal to` savedQuestionnaire
    }

    @Test
    internal fun `should update the questionnaire`() {
        val savedQuestionnaire = questionnaireRepository.save(fakeQuestionnaire)

        questionnaireRepository.save(savedQuestionnaire.copy(title = "Changed title"))

        val fetchUpdatedQuestionnaire = questionnaireRepository.findById(savedQuestionnaire.id).get()

        fetchUpdatedQuestionnaire `should be equal to` savedQuestionnaire.copy(title = "Changed title")
    }
}

private val fakeQuestionnaire =
    Questionnaire(title = "New questionnaire - creation", status = QuestionnaireStatus.DRAFT)
