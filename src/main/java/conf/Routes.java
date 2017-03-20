/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;


import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import controllers.ApplicationController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
      
    	///////////////////////////////////////////////////////////////////////
        //p.easy templates GET
        ///////////////////////////////////////////////////////////////////////          
        //dynamic 
        router.GET().route("/account").with(ApplicationController::account);
        router.GET().route("/dashboard").with(ApplicationController::dashboard);
        router.GET().route("/login").with(ApplicationController::login);
        router.GET().route("/register").with(ApplicationController::register);
        router.GET().route("/forgotPassword").with(ApplicationController::forgotPassword);
        router.GET().route("/myCalender").with(ApplicationController::myCalender);
        router.GET().route("/organization").with(ApplicationController::organization);
       
        router.GET().route("/projects").with(ApplicationController::projects);
        router.GET().route("/projects/{projectID}").with(ApplicationController::project);
        router.GET().route("/projects/{projectID}/members").with(ApplicationController::members);
        router.GET().route("/projects/{projectID}/milestones").with(ApplicationController::milestones);
        router.GET().route("/projects/{projectID}/report").with(ApplicationController::report);
        router.GET().route("/projects/{projectID}/tasks/{taskID}").with(ApplicationController::task);
        router.GET().route("/projects/{projectID}/tasks").with(ApplicationController::tasks);
       
        //static
    	router.GET().route("/").with(ApplicationController::index);
        router.GET().route("/impress").with(ApplicationController::impress);
        router.GET().route("/information").with(ApplicationController::information);

		///////////////////////////////////////////////////////////////////////
		//p.easy Resources GET
		///////////////////////////////////////////////////////////////////////        
        //Routing
        router.GET().route("/hello_world.json").with(ApplicationController::helloWorldJson);

        
    	///////////////////////////////////////////////////////////////////////
        //p.easy Resources POST
        ///////////////////////////////////////////////////////////////////////     
        //Routing        
 
    	///////////////////////////////////////////////////////////////////////
        //p.easy Resources PUT
        ///////////////////////////////////////////////////////////////////////  
        //Routing
        
    	///////////////////////////////////////////////////////////////////////
        //p.easy Resources DELETE
        ///////////////////////////////////////////////////////////////////////   
        //Routing
        
        ///////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController::serveWebJars);
        router.GET().route("/assets/{fileName: .*}").with(AssetsController::serveStatic);
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/.*").with(ApplicationController::index);
    }

}
