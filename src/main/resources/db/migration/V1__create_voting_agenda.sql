CREATE TABLE en_voting_agenda (
    voting_agenda_id int(11) NOT NULL AUTO_INCREMENT,
    description TEXT NULL,
    started_at DATETIME NULL,
    closed_at DATETIME NULL,
    PRIMARY KEY (voting_agenda_id)
);