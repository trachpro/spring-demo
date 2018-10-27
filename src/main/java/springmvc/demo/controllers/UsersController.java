package springmvc.demo.controllers;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.User;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Converts;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Map;

@RestController
@RequestMapping("api/users")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void setUsersRepository(UsersRepository user) {

        usersRepository = user;
    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    HttpEntity<Resources<User>> getList() {

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

        Resources<User> resources = new Resources<>(usersRepository.findAll());
        return new ResponseEntity<>(resources, HttpStatus.ACCEPTED);
    }

    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody ResponseEntity<String> createUser(@RequestBody Map<String, String> pet) {

        String name = pet.get("name");
        String email = pet.get("email");
        String password = pet.get("password");
//        String role = Commons.isManager()? pet.get("role"): "ROLE_CUSTOMER";


        JSONObject resp = new JSONObject();

//        if(role == null || role == "") {
//            resp.put("message", "role is required!");
//            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
//        }
        if(usersRepository.findUserByEmail(email) == null) {

            try {

                User a = usersRepository.insert(new User(name, passwordEncoder.encode(password), email));
                resp = Converts.convertModelToJson(a);
                resp.remove("password");
                return new ResponseEntity<>(resp.toString(), HttpStatus.ACCEPTED);
            } catch (Exception e) {

                resp.put("message", "internal error while inserting new account to database!");
                return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
            }
        } else {
            resp.put("message", "Email is already existed");
            System.out.println(resp);
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserDetail(@PathVariable String id) {

        System.out.println(id);

        String uId = SecurityContextHolder.getContext().getAuthentication().getName();

        JSONObject resp = new JSONObject();

        if(!Commons.isManager() && !uId.equals(id)) {

            resp.put("message", "You dont have authorize to do this action");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        User user = usersRepository.findUserBy_id(id);

        System.out.println(user == null);
        resp = Converts.convertModelToJson(user);
        return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody Map<String, String> params) {

        JSONObject resp = new JSONObject();

        if(!Commons.isManager() && !Commons.isOwner(id)) {
            resp.put("message", "You dont have authorize to do this action");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        User user = usersRepository.findUserBy_id(id);

        if(user == null) {
            resp.put("message", "user not found!");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        for(Map.Entry<String, String> entry: params.entrySet()) {

            switch (entry.getKey()) {

                case "name":
                    user.setName(entry.getValue());
                    break;

                case "password":
                    user.setPassword(passwordEncoder.encode(entry.getValue()));
                    break;

//                case "role":
//                    if(!Commons.isManager()) {
//                        resp.put("message", "you are not allowed to change your role!");
//                        return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
//                    }
//                    if(!Commons.isValidRole(entry.getValue())) {
//                        resp.put("message", "Invalid ROLE Format!");
//                        return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
//                    }
//                    user.setRole(entry.getValue());
//                    break;
//
//                    default: break;
            }
        }

        usersRepository.save(user);
        return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        JSONObject resp = new JSONObject();

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            resp.put("message", "You dont have authorize to do this action");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        try {

            Long x = usersRepository.deleteUserBy_id(id);

            resp.put("code", x);

            return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
        } catch (Exception e) {

            resp.put("message", "Internal error! please try again!");
            resp.put("error", e);
            return new ResponseEntity<>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<String> getUserWithDetail() {

        JSONObject resp = Converts.convertModelToJson(usersRepository.findUserAttachingReservation(PageRequest.of(0,10)));
        return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
    }
}
