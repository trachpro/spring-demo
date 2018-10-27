package springmvc.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.Repositories.RoomsRepository;
import springmvc.demo.models.Room;
import springmvc.demo.utils.Converts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rooms")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RoomsController {

    @Autowired
    private RoomsRepository roomsRepository;

    public void setRoomsRepository(RoomsRepository roomsRepository) {

        this.roomsRepository = roomsRepository;
    }

    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody
    ResponseEntity<String> createRoom(@RequestBody Map<String, String> pet) {

        Room room = new Room();

        room.setCapacity(3);
        room.setPrice(100000000);
        room.setStatus("RESERVING");
        room.setType("LUXURY");

        Map<String, Integer> jsonObject = new HashMap<>();

        jsonObject.put("double", 1);
        jsonObject.put("single", 2);
        room.setBeds(jsonObject);

        try {

            Room a = roomsRepository.insert(room);
            return new ResponseEntity<>(Converts.convertModelToJson(a).toString(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {

            return new ResponseEntity<>("internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<Room> getRoomList() {

        return roomsRepository.findAll();
    }
}