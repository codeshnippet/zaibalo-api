package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest{
	
	private static final String SECRET_HASHED = "5ebe2294ecd0e0f08eab7690d2a6ee69";

	@Before
	public void deleteModels() {
		Fixtures.deleteAllModels();
	}

	/**
	 * Creating a new user. And retrieving it by email.
	 */
	@Test
	public void saveAndGetUser() {
		Fixtures.loadModels("data/user.yml");
		
	    User user = User.find("byEmail", "superman@mail.com").first();

	    assertNotNull(user);
	    assertEquals("Superman", user.displayName);
	    assertEquals("superman@mail.com", user.email);
	    assertEquals("franky", user.loginName);
	    assertEquals(SECRET_HASHED, user.getPassword());
	}
	
    @Test(expected = PersistenceException.class)
    public void testDisplayNameIsUnique(){
    	User user = new User();
    	user.email = "email1@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.displayName = "TestName";
    	user.save();
    	
    	User otherUserWithSameDisplayName = new User();
    	otherUserWithSameDisplayName.email = "email2@test.com";
    	otherUserWithSameDisplayName.loginName = "login";
    	otherUserWithSameDisplayName.setPassword("password");
    	otherUserWithSameDisplayName.displayName = "TestName";
    	
    	otherUserWithSameDisplayName.save();
    }
    
    @Test(expected = PersistenceException.class)
    public void testEmailIsUnique(){
    	User user = new User();
    	user.email = "email@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.displayName = "TestName!";
    	user.save();
    	
    	User otherUserWithSameEmail = new User();
    	otherUserWithSameEmail.email = "email@test.com";
    	otherUserWithSameEmail.loginName = "login2";
    	otherUserWithSameEmail.setPassword("password");
    	otherUserWithSameEmail.displayName = "TestName@";
    	otherUserWithSameEmail.save();
    }
    
    @Test(expected = PersistenceException.class)
    public void testLoginNameIsUnique(){
    	User user = new User();
    	user.email = "email123@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.displayName = "TestName!";
    	user.save();
    	
    	User otherUserWithSameEmail = new User();
    	otherUserWithSameEmail.email = "email@test.com";
    	otherUserWithSameEmail.loginName = "login";
    	otherUserWithSameEmail.setPassword("password");
    	otherUserWithSameEmail.displayName = "TestName@";
    	otherUserWithSameEmail.save();
    }
}
