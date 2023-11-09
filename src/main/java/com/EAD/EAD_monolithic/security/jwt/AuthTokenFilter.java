package com.EAD.EAD_monolithic.security.jwt;

import com.EAD.EAD_monolithic.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Parse JWT token from the request header
            String jwt = parseJwt(request);

            // Check if the JWT token is not null and valid
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // Get the username from the JWT token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Load user details from the userDetailsService using the username obtained from the JWT token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication object and set it to the SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {

            // Log error if there was any exception while setting user authentication
            logger.error("Cannot set user authentication: {}", e);
        }

        // Call the next filter in the chain
        filterChain.doFilter(request, response);
    }


    /*
   This method parses the JWT token from the request header.
   @param request - HttpServletRequest object
   @return the JWT token if found in the request header, otherwise null
  */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

            // Extract the JWT token from the request header
            return headerAuth.substring(7, headerAuth.length());
        }


        // Return null if JWT token not found in the request header
        return null;
    }
}

