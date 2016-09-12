package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private int id;
}
