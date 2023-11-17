
package MyProjects.Accede.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "location",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<SportMatch> sportMatches = new ArrayList();

}
