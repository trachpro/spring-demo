package springmvc.demo.controllers;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.Repositories.UsersRepository;
import springmvc.demo.models.User;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Response;
import springmvc.demo.services.UsersService;

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

    private UsersService usersService = new UsersService(usersRepository, passwordEncoder);


    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    HttpEntity<Resources<User>> getList() {

        Resources<User> resources = new Resources<>(usersRepository.findAll());
        return new ResponseEntity<>(resources, HttpStatus.ACCEPTED);
    }

    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody ResponseEntity<String> createUser(@RequestBody Map<String, String> pet) {

        User user = Commons.getUserFromParams(pet);

        if(user == null) {

            return Response.getErrorMessage("Invalid params", HttpStatus.BAD_REQUEST);
        }

        return usersService.registerNewUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserDetail(@PathVariable String id) {

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            return Response.getErrorMessage("you don't have authorization to do this action", HttpStatus.FORBIDDEN);
        }

        return usersService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody Map<String, String> params) {

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            return Response.getErrorMessage("you don't have authorization to do this action", HttpStatus.FORBIDDEN);
        }

        return usersService.updateUserById(id, params);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        JSONObject resp = new JSONObject();

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            resp.put("message", "You dont have authorize to do this action");
            return Response.getErrorMessage("You don't have authorization to do this action!", HttpStatus.FORBIDDEN);
        }

        return usersService.deleteUserById(id);
    }

//    @GetMapping("/detail")
//    public ResponseEntity<String> getUserWithDetail() {
//
//        JSONObject resp = Converts.convertModelToJson(usersRepository.findUserAttachingReservation(PageRequest.of(0,10)));
//        return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
//    }
}
