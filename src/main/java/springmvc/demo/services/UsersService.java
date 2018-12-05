package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.repositories.users.UsersRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.User;
import springmvc.demo.utils.Message;

import java.util.Map;

@Service
@RestController
public class UsersService {

    private static UsersRepository usersRepository;

    private static PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repo, PasswordEncoder encoder) {

        usersRepository = repo;
        passwordEncoder = encoder;
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

    public static ResponseModel updateUserById(String id, Map<String, String> params) {

        System.out.println(usersRepository == null);
        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return new ResponseModel(JSONObject.NULL,"User not found!", HttpStatus.NOT_FOUND);
        }

        for(Map.Entry<String, String> entry: params.entrySet()) {

            switch (entry.getKey()) {

                case "name":
                    user.setName(entry.getValue());
                    break;

                case "password":
                    user.setPassword(passwordEncoder.encode(entry.getValue()));
                    break;
            }
        }

        usersRepository.save(user);
        return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
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
