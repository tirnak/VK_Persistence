package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static tirnak.persistence.common.NullObjects.returnListOrEmpty;
import static tirnak.persistence.common.StringEnhanced.wrapString;

@Entity
@Table(name="post")
public class Post implements Serializable {
    @Id
    @Column(name = "post_id")
    private String id;

    @Column(name = "date")
    private String date;

    @Column(name = "text", length = 10000)
    @Type(type = "org.hibernate.type.NTextType")
    private String text;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="repost_of")
    private Post repostOf;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="author_href")
    private Person author;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="parent_id")
    private Post parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Post> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Audio> audios;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Picture> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Link> links;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Like> likes;

    @Column(name = "saved_at", updatable = false)
    private Date savedAt = new Date();

    @Column(name = "version", updatable = false)
    private int version = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return wrapString(date).toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return wrapString(text).toString();
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
        return returnListOrEmpty(comments);
    }

    public void setComments(List<Post> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return returnListOrEmpty(likes);
    }

    public List<Picture> getImages() {
        return returnListOrEmpty(images);
    }

    public void addLike(Like like) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(like);
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

    public void addComment(Post comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public void addLink(Link link) {
        if (links == null) {
            links = new ArrayList<>();
        }
        links.add(link);
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

    public void persistRecursive(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        _persist(session);
        tx.commit();
        session.close();
    }

    private void _persist(Session session) {
        if (this.getRepostOf() != null) {
            this.getRepostOf()._persist(session);
        }
        if (session.get(Person.class, getAuthor().getHref()) == null) {
            session.save(getAuthor());
        }
        session.saveOrUpdate(this);
        getLikes().stream().forEach(like -> {
            if (session.get(Person.class, like.getOwner().getHref()) == null) {
                session.save(like.getOwner());
            }
            session.save(like);
        });
        for (Post comment : this.getComments()) {
            comment._persist(session);
        }
    }
}
