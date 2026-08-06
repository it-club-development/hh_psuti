package Models;

import General.Roles;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Companies")
@Getter
@Setter
@NoArgsConstructor
public class Company_entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID User_ID;
    private String Name;
    private String Site;
    private String Description;
    private String Logo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "User_ID")
    private User_entity user;
}
