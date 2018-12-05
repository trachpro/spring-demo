package springmvc.demo.repositories.reservations;


import org.bson.Document;
import org.springframework.data.domain.Page;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.Revenue;

import java.util.Date;
import java.util.List;

public interface ReservationsRepositoryCustom {

    public List<Reservation> findReservationsBetweenDate(Date from, Date to);
    public List<Revenue> findRevenueByMonth(Date from, Date to);
    public Page<Reservation> findReservationsWithPaging(String[] statusList, int offsets, int size);

}
