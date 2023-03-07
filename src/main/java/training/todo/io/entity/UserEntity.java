package training.todo.io.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import training.todo.ui.model.request.TodoListModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data // generates getters, setters, etc...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Users")
public class UserEntity implements Serializable, UserDetails {
    public static final long serialVersionUID = -2456506161611934727L;
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 100, unique = true)
    private String username;
    @Column(nullable = false, length = 120)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TodoListEntity> todoLists;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;

    // this method should return a list of roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
