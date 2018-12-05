package springmvc.demo.services.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.repositories.users.UsersRepository;
import springmvc.demo.models.User;

import java.util.*;

@Service
public class CurrentUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = usersRepository.findUserByEmail(s);

        if(user == null) {

            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> listAuth = new LinkedList<>();

        listAuth.add(new SimpleGrantedAuthority("ROLE_CLIENT"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), listAuth);

    }
}
