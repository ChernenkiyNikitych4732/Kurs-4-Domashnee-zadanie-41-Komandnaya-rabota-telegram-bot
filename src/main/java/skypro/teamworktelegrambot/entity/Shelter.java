package skypro.teamworktelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Сущность приюта для животных.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "shelter")
    @JsonIgnore
    private Collection<Report> reports;
    @OneToMany(mappedBy = "shelter")
    @JsonIgnore
    private Collection<AnimalOwner> animalOwners;
    @OneToMany(mappedBy = "shelter")
    @JsonIgnore
    private Collection<Volunteer> volunteers;

    public Shelter(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}