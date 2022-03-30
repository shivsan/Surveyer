# Surveyor
This is a REST service for creating questionnaires for surveying and taking responses for the questionnaires.

**Questionnaires** can be created. A **Questionnaire** has a set of **Questions**. A question has a set of **Answer Options**.
The questionnaire can be **PUBLISHED** by updating it.

**SurveyAnswers**s can be submitted for **published** **questionnaires**.
Each **SurveyAnswers** has a list of **SurveyAnswer**s. Each **SurveyAnswer** is a mapping between a question, and an answer option.
Statistics of **votes** for each **answer option** per **question** can be retrieved.
**Total votes** is also returned.
From the response, percentage of votes can be calculated.

The following tables are created:
- Questionnaire
- Question
- AnswerOption
- SurveyAnswers (Represents a full set of survey response from a single user)
- SurveyAnswer (Represents one answer in feedback from a user)

Statistics is returned as:
- Number of votes per **answer option** for the question
- Total number of votes


Here are the endpoints:
1. Create/update/get a questionnare
   1. Create: POST '/questionnaires'
   2. Get: GET '/questionnaires/{id}'
   3. Update: PUT '/questionnaires/{id}'
2. Create/update/get/delete a question
    1. Create with all answer options: POST '/questionnaires/{questionnaireId}/questions/{id}'
    2. Get with all answer options: GET '/questionnaires/{questionnaireId}/questions/{id}'
    3. Update with all answer options: PUT '/questionnaires/{questionnaireId}/questions/{id}'
    4. Delete: DELETE '/questionnaires/{questionnaireId}/questions/{id}'
    5. Get all questions with all answer options: GET '/questions'
3. Create survey and get stats
   1. Submit a survey response : POST '/survey-answers'
   2. Get the statistics for an answer: GET '/questionnaires/1/questions/2/answer-options/stats'
