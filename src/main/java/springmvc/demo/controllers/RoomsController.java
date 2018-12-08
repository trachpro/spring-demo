package springmvc.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.repositories.rooms.RoomsRepository;
import springmvc.demo.models.Room;
import springmvc.demo.services.RoomService;
import springmvc.demo.utils.Converts;
import springmvc.demo.utils.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
        value = "api/rooms",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RoomsController {

    @Autowired
    private RoomsRepository roomsRepository;

    public void setRoomsRepository(RoomsRepository roomsRepository) {

        this.roomsRepository = roomsRepository;
    }

    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody
    ResponseEntity<String> createRoom(@RequestBody MultiValueMap<String, String> pet) {

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

            return new ResponseEntity<>(Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<Room> getRoomList() {

        return roomsRepository.findAll();
    }

    @GetMapping({"/{roomNo}"})
    public Room getRoomByRoomNo(@PathVariable int roomNo) {
        //deploy
        return RoomService.findRoomByRoomNo(roomNo);
    }

    @GetMapping({"/{capacity}/{from}/{to}"})
    public ResponseEntity<String> checkAvailableRooms(@PathVariable String capacity, @PathVariable String from, @PathVariable String to) {
        int nCapacity = Integer.parseInt(capacity);
        
        return RoomService.findAvailableRoom(nCapacity, from, to).toResponse();
    }

}
