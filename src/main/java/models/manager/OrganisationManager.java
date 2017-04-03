package models.manager;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import models.beans.Organisation;
import models.beans.PeasyUser;

public class OrganisationManager {
	@Inject
	Provider<EntityManager> entitiyManagerProvider;

	public Organisation createOrganisation(String name, PeasyUser organisationManager) {
		// TODO: Implement
		return null;
	}

	public Organisation renameOrganisation(int organisationId, String newName) {
		// TODO: Implement
		return null;
	}

	public Organisation changeOrganisationManager(int organisationId, PeasyUser newAdmin) {
		// TODO: Implement
		return null;
	}

	public Organisation addUser(int organisationId, PeasyUser user) {
		// TODO: Implement
		return null;
	}

	public void removeUser(int organisationId, PeasyUser user) {
		// TODO: Implement
	}

}
