CREATE TABLE IF NOT EXISTS Questionnaire (
    id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
    status varchar(255),
    title varchar(255)
);
CREATE TABLE IF NOT EXISTS Question (
    id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
    question_value varchar(255),
    questionnaire_id int(10)
);
CREATE TABLE IF NOT EXISTS AnswerOption (
    id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
    option_index varchar(10),
    "option" varchar(255),
    question_id int(10)
);
ALTER TABLE Question
    ADD CONSTRAINT QUESTION_QUESTIONNAIRE_ID_QUESTIONNAIRE_FK
        FOREIGN KEY (questionnaire_id) REFERENCES Questionnaire(id);

ALTER TABLE AnswerOption
    ADD CONSTRAINT ANSWER_OPTION_QUESTION_ID_QUESTION_FK
        FOREIGN KEY (question_id) REFERENCES Question(id);

CREATE TABLE IF NOT EXISTS SurveyAnswers (
    id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
    questionnaire_id int(10)
);

CREATE TABLE IF NOT EXISTS SurveyAnswer (
    id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
    survey_answers_id int(10),
    question_id int(10),
    answer_option_id int(10)
);
ALTER TABLE SurveyAnswers
    ADD CONSTRAINT SURVEY_ANSWERS_QUESTIONNAIRE_ID_QUESTIONNAIRE_FK
        FOREIGN KEY (questionnaire_id) REFERENCES Questionnaire(id);

ALTER TABLE SurveyAnswer
    ADD CONSTRAINT SURVEY_ANSWER_QUESTION_ID_QUESTION_FK
        FOREIGN KEY (question_id) REFERENCES Question(id);

ALTER TABLE SurveyAnswer
    ADD CONSTRAINT SURVEY_ANSWER_ANSWER_OPTION_ID_ANSWER_OPTION_FK
        FOREIGN KEY (question_id) REFERENCES Question(id);
