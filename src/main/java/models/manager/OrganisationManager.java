package models.manager;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import models.beans.Organisation;
import models.beans.PeasyUser;

/**
 *
 * @author Tugrul
 */
public class OrganisationManager {

    @Inject
    Provider<EntityManager> entitiyManagerProvider;

    /**
     *
     * @param name
     * @param organisationManager
     * @return The Created Organization
     * @throws IllegalArgumentException
     */
    @Transactional
    public Organisation createOrganisation(String name, PeasyUser organisationManager)
            throws IllegalArgumentException {

        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (organisationManager == null) {
            throw new IllegalArgumentException();
        }

        // Get Manager to persist the organization object
        EntityManager entityManager = entitiyManagerProvider.get();
        // Create organisation bean
        Organisation organisation = new Organisation(name, organisationManager);
        // Persist the organisation
        entityManager.persist(organisation);

        return organisation;
    }

    /**
     *
     * @param organisationId
     * @param newName
     * @return The updated Organization
     * @throws IllegalArgumentException
     */
    @Transactional
    public Organisation renameOrganisation(int organisationId, String newName) throws IllegalArgumentException  {
        //check newName is not null
        if (newName == null) {
            throw new IllegalArgumentException();
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);

        //set new name
        organisation.setName(newName);

        return organisation;
    }

    /**
     *
     * @param organisationId
     * @param newAdmin
     * @return The updated Organization
     */
    @Transactional
    public Organisation changeOrganisationManager(int organisationId, PeasyUser newAdmin) throws IllegalArgumentException {
        //check bean newAdmin is not null
        if (newAdmin == null) {
            throw new IllegalArgumentException();
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);

        //set new Admin
        organisation.setOrganisationAdmin(newAdmin);

        return organisation;
    }

    /**
     *
     * @param organisationId
     * @param user
     * @throws IllegalArgumentException
     * @return The updated Organization
     */
    @Transactional
    public Organisation addUser(int organisationId, PeasyUser user) throws IllegalArgumentException {
        //check bean user is not null
        if (user == null) {
            throw new IllegalArgumentException();
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);

        //add User to organization
        organisation.getUsers().add(user);

        return organisation;

    }

    /**
     *
     * @param organisationId
     * @param user
     * @throws IllegalArgumentException
     */
    @Transactional
    public void removeUser(int organisationId, PeasyUser user) throws IllegalArgumentException  {
        //check bean user is not null
        if (user == null) {
            throw new IllegalArgumentException();
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);

        //add User to organization
        organisation.getUsers().remove(user);
    }

}
