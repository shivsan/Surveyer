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
class AnswerOptionRepositoryTest {

    @Autowired
    private lateinit var questionRepository: QuestionRepository

    @Autowired
    private lateinit var answerOptionRepository: AnswerOptionRepository

    @Autowired
    private lateinit var questionnaireRepository: QuestionnaireRepository

    @Test
    internal fun `should save the answer option`() {
        val questionnaire = questionnaireRepository.save(fakeQuestionnaire)
        val savedQuestion = questionRepository.save(fakeQuestion.copy(questionnaire = questionnaire))
        val savedAnswerOption = answerOptionRepository.save(fakeAnswerOption.copy(question = savedQuestion))

        savedAnswerOption.id `should not be` 0

        savedAnswerOption `should be equal to` fakeAnswerOption.copy(
            id = savedAnswerOption.id,
            question = savedQuestion
        )
    }

    @Test
    internal fun `should update all the answer options for a question`() {
        val questionnaire = questionnaireRepository.save(fakeQuestionnaire)
        val savedQuestion = questionRepository.save(fakeQuestion.copy(questionnaire = questionnaire))
        val questionId = savedQuestion.id
        answerOptionRepository.save(fakeAnswerOption.copy(optionIndex = "a", question = savedQuestion))
        answerOptionRepository.save(fakeAnswerOption.copy(optionIndex = "b", question = savedQuestion))
        val questionToUpdate = questionRepository.findById(questionId)

        answerOptionRepository.deleteAll(questionToUpdate.get().options)
        val updatedAnswerOptions = answerOptionRepository.saveAll(
            listOf(
                fakeAnswerOption.copy(optionIndex = "a", option = "", question = savedQuestion),
                fakeAnswerOption.copy(optionIndex = "b", option = "", question = savedQuestion)
            )
        )

        val updatedQuestion = questionRepository.findById(questionId)

        updatedQuestion.get() `should be equal to` fakeQuestion
        val updatedAnswerOption1 = answerOptionRepository.findById(updatedAnswerOptions.toList()[0].id)
        val updatedAnswerOption2 = answerOptionRepository.findById(updatedAnswerOptions.toList()[1].id)

        updatedAnswerOption1.get().question `should be equal to` fakeQuestion
        updatedAnswerOption2.get().question `should be equal to` fakeQuestion
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
    Question(id = 1, questionValue = "Question?", questionnaire = fakeQuestionnaire)
private val fakeAnswerOption =
    AnswerOption("a", "Monday", fakeQuestion)

