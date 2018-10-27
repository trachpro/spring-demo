package springmvc.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.Repositories.StaffsRepository;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.Staff;
import springmvc.demo.models.User;

import java.util.*;

@Service
public class CurrentUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private StaffsRepository staffsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        String[] email = s.split(" ");

        if(email.length == 1) {

            User user = usersRepository.findUserByEmail(s);

            if(user == null) {

                throw new UsernameNotFoundException("User not found");
            }

            List<GrantedAuthority> listAuth = new LinkedList<>();

            listAuth.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            listAuth.add(new SimpleGrantedAuthority(user.get_id()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), listAuth);
        } else {

            Staff staff = staffsRepository.getStaffModelByEmail(email[0]);

            if(staff == null) {

                throw new UsernameNotFoundException("user not found");
            }

            List<GrantedAuthority> listAuth = new LinkedList<>();

            listAuth.add(new SimpleGrantedAuthority(staff.getRole()));
            listAuth.add(new SimpleGrantedAuthority(staff.get_id()));

            return new org.springframework.security.core.userdetails.User(staff.getEmail(), staff.getPassword(), listAuth);
        }
    }
}
