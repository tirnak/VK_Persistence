package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@IdClass(Like.class)
@Table(name="like")
public class Like {

    @Id
    @Column(name = "person_id")
    @ManyToOne
    private Person person;

    @Id
    @Column(name = "post_id")
    @ManyToOne
    private Post post;
}
