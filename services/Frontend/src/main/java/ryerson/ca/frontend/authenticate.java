package ryerson.ca.frontend;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map.Entry;
import javax.crypto.SecretKey;

public class authenticate {

    private final SignatureAlgorithm signatureAlgorithm;
    private final SecretKey secretKey;

    public authenticate() {
        signatureAlgorithm = SignatureAlgorithm.HS256;
        secretKey = Keys.secretKeyFor(signatureAlgorithm); // Generates a secure 256-bit key
    }

    public String createJWT(String issuer, String subject, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Building the JWT
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(secretKey); // Uses the secure key

        // Set expiration if specified
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        String jwt = builder.compact();
        return jwt;
    }

    public Entry<Boolean, String> verify(String jwt) throws UnsupportedEncodingException {
        Jws<Claims> jws = null;
        String username = "";
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Uses the correct key
                    .build()
                    .parseClaimsJws(jwt);

            System.out.println("JWT is valid.");
            username = jws.getBody().getSubject();
            System.out.println(username);
        } catch (JwtException ex) {
            System.out.println("Invalid JWT.");
        }

        if (jws == null) {
            return new AbstractMap.SimpleEntry<>(false, "");
        }

        // Check expiration
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (jws.getBody().getExpiration().before(now)) {
            return new AbstractMap.SimpleEntry<>(false, "");
        }

        return new AbstractMap.SimpleEntry<>(true, username);
    }
}
