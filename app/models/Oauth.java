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
	public OauthProvider provider;
	
	@OneToOne(fetch=FetchType.EAGER)
	public User user;
	
	public enum OauthProvider {
		GOOGLE_PLUS;

		static public boolean contains(String aName) {
			OauthProvider[] providers = OauthProvider.values();
			for (OauthProvider provider : providers)
				if (provider.toString().equals(aName))
					return true;
			return false;
		}
	}
	
	public static Oauth findByClienIdAndProvider(String clientId, OauthProvider provider) {
		return Oauth.find("byClientIdAndProvider", clientId, provider).first();
	}
}
