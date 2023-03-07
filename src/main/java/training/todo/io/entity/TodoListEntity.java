package training.todo.io.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="todos")
public class TodoListEntity implements Serializable {
    public static final long serialVersionUID = 6567337969347974888L;
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, length = 30)
    private String todoListId;
    @Column(nullable = false, length = 30)
    private String title;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
}
