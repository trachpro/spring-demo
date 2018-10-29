package springmvc.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.reservations.ReservationsRepository;
import springmvc.demo.models.Reservation;

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
}
