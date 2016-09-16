package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="video")
public class Video {

    @Id
    @Column(name = "video_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
