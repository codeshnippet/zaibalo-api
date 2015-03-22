package models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
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
	
	@Enumerated(EnumType.STRING)
	public ServiceProvider photoProvider = ServiceProvider.AVATARS_IO;
	
	public String token;

	public User(){
		this.registrationDate = new Date();
		this.token = new BigInteger(128, new SecureRandom()).toString(32);
		this.setPassword(new BigInteger(128, new SecureRandom()).toString(32));
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		if(StringUtils.isBlank(this.token)){
			this.token = new BigInteger(32, new SecureRandom()).toString(32);
			this.save();
		}
		return token;
	}
	
	public void setPassword(String password) {
		this.password = hashPassword(password);
	}

	public static User findByLoginName(String loginName) {
		return User.find("byLoginName", loginName).first();
	}
	
	public static String hashPassword(String password) {
		return DigestUtils.md5Hex(password);
	}

}
