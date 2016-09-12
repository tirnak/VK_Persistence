package tirnak.persistence.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue
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

    @OneToMany
    @JoinColumn(name="person_id")
    private Person author;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="parent_id")
    private Post parentId;

    @OneToMany(mappedBy = "post_id")
    private List<Post> comments;
}
