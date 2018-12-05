package springmvc.demo.repositories.users;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Component;

@Component
public class UsersRepositoryImpl implements UsersRepositoryCustom{

    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UsersRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Document findUserAttachingReservation(Pageable pageable) {

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("reservations")
                .localField("email")
                .foreignField("user_id")
                .as("reservations");

        Aggregation aggregation = Aggregation.newAggregation(
//                new CustomAggregationOperation(new Document(
//                        "$lookup",
//                        new Document("from","reservations")
//                                .append("localField","email")
//                                .append("foreignField", "user_id")
//                                .append("as", "reservations")
//                ))
                lookupOperation
        );

        System.out.println(aggregation);

        Document users = mongoTemplate.aggregate(aggregation, "users",JSONObject.class).getRawResults();

        return users;
    }
}

class CustomAggregationOperation implements AggregationOperation {
    private Document operation;

    public CustomAggregationOperation (Document operation) {
        this.operation = operation;
    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(operation);
    }
}
