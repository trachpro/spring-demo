package springmvc.demo.controllers;

import org.json.JSONObject;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.models.Staff;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Response;
import springmvc.demo.services.StaffsService;

import java.util.Map;

@RestController
@RequestMapping("api/staffs")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class StaffsController {

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    ResponseEntity<String> getListStaffs() {

        System.out.println(Commons.getName());

        return StaffsService.getAllStaffs().toResponse();
    }

    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody ResponseEntity<String> createStaff(@RequestBody Map<String, String> pet) {

        Staff user = Commons.getStaffFromParams(pet);

        if(user == null) {
            return Response.getErrorMessage("Invalid params", HttpStatus.BAD_REQUEST);
        }

        return StaffsService.registerNewStaff(user).toResponse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserDetail(@PathVariable String id) {

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            return Response.getErrorMessage("you don't have authorization to do this action", HttpStatus.FORBIDDEN);
        }

        return StaffsService.getStaffById(id).toResponse();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody Map<String, String> params) {

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            return Response.getErrorMessage("you don't have authorization to do this action", HttpStatus.FORBIDDEN);
        }

        return StaffsService.updateStaffById(id, params).toResponse();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        JSONObject resp = new JSONObject();

        if(!Commons.isManager() && !Commons.isOwner(id)) {

            resp.put("message", "You dont have authorize to do this action");
            return Response.getErrorMessage("You don't have authorization to do this action!", HttpStatus.FORBIDDEN);
        }

        return StaffsService.deleteStaffById(id).toResponse();
    }
}
