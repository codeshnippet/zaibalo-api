package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "oauth")
public class Oauth extends Model{

	@Column(unique = true, nullable = false)
	public String clientId;
	
	@Enumerated(EnumType.STRING)
	public ServiceProvider provider;
	
	@OneToOne(fetch=FetchType.EAGER)
	public User user;
	
	public static Oauth findByClienIdAndProvider(String clientId, ServiceProvider provider) {
		return Oauth.find("byClientIdAndProvider", clientId, provider).first();
	}
}
