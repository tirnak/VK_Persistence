package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="vk_like")
public class Like {

    @ManyToOne
    @JoinColumn(name="post_id")
    @PrimaryKeyJoinColumn
    private Post post;

    @ManyToOne
    @JoinColumn(name="person_id")
    @PrimaryKeyJoinColumn
    private Person author;

    @Column(name="is_repost")
    private boolean isReposted;


}
