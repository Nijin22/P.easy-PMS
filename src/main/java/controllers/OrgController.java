package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import filters.LoginFilter;
import models.manager.OrganisationManager;
import models.manager.UserManager;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

@Singleton
@FilterWith(LoginFilter.class)
public class OrgController {

	@Inject
	private OrganisationManager organisationManager;
	@Inject
	private UserManager userManager;
	
	public Result removeUserFromOrg(@PathParam("orgId") String orgId,@PathParam("email") String email){
		organisationManager.removeUser(Integer.parseInt(orgId), email);
		return Results.redirect("/organisation/" + orgId);
	}
	
	public Result editUserInOrg(@PathParam("orgId") String orgId,@PathParam("email") String email,@PathParam("firstName") String firstName,@PathParam("lastName") String lastName){
		userManager.updateUserFromOrg(email, firstName, lastName);
		return Results.redirect("/organisation/" + orgId);
	}
	
}
