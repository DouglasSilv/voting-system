package com.sicredi.votingsystem.repository;

import com.sicredi.votingsystem.entity.Vote;
import com.sicredi.votingsystem.jpa.VoteJpa;
import org.springframework.stereotype.Repository;

@Repository
public class VoteRepository {

    private final VoteJpa jpa;

    public VoteRepository(VoteJpa jpa) {
        this.jpa = jpa;
    }

    public Vote save(Vote vote) {
        return jpa.save(vote);
    }

    public boolean existsByAgendaIdAndLegalId(Long votingAgendaId, String legalId) {
        return jpa.existsByAgendaIdAndLegalId(votingAgendaId, legalId);
    }
}
