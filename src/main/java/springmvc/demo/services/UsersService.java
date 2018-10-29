package springmvc.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.User;

import java.util.Map;

@Service
@RestController
public class UsersService {

    @Autowired
    private static UsersRepository usersRepository;

    @Autowired
    private static PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repo, PasswordEncoder encoder) {

        usersRepository = repo;
        passwordEncoder = encoder;
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

    public static ResponseModel updateUserById(String id, Map<String, String> params) {

        System.out.println(usersRepository == null);
        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return new ResponseModel(null,"User not found!", HttpStatus.NOT_FOUND);
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
        return new ResponseModel(user, "update successful!", HttpStatus.OK);
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
