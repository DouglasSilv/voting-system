package com.sicredi.votingsystem.controller;

import com.sicredi.votingsystem.dto.VotingAgendaResumeDTO;
import com.sicredi.votingsystem.dto.VotingAgendaSaveDTO;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.service.VotingAgendaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/voting-agendas")
public class VotingAgendaController {

    private final VotingAgendaService service;

    public VotingAgendaController(VotingAgendaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody VotingAgendaSaveDTO dto) {
        return service.save(dto);
    }

    @PatchMapping("/{id}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void start(@PathVariable Long id, @RequestParam(required = false) Long seconds) throws ApiException {
        service.start(id, seconds);
    }

    @GetMapping("/{id}/resume")
    @ResponseStatus(HttpStatus.OK)
    public VotingAgendaResumeDTO findResumeById(@PathVariable Long id) throws ApiException {
        return service.findResumeById(id);
    }

}
