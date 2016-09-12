package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="picture")
public class Picture {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private int id;
}
