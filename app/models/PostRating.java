package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
@Table(name="post_rating")
public class PostRating extends Rating{
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", referencedColumnName="id")
    public Post post;
    
    public PostRating(Post post, User user, boolean isPositive) {
        super(user, isPositive);
        this.post = post;
    }

    public static boolean hasPostRating(Post post, User user, boolean isPositive) {
        return getPostRating(post, user, isPositive) != null;
    }

    public static PostRating getPostRating(Post post, User user, boolean isPositive) {
        return PostRating.find("byPostAndUserAndValue", post, user, isPositive ? 1 : -1).first();
    }

}
