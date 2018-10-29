package springmvc.demo.Repositories.reservations;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import springmvc.demo.models.Reservation;
import java.util.Date;
import java.util.List;

public class ReservationsRepositoryImpl implements ReservationsRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Reservation> findReservationsBetweenDate(Date from, Date to) {

        Query query = new Query();

        Criteria criteria1 = new Criteria();
            criteria1.andOperator(Criteria.where("bookingFrom").gt(from), Criteria.where("bookingFrom").lt(to));

        Criteria criteria2 = new Criteria();
            criteria2.andOperator(Criteria.where("bookingTo").gt(from), Criteria.where("bookingTo").lt(to));

        Criteria criteria3 = new Criteria();
            criteria3.andOperator(Criteria.where("bookingFrom").lte(from), Criteria.where("bookingTo").gte(to));


         Criteria criteria = new Criteria();
         criteria.orOperator(criteria1,criteria2,criteria3);

        query.addCriteria(criteria);

        List<Reservation> reservations = mongoTemplate.find(query, Reservation.class, "reservations");

        return reservations;
    }
}
