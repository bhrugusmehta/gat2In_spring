
package com.gat2in.ordersystem.model;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private Long tokenid;

    @OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false, insertable = false, updatable = false)
    UserEntity user;

    @NotBlank
    private String confirmationToken;

    @CreatedDate
    private Instant createdDate;

    public ConfirmationToken() {
        confirmationToken = UUID.randomUUID().toString();
    }
    
    public ConfirmationToken(UserEntity user) {
        this.user = user;
        confirmationToken = UUID.randomUUID().toString();
    }

}