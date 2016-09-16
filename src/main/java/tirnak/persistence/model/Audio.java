package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="person")
public class Audio {

    @Id
    @Column(name = "audio_id")
    private int id;

    @Column(name = "performer")
    private String performer;

    @Column(name = "track")
    private String track;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

}
