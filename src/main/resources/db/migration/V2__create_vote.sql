CREATE TABLE en_vote (
    vote_id int(11) NOT NULL AUTO_INCREMENT,
    vote_option VARCHAR(5) NOT NULL,
    agenda_id int(11) NOT NULL,
    legal_id VARCHAR(20) NOT NULL,
    PRIMARY KEY (vote_id),
    UNIQUE KEY uk_agenda_legal_id (agenda_id, legal_id),
    FOREIGN KEY (agenda_id) REFERENCES en_voting_agenda(voting_agenda_id)
);