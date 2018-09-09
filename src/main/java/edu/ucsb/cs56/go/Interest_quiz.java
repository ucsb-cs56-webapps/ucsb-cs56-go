package edu.ucsb.cs56.pconrad;

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
 * Simple example of using Mustache Templates
 *
 */

public class Interest_quiz {

	public static final String CLASSNAME="SparkMustacheDemo02";

	public static final Logger log = Logger.getLogger(CLASSNAME);

	public static void main(String[] args) {

        port(getHerokuAssignedPort());
    staticFiles.location("templates/public");
		Map map = new HashMap();
        map.put("name", "User");
    // Map map2 = new HashMap();
		//     map2.put("feedback","Hollywood");
        // hello.mustache file is in resources/templates directory
        get("/", (rq, rs) -> new ModelAndView(map, "hello.mustache"), new MustacheTemplateEngine());

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
									quiz_map.put("imgPath", "DisneyLand.jpg");
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
									quiz_map.put("feedback", "Palm Springs Air Trampway");
									quiz_map.put("imgPath", "Palm_Springs_Air_Trampway.jpg");

								}
							}
							quiz_map.put("feedback", "Please follow our instruction for input");
						}
					}
				return new ModelAndView(quiz_map, "feedback.mustache");
			}, new MustacheTemplateEngine());
}

			// new ModelAndView(map, "addedStudent.mustache"), new MustacheTemplateEngine());



    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }


}
