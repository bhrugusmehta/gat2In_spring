package com.gat2in.ordersystem.repository;

import com.gat2in.ordersystem.model.ReportIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReportIssueRepository extends JpaRepository<ReportIssue, Long> {
}
