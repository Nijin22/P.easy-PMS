package filters;

import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.Results;

/**
 * Only allows users who are already logged in.
 * @author Dennis
 *
 */
public class LoginFilter implements Filter {

	@Override
	public Result filter(FilterChain chain, Context context) {
		if(context.getSession() == null || context.getSession().get("email") == null){
			// No session or not logged in
			context.getFlashScope().error("filter.notLoggedIn");
			return Results.forbidden().html().template("/views/system/403forbidden.ftl.html");
			
		} else {
			return chain.next(context);
		}
	}

}
