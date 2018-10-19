package springmvc.demo.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

public class TokenAuthenticationService {

    static final long EXPIRATIONTIME = 86400;
    static final String SECRET = "04d59451f0f86b35fa168348ab96e1a66271426a5b61357d7f1bcde7ed671f264cad8659fc5668bc2b3e4638b2ee3c7441fd40e2c05cd694994388724388add35407dc0d8e4f38a220f0e50454136189e3a092fe0e76e7d97be7ad80dd9d38d659f579a46e74797c58cabcfc72481a21afec87a586a056ea26cc71d23109c23d96545df2d6c621476a2356cc6fe49e040ec73c28a368ddf2feae6b4f14083419932a83cc4e9209d620b345a629621def965780f7d697fe313b01f0809b47060a11b568fe63993173c40bb06c60dc84993f1ed5edbfd0172a03219bc3f63cf03d93aaa5bc652c1aa4fbb5c834eb0ac27fe30efdb65d3e0ede01ad89323a594a9a";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String email) {

        String JWT = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.ES512, TextCodec.BASE64.decode(SECRET))
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if(token != null) {
            String user = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(SECRET))
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return user != null? new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()): null;
        }

        return null;
    }
}
