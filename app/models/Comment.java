package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import play.db.jpa.Model;

@Entity
@Table(name = "comments")
public class Comment extends Model implements Ratable {

	@Lob
	@Type(type="org.hibernate.type.StringClobType")
	@NotEmpty
	public String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date creationDate;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", referencedColumnName="id", nullable = false)
	public User author;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id", referencedColumnName="id", nullable = false)
	public Post post;

	public Comment() {
		creationDate = new Date();
	}

	@Override
	public boolean hasRating(User user, boolean isPositive) {
		return CommentRating.hasCommentRating(this, user, isPositive);
	}

	@Override
	public Rating getRating(User user, boolean isPositive) {
		return CommentRating.getCommentRating(this, user, isPositive);
	}

	@Override
	public Rating createRating(User user, boolean isPositive) {
		return new CommentRating(this, user, isPositive);
	}

}
