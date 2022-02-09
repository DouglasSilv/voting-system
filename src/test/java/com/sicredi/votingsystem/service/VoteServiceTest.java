package com.sicredi.votingsystem.service;

import com.sicredi.votingsystem.dto.UserVotingStatusDTO;
import com.sicredi.votingsystem.dto.VoteDTO;
import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.enums.UserVotingStatus;
import com.sicredi.votingsystem.enums.VoteOption;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.feign.UserFeignClient;
import com.sicredi.votingsystem.repository.VoteRepository;
import feign.FeignException;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private VoteRepository repository;
    private UserFeignClient userFeignClient;
    private VotingAgendaService votingAgendaService;

    private VoteService service;

    @Before
    public void init() {
        repository = mock(VoteRepository.class);
        userFeignClient = mock(UserFeignClient.class);
        votingAgendaService = mock(VotingAgendaService.class);

        service = new VoteService(repository, userFeignClient, votingAgendaService);
    }

    @Test
    public void save_shouldRunCorrectly() throws ApiException {
        when(userFeignClient.validateLegalId(anyString()))
                .thenReturn(buildUserAbleToVoteStatusDTO());
        when(votingAgendaService.findById(anyLong()))
                .thenReturn(buildVotingAgenda());
        when(repository.existsByAgendaIdAndLegalId(anyLong(), anyString()))
                .thenReturn(false);

        service.save(buildVoteDTO());
        verify(userFeignClient, times(1))
                .validateLegalId(anyString());
        verify(votingAgendaService, times(1))
                .findById(anyLong());
        verify(repository, times(1))
                .existsByAgendaIdAndLegalId(anyLong(), anyString());
        verify(repository, times(1)).save(argThat(newVote -> {
            assertEquals(VoteOption.YES, newVote.getOption());
            assertEquals("00000000000", newVote.getLegalId());
            assertNotNull(newVote.getAgenda());
            return true;
        }));
    }

    @Test(expected = ApiException.class)
    public void save_withUserWhoHasAlreadyVoted_shouldThrowsException() throws ApiException {
        when(userFeignClient.validateLegalId(anyString()))
                .thenReturn(buildUserAbleToVoteStatusDTO());
        when(votingAgendaService.findById(anyLong()))
                .thenReturn(buildVotingAgenda());
        when(repository.existsByAgendaIdAndLegalId(anyLong(), anyString()))
                .thenReturn(true);

        service.save(buildVoteDTO());
        verify(userFeignClient, times(1))
                .validateLegalId(anyString());
        verify(votingAgendaService, times(1))
                .findById(anyLong());
        verify(repository, times(1))
                .existsByAgendaIdAndLegalId(anyLong(), anyString());
        verify(repository, never()).save(any());
    }

    @Test(expected = ApiException.class)
    public void save_withUserUnableToVote_shouldThrowsException() throws ApiException {
        when(userFeignClient.validateLegalId(anyString()))
                .thenReturn(buildUserUnableToVoteStatusDTO());

        service.save(buildVoteDTO());
        verify(userFeignClient, times(1))
                .validateLegalId(anyString());
        verify(votingAgendaService, never())
                .findById(anyLong());
        verify(repository, never())
                .existsByAgendaIdAndLegalId(anyLong(), anyString());
        verify(repository, never()).save(any());
    }

    @Test(expected = ApiException.class)
    public void save_withInvalidLegalId_shouldThrowsException() throws ApiException {
        when(userFeignClient.validateLegalId(anyString()))
                .thenThrow(FeignException.NotFound.class);
        when(votingAgendaService.findById(anyLong()))
                .thenReturn(buildVotingAgenda());
        when(repository.existsByAgendaIdAndLegalId(anyLong(), anyString()))
                .thenReturn(true);

        service.save(buildVoteDTO());
        verify(userFeignClient, times(1))
                .validateLegalId(anyString());
        verify(votingAgendaService, times(1))
                .findById(anyLong());
        verify(repository, times(1))
                .existsByAgendaIdAndLegalId(anyLong(), anyString());
        verify(repository, never()).save(any());
    }

    @Test(expected = ApiException.class)
    public void save_withClosedAgenda_shouldRunCorrectly() throws ApiException {
        when(userFeignClient.validateLegalId(anyString()))
                .thenReturn(buildUserAbleToVoteStatusDTO());
        when(votingAgendaService.findById(anyLong()))
                .thenReturn(buildClosedAgenda());
        when(repository.existsByAgendaIdAndLegalId(anyLong(), anyString()))
                .thenReturn(false);

        service.save(buildVoteDTO());
        verify(userFeignClient, times(1))
                .validateLegalId(anyString());
        verify(votingAgendaService, times(1))
                .findById(anyLong());
        verify(repository, times(1))
                .existsByAgendaIdAndLegalId(anyLong(), anyString());
        verify(repository, never()).save(any());
    }

    private VotingAgenda buildVotingAgenda() {
        val agenda = new VotingAgenda();
        agenda.setId(3L);
        agenda.setDescription("description");
        agenda.setStartedAt(LocalDateTime.MIN);
        agenda.setClosedAt(LocalDateTime.MAX);
        return agenda;
    }

    private VotingAgenda buildClosedAgenda() {
        val agenda = new VotingAgenda();
        agenda.setId(3L);
        agenda.setDescription("description");
        agenda.setStartedAt(LocalDateTime.MIN);
        agenda.setClosedAt(LocalDateTime.MIN);
        return agenda;
    }

    private UserVotingStatusDTO buildUserAbleToVoteStatusDTO() {
        val dto = new UserVotingStatusDTO();
        dto.setStatus(UserVotingStatus.ABLE_TO_VOTE);
        return dto;
    }

    private UserVotingStatusDTO buildUserUnableToVoteStatusDTO() {
        val dto = new UserVotingStatusDTO();
        dto.setStatus(UserVotingStatus.UNABLE_TO_VOTE);
        return dto;
    }

    private VoteDTO buildVoteDTO() {
        val vote = new VoteDTO();
        vote.setOption(VoteOption.YES);
        vote.setLegalId("00000000000");
        vote.setVotingAgendaId(1L);
        return vote;
    }
}
