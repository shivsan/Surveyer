package com.research.surveyor.repositories

import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class QuestionnaireRepositoryTest {

    @Autowired
    private lateinit var questionnaireRepository: QuestionnaireRepository

    @Test
    internal fun `should save the questionnaire`() {
        val savedQuestionnaire = questionnaireRepository.save(fakeQuestionnaire)

        savedQuestionnaire.id `should not be` 0

        // TODO: Improve fetching. Can't expect to get the id as 1, unless the db is cleared.
        savedQuestionnaire `should be equal to` fakeQuestionnaire.copy(id = 1)
    }
}

private val fakeQuestionnaire =
    Questionnaire(title = "New questionnaire - creation", status = QuestionnaireStatus.DRAFT)
