package com.gat2in.ordersystem.service;


import com.gat2in.ordersystem.model.ReportIssue;
import com.gat2in.ordersystem.repository.UserReportIssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserReportIssueService {
  
    private final UserReportIssueRepository userReportIssueRepository;
    
    public UserReportIssueService(UserReportIssueRepository activityRepository) {
        this.userReportIssueRepository = activityRepository;
    }

    public ReportIssue createUserReportIsue(ReportIssue reportIssue) {
        return userReportIssueRepository.save(reportIssue);
    }


}
