package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="picture")
public class Picture {

    @Id
    @Column(name = "picture_id")
    private String id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Column(name="url")
    private String href;


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
