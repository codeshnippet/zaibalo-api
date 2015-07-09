package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import play.db.jpa.Model;

@Entity
@Table(name = "posts")
public class Post extends Model {

	@Lob
	@Type(type="org.hibernate.type.StringClobType")
	@NotEmpty
	public String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date")
	@NotNull
	public Date creationDate;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", referencedColumnName="id")
	public User author;

	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	public List<Comment> comments = new ArrayList<Comment>() ;
	
	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	public List<PostAttachment> attachments = new ArrayList<PostAttachment>();
	
	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE)
	private Set<PostRating> ratings = new HashSet<PostRating>();
	
	public Post(){
		creationDate = new Date();
	}
	
	public Post(String content, User author) {
		this();
		this.content = content;
		this.author = author;
	}

    public Object getRatingCount() {
        return ratings.size();
    }

    public Object getRatingSum() {
        int sum = 0;
        for(PostRating postRating: ratings){
            if(postRating.isPositive()){
                sum++;
            }else{
                sum--;
            }
        }
        return sum;
    }

}
