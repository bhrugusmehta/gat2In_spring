package com.gat2in.ordersystem.payload;


import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtAuthenticationResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private String id;
    private String tokenType = "Bearer";
}
