package springmvc.demo.repositories.users;

import org.bson.Document;
import org.springframework.data.domain.Pageable;

public interface UsersRepositoryCustom {

    Document findUserAttachingReservation(Pageable pageable);
}
