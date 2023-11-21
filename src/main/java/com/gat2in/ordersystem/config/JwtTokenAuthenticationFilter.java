package com.gat2in.ordersystem.config;


import com.gat2in.ordersystem.model.Gat2InUserDetails;
import com.gat2in.ordersystem.service.JwtTokenProvider;
import com.gat2in.ordersystem.service.UserService;
import io.jsonwebtoken.Claims;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final String serviceUsername;

    JwtTokenAuthenticationFilter(
            String serviceUsername,
            JwtConfig jwtConfig,
            JwtTokenProvider tokenProvider,
            UserService userService) {

        this.serviceUsername = serviceUsername;
        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(jwtConfig.getHeader());

        if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfig.getPrefix(), "");

        if(tokenProvider.validateToken(token)) {

            Claims tokenClaims = tokenProvider.getClaimsFromToken(token);
            String username = tokenClaims.getSubject();
            UsernamePasswordAuthenticationToken auth;

            if(username.equals(serviceUsername)) {

                List<Object> authorities = new ArrayList<>();
                authorities.add(tokenClaims.get("authorities"));

                auth = new UsernamePasswordAuthenticationToken(username, null,
                            authorities.stream().map(sga -> new SimpleGrantedAuthority("authorities")).collect(toList()));

            } else {
                auth = userService
                    .findByUsername(username)
                    .map(Gat2InUserDetails::new)
                    .map(userDetails -> {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                      userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        return authentication;
                    })
                    .orElse(null);
            }

            SecurityContextHolder.getContext().setAuthentication(auth);

        } else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}
