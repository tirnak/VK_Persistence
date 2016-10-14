package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="link")
public class Link {

    @Id
    @GeneratedValue
    @Column(name = "link_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Column(name="url")
    private String href;

    @Column(name="description")
    private String description;


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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
