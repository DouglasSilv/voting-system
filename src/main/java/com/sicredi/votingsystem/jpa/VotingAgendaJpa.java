package com.sicredi.votingsystem.jpa;

import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.jpa.projections.VotingAgendaResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingAgendaJpa extends JpaRepository<VotingAgenda, Long> {

    @Query("select a.id as id, count(yv.id) as positiveVotes, count(nv.id) as negativeVotes from VotingAgenda a " +
           "left join a.votes yv on yv.option = 'YES'" +
           "left join a.votes nv on nv.option = 'NO'" +
           "where a.id = :id")
    Optional<VotingAgendaResume> findResumeById(Long id);

}
