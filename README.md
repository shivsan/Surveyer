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
