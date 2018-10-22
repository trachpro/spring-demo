package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.StaffModel;

@Repository
@Transactional
public interface StaffsRepository extends MongoRepository<StaffModel, String> {

    public StaffModel getStaffModelByEmail(String email);
}
