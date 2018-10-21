package springmvc.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springmvc.demo.models.User;

import java.util.List;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
//    public void deleteByName
    public User findUserByEmail(String email);
    public User findUserBy_id(String id);
    public Long deleteUserBy_id(String id);
}
