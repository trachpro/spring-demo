package springmvc.demo.controllers;


import antlr.collections.List;
import org.json.JSONObject;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.services.ReservationService;
import springmvc.demo.utils.Commons;
import springmvc.demo.utils.Response;

import java.util.Map;

@RestController
@RequestMapping("api/reservations")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ReservationsController {

    @GetMapping("/{code}")
    public @ResponseBody ResponseEntity<String> getReservation(@PathVariable String code) {
        if (code.length() == 0) {
            return new ResponseModel(JSONObject.NULL, "Invalid input", HttpStatus.BAD_REQUEST).toResponse();
        }

        return ReservationService.findReservation(code).toResponse();
    }

    /**
     * API to make reservation
     * @param params: {name: client's name, from: booking from, to: booking to, room: room number}
     * @return ResponseEntity
     */
    @PostMapping(produces = {"application/hal+json"})
    public @ResponseBody
    ResponseEntity<String> createReservation(@RequestBody Map<String, String> params) {
        Reservation reservation = Commons.getReservationFromParams(params);

        if(reservation == null) {
            return new ResponseModel(JSONObject.NULL, "Invalid parameters", HttpStatus.BAD_REQUEST).toResponse();
        }

        return ReservationService.addNewReservation(reservation).toResponse();
    }

    @GetMapping("/own")
    public ResponseEntity<String> getReservationBelongToOwnCustomer() {

        return ReservationService.findReservationByUser().toResponse();
    }

    @PostMapping("/{offsets}/{size}")
    public ResponseEntity<String> pagingReservation(@PathVariable int offsets, @PathVariable int size, @RequestBody Map<String, String[]> body) {

        String[] statusList = body.get("status");
        return ReservationService.getPageReservations(statusList, offsets, size).toResponse();
    }

    @PutMapping("/cancel/{code}")
    public @ResponseBody ResponseEntity<String> cancelReservation(@PathVariable String code) {
        if (code.length() == 0) {
            return new ResponseModel(JSONObject.NULL, "Invalid input", HttpStatus.BAD_REQUEST).toResponse();
        }

        return ReservationService.cancelReservation(code).toResponse();
    }

    @PutMapping("/check-in/{code}")
    public @ResponseBody ResponseEntity<String> checkin(@PathVariable String code) {
        if (code.length() == 0) {
            return new ResponseModel(JSONObject.NULL, "Invalid input", HttpStatus.BAD_REQUEST).toResponse();
        }

        return ReservationService.checkin(code).toResponse();
    }

    @PutMapping("/check-out/{code}")
    public @ResponseBody ResponseEntity<String> checkout(@PathVariable String code) {
        if (code.length() == 0) {
            return new ResponseModel(JSONObject.NULL, "Invalid input", HttpStatus.BAD_REQUEST).toResponse();
        }

        return ReservationService.checkout(code).toResponse();
    }

    @GetMapping("/admin/{from}/{to}")
    public @ResponseBody ResponseEntity<String> analyzeRevenue(@PathVariable String from, @PathVariable String to) {

        return ReservationService.analyzeRevenue(from, to).toResponse();
    }

}
