package springmvc.demo.Repositories.users;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springmvc.demo.models.User;

public interface UsersRepositoryCustom {

    Document findUserAttachingReservation(Pageable pageable);
}
