package com.sicredi.votingsystem.service;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import com.sicredi.votingsystem.dto.VotingAgendaSaveDTO;
import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.repository.VotingAgendaRepository;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingAgendaService {

    private static final Long DEFAULT_VOTING_DURATION = 100L;

    private final VotingAgendaRepository repository;

    public VotingAgendaService(VotingAgendaRepository repository) {
        this.repository = repository;
    }

    public VotingAgenda findById(Long id) throws ApiException {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("Agenda not found", HttpStatus.NOT_FOUND));
    }

    public VotingAgenda save(VotingAgendaSaveDTO dto) {
        val votingAgenda = new VotingAgenda();
        votingAgenda.setDescription(dto.getDescription());
        return repository.save(votingAgenda);
    }

    public void start(Long id, Long seconds) throws ApiException {
        val votingAgenda = findById(id);
        if (votingAgenda.alreadyStarted()) {
            throw new ApiException("Agenda already started", HttpStatus.PRECONDITION_FAILED);
        }
        val now = LocalDateTime.now();
        votingAgenda.setStartedAt(now);
        votingAgenda.setClosedAt(now.plusSeconds(firstNonNull(seconds, DEFAULT_VOTING_DURATION)));
        repository.save(votingAgenda);
    }
}
