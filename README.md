# Surveyor
This is a REST service for creating questionnaires for surveying and taking responses for the questionnaires.

**Questionnaires** can be created. A **Questionnaire** has a set of **Questions**. A question has a set of **Answer Options**.
The questionnaire can be **PUBLISHED** by updating it.

**SurveyAnswers** can be submitted for **published** **questionnaires**.
Statistics of **votes** for each **answer option** per **question** can be retrieved.
**Total votes** is also returned.
From the response, percentage of votes can be calculated.
