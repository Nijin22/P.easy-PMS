/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import java.util.List;
import java.util.Map;

import models.Article;
import ninja.Result;
import ninja.Results;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.ArticleDao;
import dao.SetupDao;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.org.objectweb.asm.TypeReference;
import models.manager.FileManager;
import ninja.Context;
import ninja.Renderable;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import ninja.uploads.MemoryFileItemProvider;
import ninja.utils.ResponseStreams;
import static org.eclipse.jetty.http.HttpParser.LOG;

public class ApplicationController {

    @Inject
    ArticleDao articleDao;

    @Inject
    SetupDao setupDao;
       
    @Inject
    FileManager fileManager;
    
    private static final Logger LOG = Logger.getLogger(ApplicationController.class.getName() );


    public ApplicationController() {

    }

    public Result account() {
        return Results.html();

    }

    public Result dashboard() {
        return Results.html();

    }

    public Result impress() {
        return Results.html();

    }

    public Result information() {
        return Results.html();
    }

    public Result login() {
        return Results.html();

    }

    public Result register() {
        return Results.html();
    }

    public Result forgotPassword() {
        return Results.html();

    }

    public Result members() {
        return Results.html();

    }

    public Result milestones() {
        return Results.html();

    }

    public Result myCalender() {
        return Results.html();

    }

    public Result organization() {
        return Results.html();

    }

    public Result project() {
        return Results.html();

    }

    public Result projects() {
        return Results.html();

    }

    public Result report() {
        return Results.html();

    }

    public Result tasks() {
        return Results.html();

    }

    public Result task() {
        return Results.html();

    }

    public Result fileUpload() {
        return Results.html();

    }

    @FileProvider(DiskFileItemProvider.class)
    public Result uploadFinish(Context context) throws Exception {
        LOG.info("Start method");
        //get parameters
        FileItem upfile = context.getParameterAsFileItem("upfile");
        String type = context.getParameter("type");
        String path = fileManager.uploadFile(upfile,type);
        
        LOG.log(Level.INFO, "Uploaded File in following Path: {0}", path); 
        
        return Results.redirect("/upload"); 
    }

    public Result downloadFinish(@PathParam("fileId") String id) {
        LOG.info("Start method");
        
                
        Renderable renderable = new Renderable() { 

        @Override 
        public void render(Context context, Result result) { 
          ResponseStreams responseStreams = context.finalizeHeaders(result); 
            try {
                OutputStream outputstream = responseStreams.getOutputStream(); // do what you want with the streams
                LOG.log(Level.INFO, "Result: {0} stream: {1}", new Object[]{result, outputstream});
                fileManager.downloadFile(outputstream,id);       
            
            } catch (IOException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
          } 
        }; 
      //  return new Results.ok.render(renderable); 
        return Results.redirect("/upload").render(renderable);

        
    }
    
    /**
     * Method to put initial data in the db...
     *
     * @return
     */
    public Result setup() {

        setupDao.setup();

        return Results.ok();

    }

    public Result index() {

        Article frontPost = articleDao.getFirstArticleForFrontPage();

        List<Article> olderPosts = articleDao.getOlderArticlesForFrontPage();

        Map<String, Object> map = Maps.newHashMap();
        map.put("frontArticle", frontPost);
        map.put("olderArticles", olderPosts);

        return Results.html().render("frontArticle", frontPost)
                .render("olderArticles", olderPosts);

    }
}
