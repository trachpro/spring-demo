package springmvc.demo.services.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import springmvc.demo.Repositories.staffRepository.StaffsRepository;
import springmvc.demo.Repositories.users.UsersRepository;
import springmvc.demo.configs.Constants;
import springmvc.demo.models.Person;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class TokenAuthenticationService {

    private static StaffsRepository staffsRepository;

    private static UsersRepository usersRepository;

    public TokenAuthenticationService(StaffsRepository staffsRepository, UsersRepository usersRepository) {

        this.staffsRepository = staffsRepository;
        this.usersRepository = usersRepository;
    }

    public static void addAuthentication(HttpServletResponse res, Authentication auth) throws ServletException, Exception {

        String role = auth.getAuthorities().toArray()[0].toString();

        Person person;

        try{
            if(role.equals("ROLE_CLIENT")) {

                person = usersRepository.findUserByEmail(auth.getName());
            } else {

                person = staffsRepository.findStaffByEmail(auth.getName());
            }
        } catch (Exception e){

            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject response = new JSONObject();

            response.put("message", "Internal error");

            res.getOutputStream().println(response.toString());
            return;
        }



        JSONObject resp = new JSONObject();

        resp.put("accessToken", generateJWT(person, role));
        resp.put("id", person.get_id());
        resp.put("email", person.getEmail());
        resp.put("role", role);
        resp.put("name", person.getName());
        res.getWriter().write(resp.toString());
//        res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + " " + JWT);
    }

    public static String generateJWT(Person person, String role) {

        Map<String, Object> hashmap = new HashMap<>();

        hashmap.put("role", role);
        hashmap.put("email", person.getEmail());
        hashmap.put("id", person.get_id());
        hashmap.put("name", person.getName());

        return Jwts.builder()
                .setSubject(person.getEmail())
                .setId(person.get_id())
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
                return user != null? new UserCustom(user.get("id"), null, listAuth,  user.get("email").toString(), user.get("name").toString() ): null;
            } catch (Exception e) {

                request.setAttribute("expired", e.getMessage());
            }
        }

        return null;
    }
}
