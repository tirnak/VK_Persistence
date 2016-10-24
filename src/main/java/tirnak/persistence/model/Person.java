package tirnak.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="person")
public class Person implements Serializable {

    @Id
    @Column(name = "person_href")
    private String href;

    @Column(name = "full_name")
    private String fullName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private List<Post> posts;

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="owner")
    public List<Like> likes;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void addLike(Like like) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        this.likes.add(like);
    }

    public String getFullName() {
        return fullName;
    }
}
