/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.logging.Logger;
import models.beans.Organisation;
import models.beans.PeasyUser;
import models.manager.OrganisationManager;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.NinjaTest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tugrul
 */
public class OrganisationManagerTest extends NinjaTest {

    private static Logger log = null;
    @Inject
    Provider<EntityManager> entitiyManagerProvider;

    public OrganisationManagerTest() {
    }

    @Before
    public void setUp() {
        //initialize Logger to log Test
        log = Logger.getLogger(OrganisationManagerTest.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrganisationException1() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 1: name is null");
        om.createOrganisation("", new PeasyUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrganisationException2() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 2: PeasyUser is null");
        om.createOrganisation("PeasyOrg", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrganisationNameException1() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 1: name is null");
        om.renameOrganisation(111, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOrganisationNameException2() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 2: organisation does not exist in database");
        om.renameOrganisation(111, "orgname");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrganisationManagerException1() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 1: Manager is null");
        om.changeOrganisationManager(111222333, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOrganisationManagerException2() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 2: organisation does not exist in database");
        om.renameOrganisation(111222333, "orgname");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrganisationUserAddException1() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 1: User is null");
        om.addUser(111222333, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOrganisationUserAddException2() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 2: organisation does not exist in database");
        om.addUser(111222333, new PeasyUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrganisationUserRemoveException1() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 1: User is null");
        om.removeUser(111222333, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOrganisationUserRemoveException2() {
        OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
        log.info("Start Testing Fail Test 2: organisation does not exist in database");
        om.removeUser(111222333, new PeasyUser());
    }

    @Test
    public void testCreateUpadateOrganisation() throws GeneralSecurityException, UserAlreadyExistsException {
        try {
            OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
            UserManager um = getInjector().getInstance(UserManager.class);

            log.info("Start Testing Succesfull Test: createUpadteOrganisation");

            //Test Create Organisation
            PeasyUser admin = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
            Organisation createdOrg = om.createOrganisation("PeasyOrg", admin);
            assertEquals(createdOrg.getName(), "PeasyOrg");

            //Test Update Organisationsname
            Organisation updatedOrgName = om.renameOrganisation(createdOrg.getOrganisationId(), "PeasyOrgNewName");
            assertEquals(updatedOrgName.getName(), "PeasyOrgNewName");

            //Test Update Organisationsname
            PeasyUser newAdmin = um.createUser("engin@peasy.de", "Engin", "Mengin", "123456789");
            Organisation updatedOrgManger = om.changeOrganisationManager(createdOrg.getOrganisationId(), newAdmin);
            assertEquals(updatedOrgManger.getName(), "PeasyOrgNewName");

            //Test Add user to Organisation
            Organisation updatedOrgUserAdd = om.addUser(createdOrg.getOrganisationId(), admin);
            assertTrue(updatedOrgUserAdd.getUsers().contains(admin));

            //Test Add user to Organisation
            Organisation updatedOrgUserRemove = om.removeUser(createdOrg.getOrganisationId(), admin);
            assertFalse(updatedOrgUserRemove.getUsers().contains(admin));

        } catch (GeneralSecurityException | UserAlreadyExistsException e) {
            fail(e.getMessage());
        }

    }
}
