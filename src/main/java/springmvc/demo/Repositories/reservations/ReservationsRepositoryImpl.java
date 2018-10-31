package springmvc.demo.Repositories.reservations;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.Revenue;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

    @Override
    public List<Revenue> findRevenueByMonth(Date from, Date to) {
        Query findQuery = new Query();

        Criteria criteria1 = new Criteria();
        criteria1.andOperator(Criteria.where("bookingFrom").gt(from) );

        Criteria criteria2 = new Criteria();
        criteria2.andOperator(Criteria.where("bookingFrom").lt(to));

        Criteria criteria3 = new Criteria();
        criteria3.andOperator(Criteria.where("status").ne("CANCELLED"));

        Criteria criteria = new Criteria();
        criteria.andOperator(criteria1,criteria2,criteria3);

        findQuery.addCriteria(criteria);

        Aggregation aggregation = newAggregation(
                match(criteria)
                ,project("total")
                        .andExpression("month(bookingFrom)").as("month")
                        .andExpression("year(bookingFrom)").as("year")
                ,group("month","year").count().as("numberOfRooms")
                        .sum("total").as("revenue")

        );


        AggregationResults<Revenue> listRevenue = mongoTemplate.aggregate(aggregation, "reservations", Revenue.class);
        return listRevenue.getMappedResults();
    }
}
