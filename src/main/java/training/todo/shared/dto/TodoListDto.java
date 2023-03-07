package training.todo.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoListDto implements Serializable {
    private static final long serialVersionUID = 2094720424047524319L;
    private long id;
    private String userId;
    private String todoListId;
    private String title;
}
