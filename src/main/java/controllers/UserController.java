package controllers;

import java.util.Optional;

import com.google.inject.Singleton;

import ninja.Result;
import ninja.Results;
import ninja.params.Param;

@Singleton
public class UserController {
	
	/**
	 * Displaying the site register form
	 * @return
	 */
	public Result register(){
		Result result = Results.html();
		
		return result;
	}
	
	/**
	 * Handle the filled register form
	 * @return
	 */
	public Result registerAction(
			@Param("first_name") Optional<String> firstName){
		
		return Results.html();
	}

}
