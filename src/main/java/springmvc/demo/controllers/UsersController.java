package springmvc.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.User;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
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

//    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
//    public Resources<User> getPetList() {
//        System.out.println("get all pets...");
//        List<User> userList = usersRepository.findAll();
//
//        System.out.println(userList);
//
//        for(User p: userList) {
//            p.removeLinks();
//            Link selfLink = linkTo(UsersController.class).slash(p.getName()).withSelfRel();
//            p.add(selfLink);
//        }
//
//        Link link = linkTo(UsersController.class).withSelfRel();
//        Resources<User> result = new Resources<>(userList, link);
//        return result;
//    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    HttpEntity<Resources<User>> getList() {

        Resources<User> resources = new Resources<>(usersRepository.findAll());
//        resources.add(this.enti);
        return new ResponseEntity<>(resources, HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public Boolean createPet(@RequestBody Map<String, String> pet) {

        String name = pet.get("name");
        String email = pet.get("email");
        String password = pet.get("password");
        String role = pet.get("role");
        String address = pet.get("address");


        if(usersRepository.findUserByEmail(email) == null) {

            try {

                usersRepository.insert(new User(name, passwordEncoder.encode(password), email, role, address));
                return true;
            } catch (Exception e) {

                System.err.println(e);
                return false;
            }
        } else {
            return false;
        }
    }
}
