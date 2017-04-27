package models;

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
	    assertEquals("Superman", user.getDisplayName());
	    assertEquals("superman@mail.com", user.email);
	    assertEquals("franky", user.loginName);
	    assertEquals(SECRET_HASHED, user.getPassword());
        assertEquals("AboutFrank", user.about);
	}
	
    @Test(expected = PersistenceException.class)
    public void testDisplayNameIsUnique(){
    	User user = new User();
    	user.email = "email1@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.setDisplayName("TestName");
    	user.save();
    	
    	User otherUserWithSameDisplayName = new User();
    	otherUserWithSameDisplayName.email = "email2@test.com";
    	otherUserWithSameDisplayName.loginName = "login";
    	otherUserWithSameDisplayName.setPassword("password");
    	otherUserWithSameDisplayName.setDisplayName("TestName");
    	
    	otherUserWithSameDisplayName.save();
    }
    
    @Test(expected = PersistenceException.class)
    public void testEmailIsUnique(){
    	User user = new User();
    	user.email = "email@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.setDisplayName("TestName!");
    	user.save();
    	
    	User otherUserWithSameEmail = new User();
    	otherUserWithSameEmail.email = "email@test.com";
    	otherUserWithSameEmail.loginName = "login2";
    	otherUserWithSameEmail.setPassword("password");
    	otherUserWithSameEmail.setDisplayName("TestName@");
    	otherUserWithSameEmail.save();
    }
    
    @Test(expected = PersistenceException.class)
    public void testLoginNameIsUnique(){
    	User user = new User();
    	user.email = "email123@test.com";
    	user.loginName = "login";
    	user.setPassword("password");
    	user.setDisplayName("TestName!");
    	user.save();
    	
    	User otherUserWithSameEmail = new User();
    	otherUserWithSameEmail.email = "email@test.com";
    	otherUserWithSameEmail.loginName = "login";
    	otherUserWithSameEmail.setPassword("password");
    	otherUserWithSameEmail.setDisplayName("TestName@");
    	otherUserWithSameEmail.save();
    }
    
    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void testLoginNameIsRequired(){
    	User user = new User();
    	user.email = "email123@test.com";
    	user.setPassword("password");
    	user.setDisplayName("TestName!");

    	user.loginName = null;

    	user.save();
    }
    
    @Test
    public void testPasswordIsGeneratedIfNull(){
    	User user = new User();
    	user.loginName = "login";
    	user.email = "email123@test.com";
    	user.setDisplayName("TestName!");
    	
    	user.setPassword(null);

    	user.save();
    	User updatedUser = User.find("byEmail", "email123@test.com").first();
    	assertNotNull(updatedUser.getPassword());
    }

    @Test
    public void testSaveUserWithNullDisplayName(){
        User user = new User("login", null);
        user.setPassword("password");
        user.email =  null;
        user.save();

        User updatedUser = User.find("byLoginName", "login").first();
        assertNotNull(updatedUser.getDisplayName());
        assertEquals(updatedUser.getDisplayName(), "login");
    }

    @Test
    public void testSaveUserWithNullOptionalFields(){
    	User user = new User("login", null);
    	user.setPassword("password");
    	user.email =  null;
    	user.save();
    	
    	User user2 = new User("login2", null);
    	user2.setPassword("password2");
    	user2.email = null;
    	user2.save();
    }
    
    @Test
    public void testCreateUsersWithoutEmail(){
    	User user = new User("login222", "login222");
    	user.save();
    }
}
