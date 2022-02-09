package com.sicredi.votingsystem.controller;

import com.sicredi.votingsystem.dto.VoteDTO;
import com.sicredi.votingsystem.exception.ApiException;
import com.sicredi.votingsystem.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/votes")
public class VoteController {

    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody VoteDTO vote) throws ApiException {
        service.save(vote);
    }
}
