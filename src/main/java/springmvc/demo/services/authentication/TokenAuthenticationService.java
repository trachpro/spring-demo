package springmvc.demo.services.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import springmvc.demo.configs.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class TokenAuthenticationService {

    public static void addAuthentication(HttpServletResponse res, Authentication auth) throws ServletException, Exception {

        String id = auth.getAuthorities().toArray()[0].toString();
        String email = auth.getName();
        String role = auth.getAuthorities().toArray()[1].toString();

        JSONObject resp = new JSONObject();

        resp.put("accessToken", generateJWT(auth));
        resp.put("id", id);
        resp.put("email", email);
        resp.put("role", role);
        res.getWriter().write(resp.toString());
//        res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + " " + JWT);
    }

    public static String generateJWT(Authentication auth) {

        String id = auth.getAuthorities().toArray()[0].toString();
        System.out.println(auth.getAuthorities().toArray()[1]);
        String email = auth.getName();

        Map<String, Object> hashmap = new HashMap<>();

        hashmap.put("role", auth.getAuthorities().toArray()[1].toString());
        hashmap.put("email", email);
        hashmap.put("id", id);

        return Jwts.builder()
                .setSubject(email)
                .setId(id)
                .setClaims(hashmap)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET)
                .compact();
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Constants.HEADER_STRING);
        if(token != null) {
            try {
                Claims user = Jwts.parser()
                        .setSigningKey(Constants.SECRET)
                        .parseClaimsJws(token.replace(Constants.TOKEN_PREFIX, ""))
                        .getBody();

                List<GrantedAuthority> listAuth = new LinkedList<>();
                listAuth.add(new SimpleGrantedAuthority(user.get("role").toString()));
                return user != null? new UserCustom(user.getId(), null, listAuth,  user.get("email").toString() ): null;
            } catch (Exception e) {

                request.setAttribute("expired", e.getMessage());
            }
        }

        return null;
    }
}
