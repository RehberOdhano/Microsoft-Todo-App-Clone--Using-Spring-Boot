package training.todo.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.todo.TodoListRepo;
import training.todo.exceptions.UserServiceException;
import training.todo.io.entity.TodoListEntity;
import training.todo.io.entity.UserEntity;
import training.todo.service.TodoListService;
import training.todo.shared.Utils;
import training.todo.shared.dto.TodoListDto;

@Service
public class TodoListServiceImplementation implements TodoListService {
    @Autowired
    TodoListRepo todoListRepo;
    @Autowired
    Utils utils;
    @Override
    public TodoListDto addTodoList(TodoListDto listDto) {
        try {
            TodoListEntity todoListEntity = new TodoListEntity();
            BeanUtils.copyProperties(listDto, todoListEntity);

            todoListEntity.setTitle(listDto.getTitle().trim());
            todoListEntity.setTodoListId(utils.generateUserId(30));

            System.out.println(todoListEntity);

            TodoListEntity savedList = todoListRepo.save(todoListEntity);

            TodoListDto todoListDto = new TodoListDto();
            BeanUtils.copyProperties(savedList, todoListDto);

            System.out.println(todoListDto);

            return todoListDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
