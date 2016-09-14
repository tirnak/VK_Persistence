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
    private Post parent;

    @OneToMany(mappedBy = "parent")
    private List<Post> comments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "like", joinColumns = {@JoinColumn(name = "person_id")},
        inverseJoinColumns = { @JoinColumn(name = "post_id",
                    nullable = false, updatable = false) })
    public Set<Person> likedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getRepostOf() {
        return repostOf;
    }

    public void setRepostOf(Post repostOf) {
        this.repostOf = repostOf;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public List<Post> getComments() {
        return comments;
    }

    public void setComments(List<Post> comments) {
        this.comments = comments;
    }

    public Set<Person> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<Person> likedBy) {
        this.likedBy = likedBy;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", repostOf=" + repostOf +
                ", author=" + author +
                ", parent=" + parent +
                ", comments=" + comments +
                ", likedBy=" + likedBy +
                '}';
    }
}
