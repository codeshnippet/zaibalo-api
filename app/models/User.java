package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String loginName;
	public String password;
	public String email;
	public String displayName;
	public String authToken;

	@Temporal(TemporalType.TIMESTAMP)
	public Date registrationDate;

	public User(){
		this.registrationDate = new Date();
	}
	
	public static User findByAuthToken(String authToken) {
		return User.find("byAuthToken", authToken).first();
	}

	public String createToken() {
		authToken = UUID.randomUUID().toString();
        save();
        return authToken;
	}

	public void deleteToken() {
		authToken = null;
		save();
	}
	
}
