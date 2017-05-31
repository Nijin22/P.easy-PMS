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

import controllers.ApplicationController;
import controllers.FileController;
import controllers.ProjectController;
import controllers.UserController;

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
        router.GET().route("/forgotPassword").with(ApplicationController::forgotPassword);
        router.GET().route("/myCalender").with(ApplicationController::myCalender);
        router.GET().route("/organization/{id}").with(ApplicationController::organization);

        router.GET().route("/editProject").with(ApplicationController::editProject);
       

        router.GET().route("/projects").with(ApplicationController::projects);
        router.GET().route("/projects/{projectID}").with(ApplicationController::project);
//        router.GET().route("/projects/{projectID}/members").with(ApplicationController::members);
        router.GET().route("/projects/{projectID}/milestones").with(ApplicationController::milestones);
        router.GET().route("/projects/{projectID}/milestones/{milestoneID}").with(ApplicationController::milestone);
        router.GET().route("/projects/{projectID}/report").with(ApplicationController::report);
        router.GET().route("/projects/{projectID}/tasks/{taskID}").with(ApplicationController::task);
        router.GET().route("/projects/{projectID}/tasks").with(ApplicationController::tasks);
        router.GET().route("/upload").with(ApplicationController::fileUpload);

        // Handling Registration / Sessions
        router.GET().route("/register").with(UserController::register);
        router.POST().route("/register").with(UserController::registerAction);
        router.GET().route("/login").with(UserController::login);
        router.POST().route("/login").with(UserController::loginAction);
        router.GET().route("/logout").with(UserController::logout);
       
        //static
    	router.GET().route("/").with(ApplicationController::index);
        router.GET().route("/impress").with(ApplicationController::impress);
        router.GET().route("/information").with(ApplicationController::information); 

        ///////////////////////////////////////////////////////////////////////
        // user uploaded content
        ///////////////////////////////////////////////////////////////////////        
        
        //FILE RESOURCE
        router.POST().route("/uploadFinish/{idOfOwner}").with(FileController::uploadFinish);
        router.GET().route("/download/{fileId}/{type}").with(FileController::downloadFinish);
        //Get beacause <a> link can't do a delete call
        router.GET().route("/delete/{type}/{fileId}/{idOfOwner}").with(FileController::deleteFinish);
        
        //create Master Project -- Get beacause <a> link can't do a delete ca
        router.GET().route("/createProject/{email}").with(ProjectController::createProject);
        router.GET().route("/createMilestone/{projectId}").with(ProjectController::createMilestone);

        
        //AJAX Calls - Project
        router.POST().route("/changeProjectname/{projectId}/newName/{newName}").with(ProjectController::changeProjectname);
        router.POST().route("/changeProjectDescription/{projectId}/newDescription/{description}").with(ProjectController::changeProjectDescription);

        router.POST().route("/changeProject/{projectId}/projectStart/{projectStart}").with(ProjectController::changeProjectStart);
        router.POST().route("/changeProject/{projectId}/projectDeadline/{projectDeadline}").with(ProjectController::changeProjectEnd);
        router.POST().route("/changeProject/{projectId}/projectBudget/{projectBudget}").with(ProjectController::changeProjectBudget);
        
        router.POST().route("/changeProject/{projectId}/deleteMember/{email}").with(ProjectController::deleteUserFromProject);
        router.POST().route("/changeProject/{projectId}/addMember/{email}").with(ProjectController::addUsertoProject);

        router.POST().route("/changeProject/{projectId}/changeManager/{email}").with(ProjectController::changeManager);
      
        router.POST().route("/changeProject/{projectId}/projectStatus/{status}").with(ProjectController::changeStatus);
        
        

        
        //AJAX Calls -Task
        router.POST().route("/changeTaskname/{taskId}/newName/{newName}").with(ProjectController::changeTaskName);
        router.POST().route("/changeTaskDescription/{taskId}/newDescription/{description}").with(ProjectController::changeTaskDescription);

        
        ///////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController::serveWebJars);
        router.GET().route("/assets/{fileName: .*}").with(AssetsController::serveStatic);
        
    }

}