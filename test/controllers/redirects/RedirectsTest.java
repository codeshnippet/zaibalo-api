package controllers.redirects;

import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

import static org.junit.Assert.*;

public class RedirectsTest extends FunctionalTest {

    @Test
    public void testLoginRedirect(){
        Http.Response response = GET("/login");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/signup", response);
    }

    @Test
    public void testCategoryRedirect(){
        Http.Response response = GET("/category/11");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryWithoutIdRedirect(){
        Http.Response response = GET("/category");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategorySlashWithoutIdRedirect(){
        Http.Response response = GET("/category/");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryOrderRedirect(){
        Http.Response response = GET("/category/5/order/month");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryPageRedirect(){
        Http.Response response = GET("/category/5/page/6");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryCountRedirect(){
        Http.Response response = GET("/category/5/count/10");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryCountPageRedirect(){
        Http.Response response = GET("/category/5/count/10/page/3");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryOrderPageRedirect(){
        Http.Response response = GET("/category/5/order/month/page/10");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryOrderCountRedirect(){
        Http.Response response = GET("/category/5/order/month/count/10");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }

    @Test
    public void testCategoryOrderCountPageRedirect(){
        Http.Response response = GET("/category/5/order/month/count/10/page/13");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/#/", response);
    }
}