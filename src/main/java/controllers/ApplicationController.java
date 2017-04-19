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

public class ApplicationController {

    @Inject
    ArticleDao articleDao;

    @Inject
    SetupDao setupDao;
    public ApplicationController() {

    }
    
    public Result account() {
        return Results.html();

    }
    
    public Result dashboard() {        return Results.html();

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
    
//    public Result helloWorldJson() {
//        
//        SimplePojo simplePojo = new SimplePojo();
//        simplePojo.content = "Simple JSON test!";
//        
//    }

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
