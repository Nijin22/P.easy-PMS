package managers;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;

import org.junit.Test;

import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.NinjaTest;

public class UserManagerTest extends NinjaTest {
	final String EMAIL = "testme@nijin.org";
	final String FIRST_NAME = "JOHN";
	final String LAST_NAME = "DOE";
	final String PASSWORD = "correcthorsebatterystaple";
	final String FORM_OF_ADDRESS = "Mr. Doe";

	@Test
	public void CreateAndUpdateUser() {
		// inject userManager from runtime
		UserManager um = getInjector().getInstance(UserManager.class);

		// create user
		try {
			um.createUser(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
		} catch (GeneralSecurityException | UserAlreadyExistsException e) {
			fail();
		}

		// verify attributes are as expected
		assertEquals(FIRST_NAME, um.getUser(EMAIL).getFirstName());
		assertEquals(LAST_NAME, um.getUser(EMAIL).getLastName());

		// change form of address and update
		um.updateUser(EMAIL, FIRST_NAME, LAST_NAME, FORM_OF_ADDRESS);
		assertEquals(FORM_OF_ADDRESS, um.getUser(EMAIL).getFormOfAddress());

		// verify attributes are as expected
		assertEquals(FIRST_NAME, um.getUser(EMAIL).getFirstName());
	}

	@Test
	public void createUserTwice() {
		// inject userManager from runtime
		UserManager um = getInjector().getInstance(UserManager.class);

		// create user

		try {
			um.createUser(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
		} catch (GeneralSecurityException | UserAlreadyExistsException e) {
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
	}

	@Test
	public void checkPasswords() {

	}
}
