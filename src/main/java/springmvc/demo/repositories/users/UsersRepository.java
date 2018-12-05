package springmvc.demo.repositories.users;

import io.github.kaiso.relmongo.config.EnableRelMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.models.User;

@EnableRelMongo
@Repository
@Transactional
public interface UsersRepository extends MongoRepository<User, String>, UsersRepositoryCustom {
//    public void deleteByName
    public User findUserByEmail(String email);
    public User findUserBy_id(String id);
    public Long deleteUserBy_id(String id);
}
