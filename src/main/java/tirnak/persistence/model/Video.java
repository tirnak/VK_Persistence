package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="video")
public class Video {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private int id;
}
