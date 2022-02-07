package com.sicredi.votingsystem.jpa;

import com.sicredi.votingsystem.entity.VotingAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingAgendaJpa extends JpaRepository<VotingAgenda, Long> {}
