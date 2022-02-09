package com.sicredi.votingsystem.repository;

import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.jpa.VotingAgendaJpa;
import com.sicredi.votingsystem.jpa.projections.VotingAgendaResume;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VotingAgendaRepository {

    private final VotingAgendaJpa jpa;

    public VotingAgendaRepository(VotingAgendaJpa jpa) {
        this.jpa = jpa;
    }

    public VotingAgenda save(VotingAgenda agenda) {
        return jpa.save(agenda);
    }

    public Optional<VotingAgenda> findById(Long id) {
        return jpa.findById(id);
    }

    public Optional<VotingAgendaResume> findResumeById(Long id) {
        return jpa.findResumeById(id);
    }

}
