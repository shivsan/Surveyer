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
ALTER TABLE Question
    ADD CONSTRAINT QUESTION_QUESTIONNAIRE_ID_QUESTIONNAIRE_FK
        FOREIGN KEY (questionnaire_id) REFERENCES Questionnaire(id);
