package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="video")
public class Video {
    @Id
    @Column(name = "video_id")
    private int id;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
