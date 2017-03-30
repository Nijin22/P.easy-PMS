package models.manager;

import java.util.Set;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import models.beans.PeasyUser;
import models.beans.Project;

public class UserManager {
	@Inject
	Provider<EntityManager> entitiyManagerProvider;

	public UserManager() {
	}

	@Transactional
	public PeasyUser createUser(String email, String firstName, String lastName, String passwordCleartext) {
		// Get Manager to persist the user object
		EntityManager entityManager = entitiyManagerProvider.get();

		// Create user bean
		PeasyUser user = new PeasyUser(email, firstName, lastName, passwordCleartext);

		// Persist the user
		entityManager.persist(user);

		return user;
	}

	public PeasyUser updateUser(String firstName, String lastName, String formOfAddress) {
		// TODO: Implement
		return null;
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
	
	public Set<Project> getProjectsWhereUserIsMember(String email){
		// TODO: Implement
		return null;
	}
	
	public Set<Project> getProjectsWhereUserIsManager(String email){
		// TODO: Implement
		return null;
	}

}
