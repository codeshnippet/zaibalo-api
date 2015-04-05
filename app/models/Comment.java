package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import play.db.jpa.Model;

@Entity
@Table(name = "comments")
public class Comment extends Model{

	@Lob
	@Type(type="org.hibernate.type.StringClobType")
	@NotEmpty
	public String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date creationDate;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", referencedColumnName="id", nullable = false)
	public User author;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id", referencedColumnName="id", nullable = false)
	public Post post;

	public Comment() {
		creationDate = new Date();
	}
}
