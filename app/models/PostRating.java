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
public class PostRating extends Model{

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date creationDate;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="id")
    public User user;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", referencedColumnName="id")
    public Post post;
    
    @Column(name="value")
    public int value;

    public PostRating(){
        super();
        this.creationDate = new Date();
    }
    
    public PostRating(Post post, User user, boolean isPositive) {
        this();
        this.post = post;
        this.user = user;
        this.value = isPositive ? 1 : -1;
    }

    public boolean isPositive() {
        return value == 1;
    }

    public static boolean hasPostRating(Post post, User user, boolean isPositive) {
        return getPostRating(post, user, isPositive) != null;
    }

    public static PostRating getPostRating(Post post, User user, boolean isPositive) {
        return PostRating.find("byPostAndUserAndValue", post, user, isPositive ? 1 : -1).first();
    }

}
