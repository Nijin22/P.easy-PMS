package controllers;

import java.security.GeneralSecurityException;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import models.beans.PeasyUser;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.FlashScope;

@Singleton
public class UserController {
	@Inject
	private UserManager userManager;

	/**
	 * Displaying the site register form
	 * 
	 * @return
	 */
	public Result register() {
		Result result = Results.html();

		return result;
	}

	/**
	 * Handle the filled register form
	 * 
	 * @return
	 */
	public Result registerAction(FlashScope flashScope, @Param("first_name") Optional<String> firstName,
			@Param("last_name") Optional<String> lastName, @Param("email") Optional<String> email,
			@Param("password") Optional<String> passwordCleartext) {

		// check all fields were transmitted
		if (firstName.isPresent() && lastName.isPresent() && email.isPresent() && passwordCleartext.isPresent()) {
			// make sure email and password are not empty
			if (!email.get().isEmpty() && !passwordCleartext.get().isEmpty()) {
				// TODO: Send email verification mail
				
				// TODO: Verify both passwords match

				try {
					PeasyUser user = userManager.createUser(email.get(), firstName.get(), lastName.get(), passwordCleartext.get());
					return Results.html().render("userFirstName", user.getFirstName());
				} catch (GeneralSecurityException e) {
					return Results.internalServerError();
				} catch (UserAlreadyExistsException e) {
					flashScope.error("registration.UserAlreadyExists");
					return Results.badRequest();
				}
			} else {
				flashScope.error("registration.EmailOrPwEmpty");
				return Results.badRequest();
			}

		} else {
			return Results.badRequest();
		}

	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
