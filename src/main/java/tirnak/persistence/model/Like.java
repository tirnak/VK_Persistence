package tirnak.persistence.model;

import javax.persistence.*;

@Entity
@Table(name="vk_like")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person author;

    @Column(name="is_repost")
    private boolean isReposted;


}
