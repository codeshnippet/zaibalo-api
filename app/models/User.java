package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.codec.digest.DigestUtils;

import play.db.jpa.Model;

@Entity
@Table(name = "users")
public class User extends Model {
	
	@Column(unique=true)
	public String loginName;
	
	private String password;
	
	@Column(unique=true)
	public String email;
	
	@Column(unique=true)
	public String displayName;

	@Temporal(TemporalType.TIMESTAMP)
	public Date registrationDate;
	
	public String photo = "http://avatars.io/16XTmzfwia";

	public User(){
		this.registrationDate = new Date();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = hashPassword(password);
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
