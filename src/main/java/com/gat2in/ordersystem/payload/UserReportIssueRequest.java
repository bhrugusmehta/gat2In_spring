
package com.gat2in.ordersystem.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserReportIssueRequest {
    
    @NotBlank
    @Size(min = 4, max = 15)
    private String username;

    @NotBlank
    @Size(min = 8, max = 15)
    private String issueTopic;

    @NotBlank
    @Size(min = 20, max = 5000)
    private String issueDescription;

    private String dateTime;
}
