package com.research.surveyor.repositories

import com.research.surveyor.controllers.request.AnswerOptionRequest
import com.research.surveyor.controllers.request.QuestionRequest
import com.research.surveyor.models.AnswerOption
import com.research.surveyor.models.Question
import com.research.surveyor.models.Questionnaire
import com.research.surveyor.models.QuestionnaireStatus
import com.research.surveyor.models.SurveyAnswer
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class SurveyAnswersRepositoryTest {

    @Autowired
    private lateinit var surveyAnswerRepository: SurveyAnswerRepository

    @Autowired
    private lateinit var questionnaireRepository: QuestionnaireRepository

    @Autowired
    private lateinit var questionRepository: QuestionRepository

    @Autowired
    private lateinit var answersOptionsRepository: AnswerOptionRepository

    private lateinit var answerOption: AnswerOption
    private lateinit var question: Question
    private lateinit var questionnaire: Questionnaire

    @BeforeEach
    fun init() {
        questionnaire = questionnaireRepository.save(fakeQuestionnaire)
        question = questionRepository.save(fakeQuestion.copy(questionnaireId = questionnaire.id))
        answerOption = answersOptionsRepository.save(fakeOptions[0].copy(question = question))
    }

    @Test
    internal fun `should save the answer`() {
        val savedQuestionAnswer = surveyAnswerRepository.save(
            SurveyAnswer(
                questionId = question.id,
                answerOptionId = answerOption.id
            )
        )

        savedQuestionAnswer `should be equal to` SurveyAnswer(
            questionId = question.id,
            answerOptionId = answerOption.id
        ).copy(id = savedQuestionAnswer.id)
    }

    @Test
    internal fun `should fetch all the survey answers`() {
        val savedQuestionAnswer = surveyAnswerRepository.save(
            SurveyAnswer(
                questionId = question.id,
                answerOptionId = answerOption.id
            )
        )

        savedQuestionAnswer `should be equal to` SurveyAnswer(
            questionId = question.id,
            answerOptionId = answerOption.id
        ).copy(id = savedQuestionAnswer.id)
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
    QuestionRequest(
        id = 1,
        questionValue = "Question?",
        questionnaireId = fakeQuestionnaire.id,
        options = fakeOptionsRequest
    )

