package com.research.surveyor.repositories

import com.research.surveyor.controllers.request.AnswerOptionRequest
import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private lateinit var questionRepository: QuestionRepository

    @Autowired
    private lateinit var questionnaireRepository: QuestionnaireRepository

    @BeforeEach
    fun init() {

    }

    @Test
    internal fun `should save the question`() {
        val questionnaire = questionnaireRepository.save(fakeQuestionnaire)

        val savedQuestion = questionRepository.save(fakeQuestion)

        savedQuestion.id `should not be` 0

        // TODO: Improve fetching. Can't expect to get the id as 1, unless the db is cleared.
        savedQuestion `should be equal to` fakeQuestion.copy(id = savedQuestion.id)
    }

    @Test
    internal fun `should get the question`() {
        val savedQuestion = questionRepository.save(fakeQuestion.copy(questionValue = "New question."))

        val fetchedQuestion = questionRepository.findById(savedQuestion.id)

        fetchedQuestion.get() `should be equal to` savedQuestion
    }

    @Test
    internal fun `should update the question`() {
        val savedQuestion = questionRepository.save(fakeQuestion)

        questionRepository.save(savedQuestion.copy(questionValue = "Changed title"))

        val fetchUpdatedQuestion = questionRepository.findById(savedQuestion.id).get()
        fetchUpdatedQuestion `should be equal to` savedQuestion.copy(questionValue = "Changed title")
    }
}


private val fakeQuestionnaire = Questionnaire(title = "New questionnaire", status = QuestionnaireStatus.DRAFT)
private val fakeQuestion =
    Question(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id)
private val fakeOptions = listOf(
    AnswerOption("a", "Monday", fakeQuestion),
    AnswerOption("b", "Monday", fakeQuestion),
    AnswerOption("c", "Monday", fakeQuestion),
    AnswerOption("d", "Monday", fakeQuestion)
)

private val fakeOptionsRequest = listOf(
    AnswerOptionRequest(1, "a", "Monday"),
    AnswerOptionRequest(2, "b", "Monday"),
    AnswerOptionRequest(3, "c", "Monday"),
    AnswerOptionRequest(4, "d", "Monday")
)
private val fakeQuestionRequest =
    QuestionRequest(id = 1, questionValue = "Question?", questionnaireId = fakeQuestionnaire.id, options = fakeOptionsRequest)

