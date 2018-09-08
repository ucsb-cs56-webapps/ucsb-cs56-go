package edu.ucsb.cs56.go;

// SEE: http://sparkjava.com/tutorials/reducing-java-boilerplate

import static spark.Spark.get;
import static spark.Spark.post;
import spark.Route;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collector;

import org.apache.log4j.Logger;

public class BlogService {

	public  static final Logger log = Logger.getLogger(Model.class.getName());
	
    private static final int HTTP_BAD_REQUEST = 400;

	private Model model;

	// Lambda functionss for routes

	Route homePageRoute = (req,res)->{
		String result = "<p>This is a RESTFul API for places</p>\n" + getRoutesAsHTMLTable() + "\n";		
		return result;
	};

	private Route newPlaceRoute =
		(request, response) -> {
		try {
			Place p = json2Place(request.body());
			if (p==null) {
				response.status(HTTP_BAD_REQUEST);
				return "\n";
			}
			
			int id = model.createPlace(p);
			response.status(200);
			response.type("application/json");
			return id+"\n";
		} catch (Exception e) {
			response.status(HTTP_BAD_REQUEST);
			return "\n";
		}
	};

	private Route getAllPlacesRoute = (request, response) -> {
		response.status(200);
		response.type("application/json");
		return dataToJson(model.getAllPlaces()) + "\n";
	};


	private Route getPlaceRoute = (request, response) -> {
		response.status(200);
		response.type("application/json");
		Place p = model.getPlace(request.params(":id"));
		return dataToJson(p) + "\n";				
	};

	private Route deletePlaceRoute = (request, response) -> {
		response.status(200); // TODO: Should it be something else if :id not found?
		response.type("application/json");
		boolean result = model.deletePlace(request.params(":id"));
		return (result?"true":"false")+"\n";
	};

	private Route updatePlaceRoute = (request, response) -> {
		Place p = json2Place(request.body());
		response.status(200); // TODO: Should it be something else if :id not found?
		response.type("application/json");
		Model.PlaceUpdateResult result = model.updatePlace(request.params(":id"),p);
		return dataToJson(result)+"\n";
	};

	
	private ArrayList<RouteEntry> routeEntries = new ArrayList<RouteEntry>();
	
	public void setUpRoutes() {
		for (RouteEntry re : this.routeEntries) {
			log.debug("Registering RouteEntry: " + re.toString());
			if (re.getHttpMethod().equals("GET")) {
				spark.Spark.get(re.getUri(),re.getRoute());
			} else if (re.getHttpMethod().equals("POST")) {
				spark.Spark.post(re.getUri(),re.getRoute());
			} else if (re.getHttpMethod().equals("PUT")) {
				spark.Spark.post(re.getUri(),re.getRoute());				
			} else if (re.getHttpMethod().equals("DELETE")) {
				spark.Spark.delete(re.getUri(),re.getRoute());
			} else {				
				log.error("Route entry has unknown HTTP Method: " + re);
				throw new RuntimeException("Unknown HTTP Method: " + re);
			}			
		}
	}
	
	public BlogService(String databaseUri) {		
		model = new Model(databaseUri);
		this.routeEntries.add(new RouteEntry("GET","/api", homePageRoute, "home page that describes the API"));
		this.routeEntries.add(new RouteEntry("GET","/api/places", getAllPlacesRoute, "get all Places"));
		this.routeEntries.add(new RouteEntry("POST","/api/places", newPlaceRoute, "add a new Place"));
		this.routeEntries.add(new RouteEntry("GET","/api/places/:id", getPlaceRoute, "get Place with id <code>:id</code>"));
		this.routeEntries.add(new RouteEntry("PUT","/api/places/:id",updatePlaceRoute, "update Place with id <code>:id</code>"));
		this.routeEntries.add(new RouteEntry("DELETE","/api/places/:id",deletePlaceRoute, "delete Place with id <code>:id</code>"));
		
		setUpRoutes();
	}

	/**
	   return a HashMap with values of all the environment variables
	   listed; print error message for each missing one, and exit if any
	   of them is not defined.
	*/
	
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
	
	public static Place json2Place(String json) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Place p = mapper.readValue(json, Place.class);
		return p;
	}
	
	public static void main(String[] args) {
		
		HashMap<String,String> envVars =  
			getNeededEnvVars(new String []{
					"MONGODB_USER",
					"MONGODB_PASS",
					"MONGODB_NAME",
					"MONGODB_HOST",
					"MONGODB_PORT"				
				});
		
		String uriString = mongoDBUri(envVars);
		
		spark.Spark.port(getHerokuAssignedPort());
		
		BlogService bs = new BlogService(uriString);
		log.debug("BlogService.main() is finished... listening for requests.");
    }
	
    static int getHerokuAssignedPort() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			if (processBuilder.environment().get("PORT") != null) {
				return Integer.
					parseInt(processBuilder.environment().get("PORT"));
			}
		} catch (Exception e) {
			// fall through to return default port
		}       
        return 4567; // default if PORT isn't set
    }
    	
    public static String dataToJson(Object data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, data);
			return sw.toString();
		} catch (IOException e){
			throw new RuntimeException("IOException from a StringWriter?");
		}
    }

	public String getRoutesAsHTMLTable() {
		String result = "<table>\n" +
			" <thead><tr><th>method</th><th>uri</th><th>description</th></tr><thead>\n" + " <tbody>\n";

		for (RouteEntry re : this.routeEntries) {
			result += "  <tr>\n";
			result += "   <td><code>" + re.getHttpMethod()+ "</code></td>\n";
			result += "   <td><code>" + re.getUri() + "</code></td>\n";
			result += "   <td>" + re.getDescription() + "</td>\n";
			result += "  </tr>\n";
		}
		result += " </tbody>\n</table>\n";

		return result;
	}
}

    
