package springmvc.demo.services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.reservations.ReservationsRepository;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Room;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Response;

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

    /**
     * Cancel Reservation
     * @param code used to to find reservation
     * @return ResponseModel
     */
    public static ResponseModel cancelReservation(String code) {
        try {
            Reservation reservation = findReservationByCode(code);

            if (reservation == null) {
                return new ResponseModel(JSONObject.NULL, "Reservation Not Found", HttpStatus.NOT_FOUND);
            }

            // authenticate reservation's owner
            if(Commons.isCustomer() && !Commons.getEmail().equals(reservation.getCustomerEmail())) {
                return new ResponseModel(JSONObject.NULL, "Forbidden", HttpStatus.FORBIDDEN);
            }

            if(reservation.getStatus().equals("CHECK-IN") || reservation.getStatus().equals("FINISHED")) {
                return new ResponseModel(JSONObject.NULL, "Forbidden", HttpStatus.FORBIDDEN);
            }

            reservation.setStatus("CANCELLED");

            reservationsRepository.save(reservation);

            return new ResponseModel(reservation, "Reservation Canceled", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check in for a reservation
     * @param code find reservation by code
     * @return ResponseModel
     */
    public static ResponseModel checkin(String code) {
        try{
            Reservation reservation = findReservationByCode(code);

            if (reservation == null) {
                return new ResponseModel(JSONObject.NULL, "Reservation Not Found", HttpStatus.NOT_FOUND);
            }

            if(reservation.getStatus().equals("CANCELLED")) {
                return new ResponseModel(JSONObject.NULL, "Forbidden. Reservation has been cancelled", HttpStatus.FORBIDDEN);
            }

            Date checkinTime = new Date();
            reservation.setCheckin(checkinTime);
            reservation.setStatus("CHECK-IN");

            reservationsRepository.save(reservation);

            return new ResponseModel(reservation, "Check-in successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static Reservation findReservationByCode(String code) {
        return reservationsRepository.findReservationByCode(code);
    }


    /**
     * Find all information of a reservation
     * @param code Reservation code
     * @return ResponseModel
     */
    public static ResponseModel findReservation(String code) {
        try{
            Reservation reservation = findReservationByCode(code);

            if (reservation == null) {
                return new ResponseModel(JSONObject.NULL, "Reservation Not Found", HttpStatus.NOT_FOUND);
            }

            if(Commons.isCustomer() && !Commons.getEmail().equals(reservation.getCustomerEmail())) {
                return new ResponseModel(JSONObject.NULL, "Forbidden", HttpStatus.FORBIDDEN);
            }

            return new ResponseModel(reservation, "Retrieve reservation information successfully", HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
