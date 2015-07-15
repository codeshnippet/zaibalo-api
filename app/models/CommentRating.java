package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="comment_rating")
public class CommentRating extends Rating {

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="comment_id", referencedColumnName="id")
	public Comment comment;

    public CommentRating(Comment comment, User user, boolean isPositive) {
        super(user, isPositive);
        this.comment = comment;
    }
	
    public static boolean hasCommentRating(Comment comment, User user, boolean isPositive) {
        return getCommentRating(comment, user, isPositive) != null;
    }

    public static CommentRating getCommentRating(Comment comment, User user, boolean isPositive) {
        return CommentRating.find("byCommentAndUserAndValue", comment, user, isPositive ? 1 : -1).first();
    }

}
