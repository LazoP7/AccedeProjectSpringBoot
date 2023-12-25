
package MyProjects.Accede.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "username")
    private String username;
    @Column(name = "mail")
    private String mail;
    @Column(name = "password")
    private String password;
    @Column(name = "reputation")
    private Integer reputation;
    @Column(name = "age")
    private Integer age;
    @Column(name = "prof_descr")
    private String profDescr;
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles = new HashSet();
    @JsonBackReference
    @ManyToMany(
            mappedBy = "players"
    )
    private Set<SportMatch> mySportMatches = new HashSet();

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, username, mail, password, reputation, age, profDescr);
    }

}
