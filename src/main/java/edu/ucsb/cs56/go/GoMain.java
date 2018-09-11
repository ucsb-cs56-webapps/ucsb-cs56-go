package edu.ucsb.cs56.go;

import static spark.Spark.port;

import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Main for Go Webapp
 *
 */

public class GoMain {

	public static final String CLASSNAME="GoMain";
	
	public static final Logger log = Logger.getLogger(CLASSNAME);

	public static void main(String[] args) {

        port(getHerokuAssignedPort());
		
		Map<String, Object> map = new HashMap<String, Object>();
       
		
    // mustache files are  in resources/templates directory
    get("/", (rq, rs) -> new ModelAndView(map, "home.mustache"), new MustacheTemplateEngine());	
	
	get("/places", (rq, rs) -> new ModelAndView(map, "places.mustache"), new MustacheTemplateEngine());	
	
	get("/popular", (rq, rs) -> new ModelAndView(map, "popular.mustache"), new MustacheTemplateEngine());	

	get("/map", (rq, rs) -> new ModelAndView(map, "map.mustache"), new MustacheTemplateEngine());	
	
	}

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

	
}
