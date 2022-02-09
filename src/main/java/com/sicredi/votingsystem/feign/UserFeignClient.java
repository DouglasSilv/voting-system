package com.sicredi.votingsystem.feign;

import com.sicredi.votingsystem.dto.UserVotingStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-client", url = "https://user-info.herokuapp.com")
public interface UserFeignClient {

    @GetMapping("/users/{legalId}")
    UserVotingStatusDTO validateLegalId(@PathVariable String legalId);

}