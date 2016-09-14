package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="picture")
public class Picture {

    @Id
    @Column(name = "picture_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
