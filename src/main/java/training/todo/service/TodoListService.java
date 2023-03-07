package training.todo.service;

import training.todo.shared.dto.TodoListDto;

public interface TodoListService {
    TodoListDto addTodoList(TodoListDto listDto);
}
