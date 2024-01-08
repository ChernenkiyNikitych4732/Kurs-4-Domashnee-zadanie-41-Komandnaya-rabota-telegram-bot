package skypro.teamworktelegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность животного.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    @ManyToOne
    @JoinColumn(name = "animal_owner_id")
    private AnimalOwner animalOwner;
}