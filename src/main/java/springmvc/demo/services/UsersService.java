package springmvc.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.User;
import springmvc.demo.utils.Response;

import java.util.Map;

@Service
@RestController
public class UsersService {

    @Autowired
    private static UsersRepository usersRepository;

    @Autowired
    private static PasswordEncoder passwordEncoder;

    public static void setUsersRepository(UsersRepository usersRepository) {
        UsersService.usersRepository = usersRepository;
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        UsersService.passwordEncoder = passwordEncoder;
    }

    public static ResponseEntity<String> registerNewUser(User user) {

        if(usersRepository.findUserByEmail(user.getEmail()) == null) {

            try {

                usersRepository.insert(user);

                return Response.getSuccessMessage(user, "register successfully!", HttpStatus.OK);
            } catch (Exception e) {

                return Response.getErrorMessage("internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return Response.getErrorMessage("User is already existed!", HttpStatus.CONFLICT);
        }
    }

    public static ResponseEntity<String> getUserById(String id) {

        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return Response.getErrorMessage("User not found!", HttpStatus.BAD_REQUEST);
        } else {

            return Response.getSuccessMessage(user, "success", HttpStatus.OK);
        }
    }

    public static ResponseEntity<String> updateUserById(String id, Map<String, String> params) {

        System.out.println(usersRepository == null);
        User user = usersRepository.findUserBy_id(id);

        if(user == null) {

            return Response.getErrorMessage("User not found!", HttpStatus.NOT_FOUND);
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
        return Response.getSuccessMessage(user, "update successful!", HttpStatus.OK);
    }

    public static ResponseEntity<String> deleteUserById(String id) {

        try {

            Long x = usersRepository.deleteUserBy_id(id);

            return Response.getSuccessMessage(x,"delete successful", HttpStatus.OK);
        } catch (Exception e) {

            return Response.getErrorMessage("Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
