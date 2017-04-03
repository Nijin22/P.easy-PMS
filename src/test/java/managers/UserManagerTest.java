package managers;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;

import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Injector;

import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.NinjaTest;

public class UserManagerTest extends NinjaTest{

	@Test
	public void CrudUser(){
		final String EMAIL = "testme@nijin.org";
		final String FIRST_NAME = "JOHN";
		final String LAST_NAME = "DOE";
		final String PASSWORD = "correcthorsebatterystaple";
		
		// this is the application guice injector
        Injector injector = getInjector();
    	UserManager um = injector.getInstance(UserManager.class);
		
		// create user
		try {
			um.createUser(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
		} catch (GeneralSecurityException e) {
			fail();
		} catch (UserAlreadyExistsException e) {
			fail();
		}
		
		// try to create him again (which should fail)
		try {
			um.createUser(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
			fail(); // there should have been an exception here
		} catch (GeneralSecurityException e) {
			fail();
		} catch (UserAlreadyExistsException e) {
			// this is expected
		}
		
		// verify the name is as expected
		assertEquals(FIRST_NAME, um.getUser(EMAIL).getFirstName());
	}
}
