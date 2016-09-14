package tirnak.persistence.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    @Id
    @Column(name = "person_id")
    private int id;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "like", joinColumns = {@JoinColumn(name = "person_id")},
//            inverseJoinColumns = { @JoinColumn(name = "post_id")})
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="likedBy", targetEntity = Post.class)
    public Set<Post> likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addLike(Post tolike) {
        if (likes == null) {
            likes = new HashSet<Post>();
        }
        this.likes.add(tolike);
    }
}
