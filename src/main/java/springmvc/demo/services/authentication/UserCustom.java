package springmvc.demo.services.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserCustom extends UsernamePasswordAuthenticationToken {

    private String email;

    private String uName;

    public UserCustom(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserCustom(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String email, String name) {
        super(principal, credentials, authorities);

        this.email = email;
        this.uName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
