package tirnak.persistence.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    @Id
    @Column(name = "person_id")
    private int id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "like", joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = { @JoinColumn(name = "post_id")})
    public Set<Post> likes;
}
