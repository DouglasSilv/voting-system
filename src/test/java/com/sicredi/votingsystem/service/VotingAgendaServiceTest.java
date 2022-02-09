package com.sicredi.votingsystem.service;

import com.sicredi.votingsystem.dto.VotingAgendaSaveDTO;
import com.sicredi.votingsystem.entity.VotingAgenda;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.jpa.projections.VotingAgendaResume;
import com.sicredi.votingsystem.repository.VotingAgendaRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VotingAgendaServiceTest {

    private VotingAgendaRepository repository;

    private VotingAgendaService service;

    @Before
    public void init() {
        repository = mock(VotingAgendaRepository.class);

        service = new VotingAgendaService(repository);
    }

    @Test
    public void save_shouldRunCorrectly() {
        when(repository.save(any(VotingAgenda.class)))
                .thenReturn(buildSavedAgenda());

        val id = service.save(buildVotingAgendaDTO());
        assertEquals(new Long(1), id);
        verify(repository, times(1)).save(argThat(agenda -> {
            assertEquals("description", agenda.getDescription());
            return true;
        }));
    }

    @Test
    public void start_shouldRunCorrectly() throws ApiException {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(buildSavedAgenda()));

        service.start(1L, 120L);
        verify(repository, times(1)).save(argThat(agenda -> {
            assertNotNull(agenda.getClosedAt());
            assertNotNull(agenda.getStartedAt());
            assertEquals(120L, ChronoUnit.SECONDS.between(agenda.getStartedAt(), agenda.getClosedAt()));
            return true;
        }));
    }

    @Test(expected = ApiException.class)
    public void start_withClosedAgenda_shouldThrowsException() throws ApiException {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(buildClosedAgenda()));

        service.start(1L, 120L);
    }

    @Test
    public void findResumeById_shouldRunCorrectly() throws ApiException {
        when(repository.findResumeById(anyLong())).thenReturn(Optional.of(mock(VotingAgendaResume.class)));

        val resume = service.findResumeById(1L);
        assertNotNull(resume);
        verify(repository, times(1)).findResumeById(anyLong());
    }


    private VotingAgenda buildSavedAgenda() {
        val agenda = new VotingAgenda();
        agenda.setDescription("description");
        agenda.setId(1L);
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

    private VotingAgendaSaveDTO buildVotingAgendaDTO() {
        val agenda = new VotingAgendaSaveDTO();
        agenda.setDescription("description");
        return agenda;
    }
}
