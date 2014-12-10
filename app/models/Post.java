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
	public User author;

	public Post(){
		creationDate = new Date();
	}
	
	public Post(String content, User author) {
		this();
		this.content = content;
		this.author = author;
	}
}
