package com.sicredi.votingsystem.service;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import com.sicredi.votingsystem.dto.VotingAgendaResumeDTO;
import com.sicredi.votingsystem.dto.VotingAgendaSaveDTO;
import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.repository.VotingAgendaRepository;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
public class VotingAgendaService {

    private static final Long DEFAULT_VOTING_DURATION = 100L;

    private final VotingAgendaRepository repository;

    public VotingAgendaService(VotingAgendaRepository repository) {
        this.repository = repository;
    }

    public VotingAgenda findById(Long id) throws ApiException {
        return repository.findById(id)
                .orElseThrow(getAgendaNotFoundException());
    }

    private Supplier<ApiException> getAgendaNotFoundException() {
        return () -> new ApiException("Agenda not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Save a new voting agenda
     *
     * @param dto dto to save
     * @return new voting agenda id's
     */
    @Transactional
    public Long save(VotingAgendaSaveDTO dto) {
        val votingAgenda = new VotingAgenda();
        votingAgenda.setDescription(dto.getDescription());
        return repository.save(votingAgenda).getId();
    }

    /**
     * Start a session by agenda
     *
     * @param id agenda id's
     * @param seconds session duration in seconds, default is 100
     */
    @Transactional
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

    /**
     * Get agenda resume by id (positive and negative votes)
     *
     * @param id agenda id's
     * @return voting agenda resume dto
     */
    public VotingAgendaResumeDTO findResumeById(Long id) throws ApiException {
        val resume = repository.findResumeById(id)
                .orElseThrow(getAgendaNotFoundException());
        return new VotingAgendaResumeDTO()
                .setId(resume.getId())
                .setPositiveVotes(resume.getPositiveVotes())
                .setNegativeVotes(resume.getNegativeVotes());
    }

}
