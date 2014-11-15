package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String loginName;
	public String password;
	public String email;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date registrationDate;
	
}
