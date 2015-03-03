package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;

import play.db.jpa.Model;

@Entity
@Table(name = "users")
public class User extends Model {
	
	@NotNull
	@Column(unique = true, nullable = false)
	public String loginName;
	
	@NotNull
	@Column(nullable = false)
	public String password;
	
	@Column(unique=true)
	public String email;
	
	@Column(unique=true)
	public String displayName;

	@Temporal(TemporalType.TIMESTAMP)
	public Date registrationDate;
	
	public String photo;

	public User(){
		this.registrationDate = new Date();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : hashPassword(password);
	}

	public static User findByAuthToken(String authToken) {
		return User.find("byAuthToken", authToken).first();
	}

	public static User findByLoginName(String loginName) {
		return User.find("byLoginName", loginName).first();
	}
	
	public static String hashPassword(String password) {
		return DigestUtils.md5Hex(password);
	}
}
