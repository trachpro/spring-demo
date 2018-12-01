package springmvc.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.users.UsersRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.User;
import springmvc.demo.utils.Commons;

import java.util.Map;

@Service
@RestController
public class UsersService {

    private static UsersRepository usersRepository;

    private static PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repo, PasswordEncoder pwdE) {

        usersRepository = repo;
        passwordEncoder = pwdE;
    }

    public static ResponseModel getAllUsers() {

        try {
            return new ResponseModel(usersRepository.findAll(),"successfull", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null, "internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel registerNewUser(User user) {

        if(usersRepository.findUserByEmail(user.getEmail()) == null) {

            try {

                usersRepository.insert(user);

                return new ResponseModel(user, "register successfully!", HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseModel(null, "internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return new ResponseModel(null,"User is already existed!", HttpStatus.CONFLICT);
        }
    }

    public static ResponseModel getUserById(String id) {

        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return new ResponseModel(null,"User not found!", HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseModel(user, "success", HttpStatus.OK);
        }
    }

    public static ResponseModel changePassword(MultiValueMap<String, String> params) {

        System.out.println(usersRepository == null);
        User user = usersRepository.findUserByEmail(Commons.getEmail());

        if(user == null) {

            return new ResponseModel(null,"User not found!", HttpStatus.NOT_FOUND);
        }

        String oldPassword = params.getFirst("oldPassword");
        String newPassword = params.getFirst("newPassword");

        if(oldPassword!= null &&
                BCrypt.checkpw(oldPassword, user.getPassword()) &&
                Commons.isValidPassword(newPassword)
                )
        {
                user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            return new ResponseModel(null,"invalid old password not matching or invalid new password!", HttpStatus.BAD_REQUEST);
        }

        usersRepository.save(user);
        return new ResponseModel(null, "update successful!", HttpStatus.OK);
    }

    public static ResponseModel deleteUserById(String id) {

        try {

            Long x = usersRepository.deleteUserBy_id(id);

            return new ResponseModel(x,"delete successful", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
