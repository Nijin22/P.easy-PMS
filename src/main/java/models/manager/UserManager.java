package models.manager;

import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

import helpers.PasswordHelper;
import models.beans.PeasyUser;
import models.beans.Project;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.jpa.UnitOfWork;
import ninja.lifecycle.Start;

@Singleton
public class UserManager {
	@Inject
	Provider<EntityManager> entitiyManagerProvider;

	public UserManager() {
	}

	@Start(order = 90)
	public void startService() {
		// do something
		System.out.println("UserManager service started.");
	}

	/**
	 * Creates a new user
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param passwordCleartext
	 * @return
	 * @throws GeneralSecurityException
	 *             if there is a problem with hash+slating the password
	 * @throws UserAlreadyExistsException
	 *             if the user already is registered
	 */
	@Transactional
	public PeasyUser createUser(String email, String firstName, String lastName, String passwordCleartext)
			throws GeneralSecurityException, UserAlreadyExistsException {
		// Get Manager to persist the user object
		EntityManager entityManager = entitiyManagerProvider.get();

		// Check if user already exists
		if (entityManager.find(PeasyUser.class, email) == null) {
			// email is unused

			// Compute the hashed + salted password
			String pwHashSalt = PasswordHelper.getSaltedHash(passwordCleartext);

			// Create user bean
			PeasyUser user = new PeasyUser(email, firstName, lastName, pwHashSalt);

			// Persist the user
			entityManager.persist(user);
			return user;
		} else {
			throw new UserAlreadyExistsException();
		}

	}

	/**
	 * Retrieve a user from the database / cache
	 * 
	 * @param email
	 * @return The specified user as a {@link models.beans.PeasyUser} object
	 */
	@UnitOfWork
	public PeasyUser getUser(String email) {
		EntityManager entityManager = entitiyManagerProvider.get();
		PeasyUser user = entityManager.find(PeasyUser.class, email);

		if (user == null) {
			throw new NoSuchElementException("User with email address " + email + "is not in the database");
		} else {
			return user;
		}
	}

	/**
	 * Update a user in the database.
	 * Note, that you NEED to supply all fields. If they are left empty, they will be overwritten to empty.
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param formOfAddress
	 * @return The updated user
	 */
	@Transactional
	public PeasyUser updateUser(String email, String firstName, String lastName, String formOfAddress) {
		EntityManager entityManager = entitiyManagerProvider.get();
		PeasyUser user = entityManager.find(PeasyUser.class, email);
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setFormOfAddress(formOfAddress);
		
		return user;
	}

	public void updatePassword(String email, String passwordCleartext) {
		// TODO: Implement
	}

	public void deactivateUser(String email) {
		// TODO: Implement
	}

	public boolean verifyLogin(String email, String passwordCleartext) {
		// TODO: Implement
		return false;
	}

	public Set<Project> getProjectsWhereUserIsMember(String email) {
		// TODO: Implement
		return null;
	}

	public Set<Project> getProjectsWhereUserIsManager(String email) {
		// TODO: Implement
		return null;
	}

	// private methods:

}
