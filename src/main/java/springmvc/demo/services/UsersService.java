package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.repositories.users.UsersRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.User;
import springmvc.demo.utils.Message;
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
            return new ResponseModel(usersRepository.findAll(), Message.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null, Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel registerNewUser(User user) {

        if(usersRepository.findUserByEmail(user.getEmail()) == null) {

            try {

                usersRepository.insert(user);

                return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseModel(JSONObject.NULL, Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return new ResponseModel(JSONObject.NULL,"User is already existed!", HttpStatus.CONFLICT);
        }
    }

    public static ResponseModel getUserById(String id) {

        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return new ResponseModel(JSONObject.NULL,"User not found!", HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
        }
    }

    public static ResponseModel changePassword(MultiValueMap<String, String> params) {

        System.out.println(usersRepository == null);
        User user = usersRepository.findUserByEmail(Commons.getEmail());

        if(user == null) {

            return new ResponseModel(JSONObject.NULL,"User not found!", HttpStatus.NOT_FOUND);
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

        return new ResponseModel(JSONObject.NULL, Message.SUCCESS, HttpStatus.OK);

    }

    public static ResponseModel deleteUserById(String id) {

        try {

            Long x = usersRepository.deleteUserBy_id(id);

            return new ResponseModel(x,Message.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
