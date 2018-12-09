package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.repositories.rooms.RoomsRepository;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Room;
import springmvc.demo.models.Rooms;
import springmvc.demo.utils.Converts;
import springmvc.demo.utils.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@RestController
public class RoomService {

    @Autowired
    private static RoomsRepository roomsRepository;

    public RoomService(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }


    /**
     *  find available room to be reserved between 2 days, and capacity
     *  @param capacity number of stayers
     *  @param sFrom check in day;
     *  @param sTo check out day;
     *  @return ResponseModel
     */
    public static ResponseModel findAvailableRoom(int capacity, String sFrom, String sTo) {


        try {
            Date from = Converts.convertStringToDate(sFrom);  // convert input string to date
            Date to = Converts.convertStringToDate(sTo);

            if(capacity <= 0 || to.before(from)) {
                return new ResponseModel(JSONObject.NULL, Message.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }

            // list of rooms matching by number of stayers
            List<Room> rooms = roomsRepository.findRoomsByCapacity(capacity);

            // list of reservations between 2 days
            List<Reservation> reservations = ReservationService.findReservationBetweenDate(from, to);

            List<Integer> reservedRooms = new ArrayList<Integer>();



            for (Reservation r: reservations
                 ) {
                if(!r.getStatus().equals("CANCELLED"))  // ignore cancelled reservations
                    reservedRooms.add(r.getRoomNo());
            }

            // filter available room to be reserved
            Iterator<Room> it = rooms.iterator();
            while (it.hasNext()) {
                Room r = it.next();
                if(reservedRooms.contains(r.getRoomNo())) {
                    it.remove();
                }
            }

            return new ResponseModel(new Rooms(rooms), Message.SUCCESS, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public static Room findRoomByRoomNo(int roomNo) {
        return roomsRepository.findRoomsByRoomNo(roomNo);
    }

    public static Boolean checkRoomAvailibilityBetweenDate(int roomNo, Date from, Date to) {
        List<Reservation> reservations = ReservationService.findReservationBetweenDate(from, to);

        for (Reservation r: reservations
                ) {
            if(r.getRoomNo() == roomNo && !r.getStatus().equals("CANCELLED") && !r.getStatus().equals("FINISHED")) {
                return false;
            }
        }

        return true;
    }
}
