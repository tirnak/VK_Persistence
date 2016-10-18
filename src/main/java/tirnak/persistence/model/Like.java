package tirnak.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="vk_like")
public class Like implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name="post_id")
    @PrimaryKeyJoinColumn
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name="person_href")
    @PrimaryKeyJoinColumn
    private Person author;

    @Column(name="is_repost")
    private boolean isReposted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Like like = (Like) o;

        if (isReposted != like.isReposted) return false;
        if (author != null ? !author.equals(like.author) : like.author != null) return false;
        if (post != null ? !post.equals(like.post) : like.post != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = post != null ? post.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (isReposted ? 1 : 0);
        return result;
    }
}
