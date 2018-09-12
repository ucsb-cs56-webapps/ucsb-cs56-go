package edu.ucsb.cs56.go;

import static spark.Spark.port;

import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

/**
 * Main for Go Webapp
 *
 */

public class GoMain {

	public static final String CLASSNAME="GoMain";
	
	public static final Logger log = Logger.getLogger(CLASSNAME);

	public static HashMap<String,String> getNeededEnvVars(String [] neededEnvVars) {
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		HashMap<String,String> envVars = new HashMap<String,String>();
		
		boolean error=false;		
		for (String k:neededEnvVars) {
			String v = processBuilder.environment().get(k);
			if ( v!= null) {
				envVars.put(k,v);
			} else {
				error = true;
				System.err.println("Error: Must define env variable " + k);
			}
		}
		
		if (error) { System.exit(1); }
		
		System.out.println("envVars=" + envVars);
		return envVars;	 
	}

	public static String mongoDBUri(HashMap<String,String> envVars) {
		
		System.out.println("envVars=" + envVars);
		
		// mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
		String uriString = "mongodb://" +
			envVars.get("MONGODB_USER") + ":" +
			envVars.get("MONGODB_PASS") + "@" +
			envVars.get("MONGODB_HOST") + ":" +
			envVars.get("MONGODB_PORT") + "/" +
			envVars.get("MONGODB_NAME");
		System.out.println("uriString=" + uriString);
		return uriString;
	}


	public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFiles.location("templates/public");
		
		HashMap<String,String> envVars =  
			getNeededEnvVars(new String []{
					"MONGODB_USER",
					"MONGODB_PASS",
					"MONGODB_NAME",
					"MONGODB_HOST",
					"MONGODB_PORT"				
				});
		String uriString = mongoDBUri(envVars);
		BlogService bs = new BlogService(uriString);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
       
		
    // mustache files are  in resources/templates directory
    get("/", (rq, rs) -> new ModelAndView(map, "home.mustache"), new MustacheTemplateEngine());	
	
	get("/places", (rq, rs) -> new ModelAndView(map, "places.mustache"), new MustacheTemplateEngine());	
	
	get("/popular", (rq, rs) -> new ModelAndView(map, "popular.mustache"), new MustacheTemplateEngine());	

	get("/map", (rq, rs) -> new ModelAndView(map, "map.mustache"), new MustacheTemplateEngine());

	get("/fill_quiz", (rq, rs) -> new ModelAndView(map, "fill_quiz.mustache"), new MustacheTemplateEngine());

		post("/feedback", (rq, rs) ->
			{
				Map quiz_map = new HashMap();
				if((rq.queryParams("q1")).equals("NC")){
					if((rq.queryParams("q2")).equals("close")){
						if((rq.queryParams("q3")).equals("cheap")){
							if((rq.queryParams("q4")).equals("urban")){
								quiz_map.put("feedback", "Santa Cruz Boardwalk");
								quiz_map.put("imgPath", "Santa_Cruz_Boardwalk.jpg");
							}
							else{
								quiz_map.put("feedback", "Mission Peak Hiking Trail");
								quiz_map.put("imgPath", "Mission_Peak_Hiking_Trail.jpg");

							}
						}
						else{
							if((rq.queryParams("q4")).equals("urban")){
								quiz_map.put("feedback", "Monterey Bay Aquarium");
								quiz_map.put("imgPath", "Monterey_Bay_Aquarium.jpg");
							}
							else{
								quiz_map.put("feedback", "Yosemite Valley Camping");
								quiz_map.put("imgPath", "Yosemite_Valley_Camping.jpg");
							}
						}
					}
						else{
							if((rq.queryParams("q3")).equals("cheap")){
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "Golden Gate Bridge");
									quiz_map.put("imgPath", "Golden_Gate_Bridge.jpg");
								}
								else{
									quiz_map.put("feedback", "Japanese Tea Garden");
									quiz_map.put("imgPath", "Japanese_Tea_Garden.jpg");
								}
							}
							else{
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "Ice Cream Museum");
									quiz_map.put("imgPath", "Ice_Cream_Museum.jpg");
								}
								else{
									quiz_map.put("feedback", "Lake Tahoe Camping");
									quiz_map.put("imgPath", "Lake_Tahoe_Camping.jpg");
								}

							}
					}
				}
					else{
						if((rq.queryParams("q2")).equals("close")){
							if((rq.queryParams("q3")).equals("cheap")){
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "Hollywood Sign");
									quiz_map.put("imgPath", "Hollywood.jpg");
								}
								else{
									quiz_map.put("feedback", "Malibu Beach");
									quiz_map.put("imgPath", "Malibu_Beach.jpg");
								}
							}
							else{
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "DisneyLand");
									quiz_map.put("imgPath", "disneyland.jpg");
								}
								else{
									quiz_map.put("feedback", "Santa Catalina Island");
									quiz_map.put("imgPath", "Santa_Catalina_Island.jpg");
								}
							}
						}
						else{
							if((rq.queryParams("q3")).equals("cheap")){
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "Palm Springs Air Museum");
									quiz_map.put("imgPath", "Palm_Springs_Air_Museum.jpg");
								}
								else{
									quiz_map.put("feedback", "Joshua Tree National Park");
									quiz_map.put("imgPath", "Joshua_Tree_National_Park.jpg");
								}
							}
							else{
								if((rq.queryParams("q4")).equals("urban")){
									quiz_map.put("feedback", "San Diego SeaWorld");
									quiz_map.put("imgPath", "San_Diego_SeaWorld.jpg");
								}
								else{
									quiz_map.put("feedback", "Palm Springs Air Tramway");
									quiz_map.put("imgPath", "Palm_Springs_Air_Tramway.jpg");

								}
							}

						}
					}
					if(quiz_map.get("feedback").equals("Palm Springs Air Tramway")){
					quiz_map.put("feedback", "Please follow our instruction for input");
					quiz_map.put("imgPath", "Error.jpg");
				}
				return new ModelAndView(quiz_map, "feedback.mustache");
			}, new MustacheTemplateEngine());	
	
	log.debug("GoMain.main() is finished... listening for requests.");
	}

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

	
}
