package com.sicredi.votingsystem.jpa;

import com.sicredi.votingsystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteJpa extends JpaRepository<Vote, Long> {}
