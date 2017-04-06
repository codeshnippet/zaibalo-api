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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import play.Play;
import play.db.jpa.Model;

@Entity
@Table(name = "users")
public class User extends Model {
	
	@NotNull
	@Column(name = "login_name", unique = true)
	public String loginName;
	
	@NotNull
	@Column(nullable = false)
	private String password;
	
	@Column(unique=true)
	public String email;

	@Column(name = "display_name", unique = true)
	private String displayName;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date registrationDate;
	
	private String photo = Play.configuration.getProperty("user.default.photo.url");
	
	@Enumerated(EnumType.STRING)
	@Column(name = "photo_provider")
	public ServiceProvider photoProvider = ServiceProvider.ZAIBALO;
	
	public String token;

    @Column(name = "about")
    public String about = "-";

	protected User(){
		this.registrationDate = new Date();
		this.token = new BigInteger(128, new SecureRandom()).toString(32);
		this.password = new BigInteger(128, new SecureRandom()).toString(32);
	}
	
	public User(String loginName, String displayName){
		this();
		this.loginName = loginName;
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		if(StringUtils.isBlank(this.token)){
			this.token = new BigInteger(128, new SecureRandom()).toString(32);
			this.save();
		}
		return token;
	}
	
	public void setPassword(String password) {
		if(StringUtils.isBlank(password)){
			password = new BigInteger(128, new SecureRandom()).toString(32);
		}
		this.password = hashPassword(password);
	}

	public static User findByLoginName(String loginName) {
		return User.find("byLoginName", loginName).first();
	}

	public static String hashPassword(String password) {
		return DigestUtils.md5Hex(password);
	}

	public void setDisplayName(String displayName) {
			this.displayName = displayName;
	}

	public String getDisplayName() {
        if(StringUtils.isEmpty(this.displayName)){
            return loginName;
        }
		return this.displayName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		if(!StringUtils.isEmpty(photo)){
			this.photo = photo;			
		}
	}

}
