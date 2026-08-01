package Models;

import General.Roles;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
public class User_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;
    private String Email;
    private Roles Role; //General
    private LocalDateTime Created_at;
    private LocalDateTime Last_login;
    private String Password_hash;
}
