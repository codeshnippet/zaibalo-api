package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class Post extends Model {

	@Lob
	public String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date creationDate;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User author;

	public Post(String content) {
		this.content = content;
		creationDate = new Date();
	}
}
