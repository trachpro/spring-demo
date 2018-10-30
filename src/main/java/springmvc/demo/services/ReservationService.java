package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.reservations.ReservationsRepository;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Room;
import springmvc.demo.utils.Commons;

import java.util.Date;
import java.util.List;

@Service
@RestController
public class ReservationService {

    @Autowired
    private static ReservationsRepository reservationsRepository;

    public ReservationService(ReservationsRepository reservationsRepository) {
        this.reservationsRepository = reservationsRepository;
    }

    public static List<Reservation> findReservationBetweenDate(Date from, Date to) {
        return reservationsRepository.findReservationsBetweenDate(from, to);
    }

    /**
     * Create new reservation
     * @param reservation
     * @return ResponseModel
     */
    public static ResponseModel addNewReservation(Reservation reservation) {

        try {
            Date from = reservation.getBookingFrom();
            Date to = reservation.getBookingTo();
            String email = getCustomerEmail();
            reservation.setCustomerEmail(email);
            reservation.setStatus("BOOKING");
            int roomNo = reservation.getRoomNo();

            Room roomDetail = RoomService.findRoomByRoomNo(roomNo);

            // validate room number
            if (roomDetail == null) {
                return new ResponseModel(JSONObject.NULL, "Room is not available to reserve", HttpStatus.CONFLICT);
            }

            // validate booking time
            if (to.before(from)) {
                return new ResponseModel(JSONObject.NULL, "Invalid input", HttpStatus.BAD_REQUEST);
            }

            // check room availibility
            if(!RoomService.checkRoomAvailibilityBetweenDate(roomNo, from, to))
                return new ResponseModel(JSONObject.NULL, "Room is not available to reserve", HttpStatus.CONFLICT);


            String code = generateBookingCode();
            reservation.setCode(code);

            int totalReceipt = roomDetail.getPrice() * (int)Commons.calculateDayDifference(from, to);
            reservation.setTotal(totalReceipt);

            reservationsRepository.save(reservation);

            return new ResponseModel(reservation, "Booking Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private static String generateBookingCode() {
        String code = Commons.generateRandomString();

        while(reservationsRepository.findReservationByCode(code) != null) {
            code = Commons.generateRandomString();
        }
        return code;
    }

    private static String getCustomerEmail() {
        if(Commons.isCustomer())
            return Commons.getEmail();
        return "";
    }


}
