package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.GenericModel;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Rating extends GenericModel {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
    public Long id;

    public Long getId() {
        return id;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date creationDate;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="id")
    public User user;
    
    @Column(name="value")
    public int value;

    public Rating(){
        this.creationDate = new Date();
    }
    
    public Rating(User user, boolean isPositive){
    	this();
        this.user = user;
        this.value = isPositive ? 1 : -1;
    }
    
    public boolean isPositive() {
        return value == 1;
    }
    
    @Override
    public Object _key() {
        return getId();
    }
}
