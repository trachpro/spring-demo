package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.rooms.RoomsRepository;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Room;
import springmvc.demo.models.Rooms;
import springmvc.demo.utils.Converts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RestController
public class RoomService {

    @Autowired
    private static RoomsRepository roomsRepository;

    public RoomService(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    public static ResponseModel findAvailableRoom(int capacity, String sFrom, String sTo) {


        try {
            Date from = Converts.convertStringToDate(sFrom);
            Date to = Converts.convertStringToDate(sTo);

            if(capacity <= 0 || to.before(from)) {
                return new ResponseModel(JSONObject.NULL,"Invalid input", HttpStatus.BAD_REQUEST);
            }

            List<Room> rooms = roomsRepository.findRoomsByCapacity(capacity);

            List<Reservation> reservations = ReservationService.findReservationBetweenDate(from, to);
            System.out.println("Size = " + reservations.size());
            for (Room r: rooms
                 ) {
                System.out.println( "Room Number: " + r.getRoomNo());
            }

            for (Reservation re: reservations
                 ) {
                System.out.println("Reservation : "+re.getRoomNo());
            }


            return new ResponseModel(new Rooms(rooms), "Success", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(null,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
