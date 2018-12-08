package springmvc.demo.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.repositories.reservations.ReservationsRepository;
import springmvc.demo.models.*;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Converts;
import springmvc.demo.utils.Message;

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
                return new ResponseModel(JSONObject.NULL, Message.CONFLICT, HttpStatus.CONFLICT);
            }

            // validate booking time
            if (to.before(from)) {
                return new ResponseModel(JSONObject.NULL, Message.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }

            // check room availibility
            if(!RoomService.checkRoomAvailibilityBetweenDate(roomNo, from, to))
                return new ResponseModel(JSONObject.NULL, Message.CONFLICT, HttpStatus.CONFLICT);


            String code = generateBookingCode();
            reservation.setCode(code);

            int totalReceipt = roomDetail.getPrice() * (int)Commons.calculateDayDifference(from, to);
            reservation.setTotal(totalReceipt);

            reservationsRepository.save(reservation);

            return new ResponseModel(reservation, Message.SUCCESS, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseModel(JSONObject.NULL, Message.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            // authenticate reservation's owner
            if(Commons.isCustomer() && !Commons.getEmail().equals(reservation.getCustomerEmail())) {
                return new ResponseModel(JSONObject.NULL, Message.FORBIDDEN, HttpStatus.FORBIDDEN);
            }

            if(reservation.getStatus().equals("CHECK-IN") || reservation.getStatus().equals("FINISHED")) {
                return new ResponseModel(JSONObject.NULL, Message.FORBIDDEN, HttpStatus.FORBIDDEN);
            }

            reservation.setStatus("CANCELLED");

            reservationsRepository.save(reservation);

            return new ResponseModel(reservation, Message.SUCCESS, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check in for a reservation
     * @param code find reservation by code
     * @return ResponseModel
     */
    public static ResponseModel checkin(String code) {
        try{
            Reservation re = findReservationByCode(code);
            if(re == null) {
                return new ResponseModel(JSONObject.NULL, Message.NOT_FOUND, HttpStatus.NOT_FOUND);
            } else if( !re.getStatus().equals("BOOKING") ) {
                return new ResponseModel(JSONObject.NULL, Message.CONFLICT, HttpStatus.CONFLICT);
            }

            Date checkinTime = new Date();

            ResponseModel response = updateReservation(code, checkinTime, null, "CHECK-IN");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check out activity
     * @param code find reservation to update by code
     * @return ResponseModel
     */
    public static ResponseModel checkout(String code) {
        try{
            Reservation re = findReservationByCode(code);
            if(re == null) {
                return new ResponseModel(JSONObject.NULL, Message.NOT_FOUND, HttpStatus.NOT_FOUND);
            } else if( !re.getStatus().equals("CHECK-IN") ) {
                return new ResponseModel(JSONObject.NULL, Message.CONFLICT, HttpStatus.CONFLICT);
            }

            Date checkoutTime = new Date();

            ResponseModel response = updateReservation(code, null, checkoutTime, "FINISHED");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ResponseModel updateReservation(String code, Date checkinTime, Date checkoutTime, String status) {
        Reservation reservation = findReservationByCode(code);

        if (reservation == null) {
            return new ResponseModel(JSONObject.NULL, Message.NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(reservation.getStatus().equals("CANCELLED")) {
            return new ResponseModel(JSONObject.NULL, Message.FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        String msg = "";
        if(checkinTime != null) {
            reservation.setCheckin(checkinTime);
            reservation.setStatus("CHECK-IN");
            msg = "Check-in successfully";
        }


        if(checkoutTime != null) {
            reservation.setStatus("FINISHED");
            reservation.setCheckout(checkoutTime);
            msg = "Check-out successfully";
        }


        reservation.setStatus(status);

        return new ResponseModel(reservation, Message.SUCCESS, HttpStatus.OK);
    }

    private static Reservation findReservationByCode(String code) {
        return reservationsRepository.findReservationByCode(code);
    }

    private static List<Reservation> findReservationByEmail(String email) {
        return reservationsRepository.findReservationsByCustomerEmail(email);
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
                return new ResponseModel(JSONObject.NULL, Message.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            if(Commons.isCustomer() && !Commons.getEmail().equals(reservation.getCustomerEmail())) {
                return new ResponseModel(JSONObject.NULL, Message.FORBIDDEN, HttpStatus.FORBIDDEN);
            }

            return new ResponseModel(reservation, Message.SUCCESS, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Find all information of a reservation
     * @return List<ResponseModel></>
     */
    public static ResponseModel findReservationByUser() {

        try{
            List<Reservation> reservations = findReservationByEmail(Commons.getEmail());

            if (reservations == null) {
                return new ResponseModel(JSONObject.NULL, "Reservation Not Found", HttpStatus.NOT_FOUND);
            }

            return new ResponseModel(reservations, "Retrieve reservation information successfully", HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Calculate number of reserved rooms, and total revenue between 2 dates
     * @param sFrom starting day
     * @param sTo ending day
     * @return ResponseModel
     */
    public static ResponseModel analyzeRevenue(String sFrom, String sTo) {
        try {
            Date from = Converts.convertStringToDate(sFrom);
            Date to = Converts.convertStringToDate(sTo);

            if(to.before(from)) {
                return new ResponseModel(JSONObject.NULL, Message.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }
            List<Revenue> list = reservationsRepository.findRevenueByMonth(from, to);


            return new ResponseModel(new Revenues(list), Message.SUCCESS, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * get reservation by status and pagination
     * @param statusList List of status
     * @param offsets page
     * @param size size
     * @return ResponseModel
     */
    public static ResponseModel getPageReservations(String[] statusList, int offsets, int size) {
        try {

            size = size == 0? 10: size;

            Page result = reservationsRepository.findReservationsWithPaging(statusList, offsets, size);
            return new ResponseModel(result, "Retrieve information successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(JSONObject.NULL,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
