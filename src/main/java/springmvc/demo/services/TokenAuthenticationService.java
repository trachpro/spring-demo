package springmvc.demo.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import springmvc.demo.configs.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class TokenAuthenticationService {

    public static void addAuthentication(HttpServletResponse res, Authentication auth) throws Exception, ServletException {

        String id = auth.getAuthorities().toArray()[0].toString();
        String email = auth.getName();
        String role = auth.getAuthorities().toArray()[1].toString();

        String JWT = Jwts.builder()
                .setSubject(email)
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET)
                .compact();

        JSONObject resp = new JSONObject();

        resp.put("accessToken", JWT);
        resp.put("id", id);
        resp.put("email", email);
        resp.put("role", role);
        res.getWriter().write(resp.toString());
        res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + " " + JWT);

    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Constants.HEADER_STRING);
        if(token != null) {
            Claims user = Jwts.parser()
                    .setSigningKey(Constants.SECRET)
                    .parseClaimsJws(token.replace(Constants.TOKEN_PREFIX, ""))
                    .getBody();

            List<GrantedAuthority> listAuth = new LinkedList<>();
            listAuth.add(new SimpleGrantedAuthority(user.getId()));
            return user != null? new UsernamePasswordAuthenticationToken(user.getId(), null, listAuth ): null;
        }

        return null;
    }
}
