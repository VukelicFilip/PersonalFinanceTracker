package com.example.personalfinancetracker.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${inside.app.jwtSecret}")
	private String jwtSecret;

	@Value("${inside.app.jwtExpirationMs}")
	private long jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {

		com.multi.tenant.security.UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setHeaderParam("typ","JWT")
		        .setSubject((userPrincipal.getUsername()))
		        .claim("role", userPrincipal.getAuthorities().toString().substring(1, userPrincipal.getAuthorities().toString().length() - 1))
                .claim("tenantIdentifier", userPrincipal.getTenantIdentifier())
		        .setIssuedAt(new Date())
		        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
		        .signWith(SignatureAlgorithm.HS512, jwtSecret)
		        .compact();
		  
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
    
    public String getTenantIdentifierFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return (String) claims.get("tenantIdentifier");
        } catch (JwtException e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
