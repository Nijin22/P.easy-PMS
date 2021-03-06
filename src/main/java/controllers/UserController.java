package controllers;

import java.security.GeneralSecurityException;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import filters.LoginFilter;
import models.beans.PeasyUser;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.exceptions.BadRequestException;
import ninja.exceptions.InternalServerErrorException;
import ninja.i18n.Lang;
import ninja.params.Param;
import ninja.session.FlashScope;

@Singleton
public class UserController {
	@Inject
	private UserManager userManager;
	@Inject
	Lang lang;

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
	 * Handles the action after registration (and returns feedback if it worked)
	 * 
	 * @param flashScope
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param passwordCleartext
	 * @param passwordConfirm
	 * @return
	 */
	public Result registerAction(FlashScope flashScope, @Param("first_name") Optional<String> firstName,
			@Param("last_name") Optional<String> lastName, @Param("email") Optional<String> email,
			@Param("password") Optional<String> passwordCleartext,
			@Param("password_confirmation") Optional<String> passwordConfirm) {

		// check all fields were transmitted
		if (firstName.isPresent() && lastName.isPresent() && email.isPresent() && passwordCleartext.isPresent()
				&& passwordConfirm.isPresent()) {

			// make sure email and password are not empty
			if (!email.get().isEmpty() && !passwordCleartext.get().isEmpty()) {

				// verify both passwords match
				if (passwordCleartext.get().equals(passwordConfirm.get())) {

					// create the user
					try {
						userManager.createUser(email.get(), firstName.get(), lastName.get(), passwordCleartext.get());

						// TODO: Send email verification mail
						flashScope.success("register.success");
						return Results.redirect("/login");
					} catch (GeneralSecurityException e) {
						throw new InternalServerErrorException("Error when hashing password");
					} catch (UserAlreadyExistsException e) {
						flashScope.error("registration.UserAlreadyExists");
						return Results.redirect("/login");
					}
				} else {
					flashScope.error("registration.PwDontMatch");
					return Results.redirect("/register");
				}

			} else {
				flashScope.error("registration.EmailOrPwEmpty");
				return Results.redirect("/register");
			}

		} else {
			throw new BadRequestException("Not all required parameters transfered.");
		}

	}

	public Result login() {
		return Results.html();
	}

	public Result loginAction(FlashScope flashScope, Context context, @Param("email") Optional<String> email,
			@Param("password") Optional<String> passwordCleartext) {

		// check fields are there and filled
		if (!email.isPresent() || !passwordCleartext.isPresent()) {
			throw new BadRequestException("Not all required parameters transfered.");
		}
		if (email.get().isEmpty() || passwordCleartext.get().isEmpty()) {
			throw new BadRequestException("Not all required parameters transfered.");
		}

		// Verify credentials
		try {
			if (userManager.verifyLogin(email.get(), passwordCleartext.get())) {
				// Credentials correct

				// Set session
				PeasyUser user = userManager.getUser(email.get());
				context.getSession().put("email", user.getEmailAddress());
				context.getSession().put("firstName", user.getFirstName());
				context.getSession().put("lastName", user.getLastName());

				flashScope.success("login.success");
				return Results.redirect("/dashboard");
			} else {
				// Credentials false
				flashScope.error("login.failed");
				return Results.redirect("/login");
			}
		} catch (GeneralSecurityException e) {
			throw new InternalServerErrorException("Error when hashing password");
		}
	}

	public Result logout(Context context) {
		context.getSession().clear();
		context.getFlashScope().success("logout.success");
		return Results.redirect("/");
	}

	@FilterWith(LoginFilter.class)
	public Result account(Context context) {
		Result result = Results.html();
		PeasyUser user = userManager.getUser(context.getSession().get("email"));

		result.render("user", user);
		if (user.getOrganisation() != null) {
			result.render("organisation", user.getOrganisation().getName());
		} else {
			result.render("organisation", "-");
		}

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result accountUpdate(FlashScope flashScope, Context context, @Param("firstName") Optional<String> firstName,
			@Param("lastName") Optional<String> lastName, @Param("formOfAddress") Optional<String> formOfAddress) {

		userManager.updateUser(context.getSession().get("email"), firstName.get(), lastName.get(), formOfAddress.get());
		context.getSession().put("firstName", firstName.get());
		context.getSession().put("lastName", lastName.get());

		flashScope.success("account.isUpdated");

		return Results.redirect("/account");
	}

	@FilterWith(LoginFilter.class)
	public Result accountUpdatePassword(FlashScope flashScope, Context context,
			@Param("oldPassword") Optional<String> oldPassword, @Param("newPassword") Optional<String> newPassword) {

		try {
			String email = context.getSession().get("email");
			if (userManager.verifyLogin(email, oldPassword.get())) {
				userManager.updatePassword(email, newPassword.get());
				flashScope.success("account.PwUpdate.isUpdated");

			} else {
				flashScope.error("account.PwUpdate.mismatch");
			}

			return Results.redirect("/account");

		} catch (GeneralSecurityException e) {
			throw new InternalServerErrorException("Error when hashing password");
		}
	}

	public Result changeLanguage(FlashScope flashScope, @Param("lang") Optional<String> selectedLanguage) {
		if (selectedLanguage.isPresent() && (selectedLanguage.get().equals("de"))
				|| selectedLanguage.get().equals("en")) {
			Result result = Results.redirect("/");
			;

			flashScope.success("nav.lang.success");
			lang.setLanguage(selectedLanguage.get(), result);

			return result;
		} else {
			throw new BadRequestException("Selected language not valid");
		}

	}

	/////////////////////////////////////////
	// Getters & Setters
	/////////////////////////////////////////

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

}
