/**
 * Copyright (C) 2012-2017 the original author or authors.
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
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;

import controllers.ApiController;
import controllers.ApplicationController;
import controllers.ArticleController;
import controllers.FileController;
import controllers.LoginLogoutController;

public class Routes implements ApplicationRoutes {
    
    @Inject
    NinjaProperties ninjaProperties;

    /**
     * Using a (almost) nice DSL we can configure the router.
     * 
     * The second argument NinjaModuleDemoRouter contains all routes of a
     * submodule. By simply injecting it we activate the routes.
     * 
     * @param router
     *            The default router of this application
     */
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
        
        
        //Fileupload htmlside for testing
        router.GET().route("/upload").with(ApplicationController::fileUpload);
        router.POST().route("/uploadFinish").with(FileController::uploadFinish);
        router.GET().route("/download/{fileId}").with(FileController::downloadFinish);


       
        //static
    	router.GET().route("/").with(ApplicationController::index);
        router.GET().route("/impress").with(ApplicationController::impress);
        router.GET().route("/information").with(ApplicationController::information);

		///////////////////////////////////////////////////////////////////////
		//p.easy Resources GET
		///////////////////////////////////////////////////////////////////////        
        //Routing
//        router.GET().route("/hello_world.json").with(ApplicationController::helloWorldJson);

        
    	///////////////////////////////////////////////////////////////////////
        //p.easy Resources POST
        ///////////////////////////////////////////////////////////////////////     
        //Routing        
        
        // puts test data into db:
        if (!ninjaProperties.isProd()) {
            router.GET().route("/setup").with(ApplicationController::setup);
        }
        
        ///////////////////////////////////////////////////////////////////////
        // Login / Logout
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/login").with(LoginLogoutController::login);
        router.POST().route("/login").with(LoginLogoutController::loginPost);
        router.GET().route("/logout").with(LoginLogoutController::logout);
        
        ///////////////////////////////////////////////////////////////////////
        // Create new article
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/article/new").with(ArticleController::articleNew);
        router.POST().route("/article/new").with(ArticleController::articleNewPost);
        
        ///////////////////////////////////////////////////////////////////////
        // Create new article
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/article/{id}").with(ArticleController::articleShow);

        ///////////////////////////////////////////////////////////////////////
        // Api for management of software
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/api/{username}/articles.json").with(ApiController::getArticlesJson);
        router.GET().route("/api/{username}/article/{id}.json").with(ApiController::getArticleJson);
        router.GET().route("/api/{username}/articles.xml").with(ApiController::getArticlesXml);
        router.POST().route("/api/{username}/article.json").with(ApiController::postArticleJson);
        router.POST().route("/api/{username}/article.xml").with(ApiController::postArticleXml);
 
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
