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
import springmvc.demo.models.StaffModel;

import java.util.LinkedList;
import java.util.List;

@Service
public class StaffUserDetailService implements UserDetailsService {

    @Autowired
    private StaffsRepository staffsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        String[] email = s.split(" ");

        System.out.println("staff running");

        StaffModel staff = staffsRepository.getStaffModelByEmail(s);

        if(staff == null) {

            throw new UsernameNotFoundException("user not found");
        }

        List<GrantedAuthority> listAuth = new LinkedList<>();

        listAuth.add(new SimpleGrantedAuthority(staff.getRole()));
        listAuth.add(new SimpleGrantedAuthority(staff.get_id()));

        return new org.springframework.security.core.userdetails.User(staff.getEmail(), staff.getPassword(), listAuth);
    }
}
