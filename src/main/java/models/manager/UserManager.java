package models.manager;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import models.beans.PeasyUser;

public class UserManager {
	@Inject
	Provider<EntityManager> entitiyManagerProvider;
	
	public UserManager() {	
	}

	@Transactional
	public void createUser(String email, String firstName, String lastName, String passwordCleartext){
		// Get Manager to persist the user object
		EntityManager entityManager = entitiyManagerProvider.get();
		
		// Create user bean
		PeasyUser user = new PeasyUser(email, firstName, lastName, passwordCleartext);
		
		// Persist the user
		entityManager.persist(user);
	}
}
