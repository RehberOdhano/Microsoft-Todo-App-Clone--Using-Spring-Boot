package training.todo.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import training.todo.UserRepository;
import training.todo.exceptions.UserServiceException;
import training.todo.io.entity.UserEntity;
import training.todo.service.TodoListService;
import training.todo.shared.dto.TodoListDto;
import training.todo.ui.model.request.TodoListModel;
import training.todo.ui.model.response.ErrorMessages;
import training.todo.ui.model.response.TodoListRest;

@RestController
@RequestMapping("/api")
public class TodoController {
    @Autowired
    TodoListService todoListService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/add/todo")
    public TodoListRest addTodo(@RequestBody TodoListModel todolist) {
        UserEntity user = userRepository.findByUserId(todolist.getUserId());
        if(user == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        TodoListDto listDto = new TodoListDto();
        BeanUtils.copyProperties(todolist, listDto);

        TodoListDto todoListDto = todoListService.addTodoList(listDto);
        TodoListRest response = new TodoListRest();
        BeanUtils.copyProperties(todoListDto, response);

        return response;
    }
}
