package tirnak.persistence.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    @Id
    @Column(name = "person_href")
    private String href;


    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="likedBy", targetEntity = Post.class)
    public Set<Post> likes;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void addLike(Post tolike) {
        if (likes == null) {
            likes = new HashSet<Post>();
        }
        this.likes.add(tolike);
    }
}
