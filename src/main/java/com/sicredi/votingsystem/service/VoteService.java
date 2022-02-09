package com.sicredi.votingsystem.service;

import com.sicredi.votingsystem.dto.VoteDTO;
import com.sicredi.votingsystem.entity.Vote;
import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.enums.UserVotingStatus;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.feign.UserFeignClient;
import com.sicredi.votingsystem.repository.VoteRepository;
import feign.FeignException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VoteService {

    private final VoteRepository repository;

    private final UserFeignClient userFeignClient;
    private final VotingAgendaService votingAgendaService;

    public VoteService(VoteRepository repository,
                       UserFeignClient userFeignClient,
                       VotingAgendaService votingAgendaService) {
        this.repository = repository;
        this.userFeignClient = userFeignClient;
        this.votingAgendaService = votingAgendaService;
    }

    /**
     * Save a new vote for agenda
     *
     * @param dto dto to save
     */
    @Transactional
    public void save(VoteDTO dto) throws ApiException {
        validateUserOnVote(dto.getLegalId());
        val agenda = votingAgendaService.findById(dto.getVotingAgendaId());
        validateAgendaStatus(agenda);
        validateUserVote(dto);
        val vote = buildNewVote(dto, agenda);
        repository.save(vote);
    }

    private Vote buildNewVote(VoteDTO dto, VotingAgenda agenda) {
        return new Vote()
                .setLegalId(dto.getLegalId())
                .setAgenda(agenda)
                .setOption(dto.getOption());
    }

    private void validateUserVote(VoteDTO dto) throws ApiException {
        val userAlreadyVoted = repository.existsByAgendaIdAndLegalId(dto.getVotingAgendaId(), dto.getLegalId());
        if (userAlreadyVoted) {
            throw new ApiException("User has already voted in this agenda", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private void validateAgendaStatus(VotingAgenda agenda) throws ApiException {
        if (!agenda.isOpen()) {
            throw new ApiException("Agenda is not open", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private void validateUserOnVote(String legalId) throws ApiException {
        try {
            val userStatus = userFeignClient.validateLegalId(legalId);
            if (UserVotingStatus.UNABLE_TO_VOTE.equals(userStatus.getStatus())) {
                throw new ApiException("User unable to vote", HttpStatus.PRECONDITION_FAILED);
            }
        } catch (FeignException.NotFound notFound) {
            throw new ApiException("Legal id not found", HttpStatus.NOT_FOUND);
        } catch (FeignException exception) {
            throw new ApiException("An unexpected error occurred during user validation",
                    HttpStatus.PRECONDITION_FAILED);
        }
    }
}
