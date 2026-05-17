package com.mariajulia.auth_service.security;

import com.mariajulia.auth_service.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // transforma a palavra secreta em uma chave de assinatura adequada para o algoritmo HMAC-SHA
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Gera um token JWT contendo o ID do usuário, email e papel, com data de expiração baseada na configuração
    public String generateToken(UUID userId, String email, String role) {
        long now = System.currentTimeMillis();
        long expiryTime = now + jwtProperties.getExpiration();

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(expiryTime))
                .signWith(getSigningKey())
                .compact();
    }

    // Valida o token JWT verificando sua assinatura e estrutura, retornando true se for válido ou false caso contrário
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extrai o ID do usuário do token JWT, convertendo o valor do campo "sub" (subject) para um UUID
    public UUID getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    // Extrai o email do usuário do token JWT, acessando o campo "email" presente nas reivindicações (claims)
    public String getUserEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    // Extrai o papel do usuário do token JWT, acessando o campo "role" presente nas reivindicações (claims)
    public String getUserRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    // Verifica se o token JWT está expirado, comparando a data de expiração com a data atual
    public boolean isTokenExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date());
    }

    // Mtodo auxiliar para obter as reivindicações (claims) do token JWT, lançando uma exceção se o token for inválido
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
