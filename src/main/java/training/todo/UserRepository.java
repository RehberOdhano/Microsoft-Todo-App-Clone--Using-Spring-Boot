package training.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.todo.io.entity.UserEntity;

// this interface will take user entity class and persist the data into the database...
// which means that we can use this class for database queries...
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // the crud operations will be provided by the CrudRepository but if we want to implement a few
    // custom methods then we can define here their definitions...
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    UserEntity findByUsername(String username);
}
