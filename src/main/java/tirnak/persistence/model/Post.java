package tirnak.persistence.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="post")
public class Post {

    @Id
    @Column(name = "post_id")
    private int id;

    @Column(name = "date")
    private String date;

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

    @OneToMany(mappedBy = "post")
    private List<Audio> audios;

    @OneToMany(mappedBy = "post")
    private List<Picture> images;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "vk_like", joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "post_id")},
        inverseJoinColumns = { @JoinColumn(name = "person_id", referencedColumnName = "person_id")})
    public Set<Person> likedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public void addLikedBy(Person personLiked) {
        if (likedBy == null) {
            likedBy = new HashSet<>();
        }
        likedBy.add(personLiked);
    }

    public void addAudio(Audio audio) {
        if (audios == null) {
            audios = new ArrayList<>();
        }
        audios.add(audio);
    }

    public void addImage(Picture picture) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(picture);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                (repostOf != null ? ", repostOf=" +  repostOf.getId() : "") +
                ", author=" + author +
                (parent != null ? ", parent=" + parent : "") +
                '}';
    }
}
