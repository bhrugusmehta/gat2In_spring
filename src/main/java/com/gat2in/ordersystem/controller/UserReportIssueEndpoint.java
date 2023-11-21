package com.gat2in.ordersystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gat2in.ordersystem.model.ReportIssue;
import com.gat2in.ordersystem.payload.ApiResponse;
import com.gat2in.ordersystem.payload.UserReportIssueRequest;
import com.gat2in.ordersystem.service.UserReportIssueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.security.SecureRandom;
import java.util.*;

@RestController
@Slf4j

public class UserReportIssueEndpoint {

    private final UserReportIssueService userReportIssueService;

    public UserReportIssueEndpoint(UserReportIssueService userReportIssueService) {
        this.userReportIssueService = userReportIssueService;
    }

    @PostMapping(value = "/issue", produces = {MediaType.APPLICATION_JSON_VALUE })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createReportIssue( @Valid @RequestBody UserReportIssueRequest payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String prefix = "TKT_" + username + "_";
        SecureRandom rand = new SecureRandom();

        String ticketNumber = prefix + Math.abs(rand.nextLong());

        ReportIssue reportIssueBuilder = ReportIssue
                .builder()
                .username(username)
                .issueTopic(payload.getIssueTopic())
                .issueDescription(payload.getIssueDescription())
                .dateTime(new Date().toString())
                .ticketNumber(ticketNumber)
                .build();

        ReportIssue savedIssue = userReportIssueService.createUserReportIsue(reportIssueBuilder);

        return ResponseEntity.ok().body(new ApiResponse(true,
                "Ticket created successfully, ticket number is " + ticketNumber));
    }

    
}
