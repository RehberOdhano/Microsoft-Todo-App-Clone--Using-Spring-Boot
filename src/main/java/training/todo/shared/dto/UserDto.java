package training.todo.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import training.todo.io.entity.Role;
import training.todo.io.entity.TodoListEntity;
import training.todo.ui.model.request.TodoListModel;

import java.io.Serializable;
import java.util.List;

@Data // generates getters, setters, etc...
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    public static final long serialVersionUID = 6350732993449505397L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Role role;
    private List<TodoListEntity> todoLists;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
}
