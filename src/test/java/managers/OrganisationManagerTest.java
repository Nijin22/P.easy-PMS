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
import java.lang.IllegalArgumentException;
import java.security.GeneralSecurityException;
import javax.persistence.EntityManager;
import models.User;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.NinjaTest;
import ninja.jpa.UnitOfWork;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Tugrul
 */
public class OrganisationManagerTest extends NinjaTest {
    private static Logger log =null;
    @Inject
    Provider<EntityManager> entitiyManagerProvider;
    
    public OrganisationManagerTest() {
    }
    
    @Before
    public void setUp() {
    //initialize Logger to log Test
    log = Logger.getLogger( OrganisationManagerTest.class.getName() );
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
    
    @Test
    public void testCreateOrganisation() throws GeneralSecurityException, UserAlreadyExistsException {
       try{
       OrganisationManager om = getInjector().getInstance(OrganisationManager.class);
       UserManager um = getInjector().getInstance(UserManager.class);
      
       log.info("Start Testing Succesfull Test: createOrganisation");
     
       PeasyUser user = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
       Organisation createdOrg = om.createOrganisation("PeasyOrg",user);
       
       assertEquals(createdOrg.getName(), "PeasyOrg");
       }catch(GeneralSecurityException| UserAlreadyExistsException e){
           fail(e.getMessage() + " " + e.getStackTrace());
       }
       
    }
  
    /**
     * Test of renameOrganisation method, of class OrganisationManager.
     */
    @Ignore
    @Test
    public void testRenameOrganisation() {
        System.out.println("renameOrganisation");
        int organisationId = 0;
        String newName = "";
        OrganisationManager instance = new OrganisationManager();
        Organisation expResult = null;
        Organisation result = instance.renameOrganisation(organisationId, newName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changeOrganisationManager method, of class OrganisationManager.
     */
    @Ignore
    @Test
    public void testChangeOrganisationManager() {
        System.out.println("changeOrganisationManager");
        int organisationId = 0;
        PeasyUser newAdmin = null;
        OrganisationManager instance = new OrganisationManager();
        Organisation expResult = null;
        Organisation result = instance.changeOrganisationManager(organisationId, newAdmin);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addUser method, of class OrganisationManager.
     */
    @Ignore
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        int organisationId = 0;
        PeasyUser user = null;
        OrganisationManager instance = new OrganisationManager();
        Organisation expResult = null;
        Organisation result = instance.addUser(organisationId, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeUser method, of class OrganisationManager.
     */
    @Ignore
    @Test
    public void testRemoveUser() {
        System.out.println("removeUser");
        int organisationId = 0;
        PeasyUser user = null;
        OrganisationManager instance = new OrganisationManager();
        instance.removeUser(organisationId, user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
