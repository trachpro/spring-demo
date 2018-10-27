package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.Staff;

@Repository
@Transactional
public interface StaffsRepository extends MongoRepository<Staff, String> {

    public Staff getStaffModelByEmail(String email);
}
