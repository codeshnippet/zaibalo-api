package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import play.db.jpa.Model;

@Entity
@Table(name = "posts")
public class Post extends Model implements Ratable {

	@Lob
	@Type(type="org.hibernate.type.StringClobType")
	@NotEmpty
	public String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date")
	@NotNull
	public Date creationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_id", referencedColumnName="id")
	public User author;

	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	public List<Comment> comments = new ArrayList<Comment>() ;
	
	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	public List<PostAttachment> attachments = new ArrayList<PostAttachment>();
	
	@OneToMany(mappedBy = "post", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	public Set<PostRating> ratings = new HashSet<PostRating>();
	
	public Post(){
		creationDate = new Date();
	}
	
	public Post(String content, User author) {
		this();
		this.content = content;
		this.author = author;
	}

    public Integer getRatingCount() {
        return ratings.size();
    }

    public Integer getRatingSum() {
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

	@Override
	public boolean hasRating(User user, boolean isPositive) {
		return PostRating.hasPostRating(this, user, isPositive);
	}

	@Override
	public Rating getRating(User user, boolean isPositive) {
		return PostRating.getPostRating(this, user, isPositive);
	}

	@Override
	public Rating createRating(User user, boolean isPositive) {
		return new PostRating(this, user, isPositive);
	}

    public enum SortBy {
        CREATION_DATE
    }

}
