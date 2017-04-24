package conf;

import java.security.GeneralSecurityException;

import javax.inject.Singleton;

import ninja.lifecycle.Start;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;

import models.beans.PeasyUser;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;

@Singleton
public class StartupActions {

	private NinjaProperties ninjaProperties;
	private UserManager userManager;

	@Inject
	public StartupActions(NinjaProperties ninjaProperties, UserManager userManager) {
		this.ninjaProperties = ninjaProperties;
		this.userManager = userManager;
	}

	/**
	 * Generates example data (users, project, ...) when using dev or test mode.
	 */
	@Start(order = 50)
	public void generateDummyDataWhenInTest() {
		if (!ninjaProperties.isProd()) {
			// as long as we are not in a production environment...
			try {
				PeasyUser user = userManager.createUser("exampleuser@peasy.com", "John", "Doe", "123");
				System.out.println("Example User created: " + user.toString());
			} catch (GeneralSecurityException | UserAlreadyExistsException e) {
				// This really shouldn't be possible unless something is
				// not configured correctly.
				throw new RuntimeException(e);
			}

		}
	}

}
