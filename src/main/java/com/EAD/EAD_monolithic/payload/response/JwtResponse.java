package com.EAD.EAD_monolithic.payload.response;

import java.util.List;

/* a class that represent the JWT response payload for a JWT authentication request*/
public class JwtResponse {
    private String token; // JWT token
    private String type = "Bearer";    // Token type. here set to 'Bearer'
    private Long id;
    private String username;
    private String email;
    private List<String> roles;


    //a constructor take all these fields as argument and initialize a member variable
    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }


    //getters and setters to access and modify

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}

