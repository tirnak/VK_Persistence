package tirnak.persistence.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="post")
public class Post {
    @Id
    @Column(name = "post_id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "text")
    private String text;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="repost_of")
    private Post repostOf;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person author;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="parent_id")
    private Post parentId;

    @OneToMany(mappedBy = "parent_id")
    private List<Post> comments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "like", joinColumns = {@JoinColumn(name = "post_id")},
        inverseJoinColumns = { @JoinColumn(name = "person_id",
                    nullable = false, updatable = false) })
    public Set<Person> likedBy;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", repostOf=" + repostOf +
                ", author=" + author +
                ", parentId=" + parentId +
                ", comments=" + comments +
                ", likedBy=" + likedBy +
                '}';
    }
}
