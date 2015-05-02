package models;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class PostTest extends UnitTest{

	@Before
	public void deleteModels() {
		Fixtures.deleteAllModels();
	}
	
	@Test
	public void testSaveAndGetPost(){
		Fixtures.loadModels("data/posts.yml");
		
		Post post = Post.find("byContent", "test content 1").first();
		assertNotNull(post);
		assertNotNull(post.creationDate);
		assertEquals("franky", post.author.loginName);
		assertEquals("superman@mail.com", post.author.email);
		assertNotNull(post.attachments);
		assertEquals(1, post.attachments.size());
		assertEquals("www.url.com", post.attachments.get(0).url);
		assertEquals(AttachmentType.IMAGE, post.attachments.get(0).type);
	}
}
