package controllers;

import com.google.inject.Singleton;

import ninja.Result;
import ninja.Results;

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

}
