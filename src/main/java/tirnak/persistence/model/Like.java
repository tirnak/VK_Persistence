package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="like")
public class Like {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private int id;
}
