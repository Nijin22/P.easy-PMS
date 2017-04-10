package models.manager;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import java.util.NoSuchElementException;


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
     * @return created organization bean
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
        Organisation organisation = new Organisation();
        organisation.setName(name);
        organisation.setOrganisationAdmin(organisationManager);
        organisationManager.setOrganisation(organisation);
        // Persist the organisation
        entityManager.persist(organisation);
        
        return organisation;
    }

    /**
     *
     * @param organisationId
     * @param newName
     * @return updated organization bean
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Transactional
    public Organisation renameOrganisation(int organisationId, String newName) throws IllegalArgumentException, NoSuchElementException {
        //check newName is not null
        if (newName == null) {
            throw new IllegalArgumentException();
        }
        
        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);
        
        if (organisation == null) {
            throw new NoSuchElementException();
        } else {
            //set new name
            organisation.setName(newName);
            
            return organisation;
        }
        
    }

    /**
     *
     * @param organisationId
     * @param newAdmin
     * @return updated organization bean
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Transactional
    public Organisation changeOrganisationManager(int organisationId, PeasyUser newAdmin) throws IllegalArgumentException, NoSuchElementException {
        //check bean newAdmin is not null
        if (newAdmin == null) {
            throw new IllegalArgumentException();
        }
        
        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);
        
        if (organisation == null) {
            throw new NoSuchElementException();
        } else {
            //set new Admin
            organisation.setOrganisationAdmin(newAdmin);
            return organisation;
        }
    }

    /**
     *
     * @param organisationId
     * @param user
     * @return updated organization bean
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Transactional
    public Organisation addUser(int organisationId, PeasyUser user) throws IllegalArgumentException, NoSuchElementException {
        //check bean user is not null
        if (user == null) {
            throw new IllegalArgumentException();
        }
        
        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);
        
        if (organisation == null) {
            throw new NoSuchElementException();
        } else {
            //add User to organization
            organisation.getUsers().add(user);
            user.setOrganisation(organisation);
            return organisation;
        }
        
    }

    /**
     *
     * @param organisationId
     * @param user
     * @return updated Organisation bean
     * @throws IllegalArgumentException
     */
    @Transactional
    public Organisation removeUser(int organisationId, PeasyUser user) throws IllegalArgumentException {
        //check bean user is not null
        if (user == null) {
            throw new IllegalArgumentException();
        }
        
        EntityManager entityManager = entitiyManagerProvider.get();
        Organisation organisation = entityManager.find(Organisation.class, organisationId);
        
        if (organisation == null) {
            throw new NoSuchElementException();
        } else {
            //add User to organization
            organisation.getUsers().remove(user);
            user.setOrganisation(null);
            return organisation;
        }
        
    }
}
