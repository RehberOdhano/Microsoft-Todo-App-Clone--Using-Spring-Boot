package training.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.todo.io.entity.TodoListEntity;

@Repository
public interface TodoListRepo extends JpaRepository<TodoListEntity, Long> {

}
