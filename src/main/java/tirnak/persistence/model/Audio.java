package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="audio")
public class Audio {

    @Id
    @Column(name = "audio_id")
    private String id;

    @Column(name = "performer")
    private String performer;

    @Column(name = "track")
    private String track;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;



    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
