package edu.ucsb.cs56.go;

import com.fasterxml.jackson.core.JsonParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
	
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.ReturnDocument.AFTER;
import static com.mongodb.client.model.ReturnDocument.BEFORE;
import com.mongodb.client.model.FindOneAndReplaceOptions;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.apache.log4j.Logger;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;

// In a real application you may want to use a DB, for this example we just store the places in memory

public class Model {

	private static Logger log = BlogService.log;
	
    private int nextId = 1;

	private MongoClientURI uri;
    private MongoClient client;
    private MongoDatabase db;

	private MongoCollection<Document> placeCollection;
	
	public Model (String uriString) {
		log.debug("Connecting to MongoDB using uriString="+uriString);
		this.uri = new MongoClientURI(uriString); 
		this.client = new MongoClient(uri);
		this.db = client.getDatabase(uri.getDatabase());
		log.debug("Connected to MongoDB, db="+this.db+" client="+this.client);

		// get a handle to the "places" collection

        placeCollection = this.db.getCollection("places");
 
		
	}
    
	/**
	   Code from <a href="https://stackoverflow.com/questions/32065045/auto-increment-sequence-in-mongodb-using-java">
	   https://stackoverflow.com/questions/32065045/auto-increment-sequence-in-mongodb-using-java</a> to get next
	   sequence number from a MongoDB database.
	*/
	
	public int getNextSequence(String name) throws Exception {
		MongoCollection<Document> collection = db.getCollection("counters");
		collection.updateOne(eq("_id", name), new Document("$inc", new Document("seq", 1)));
		return collection.find(eq("_id", name)).first().getInteger("seq");
	}

	// document.put("_id", getNextSequence("userid"));
    // document.put("name","Sarah C.");
    // collection.insert(document); // insert first doc

	public int createPlace(Place p) throws Exception {
		return createPlace(p.getTitle(),p.getDescription(),p.getCategories());
	}

	public int createPlace(String title, String description, List categories) throws Exception {
		
		int id  = getNextSequence("placeId");
		System.out.println("\n\n\n\n\n******* nextSeq = " + id + "************\n\n\n\n\n");
		
		Place place = new Place();
		place.setId(id);
		place.setTitle(title);
		place.setDescription(description);
		place.setCategories(categories);
		String json = BlogService.dataToJson(place);
		System.out.println("\n\n\n\n\n******* json = " + json + "************\n\n\n\n\\n");
		placeCollection.insertOne(Document.parse(json));
		
		return id;
    }
    
    public List<Place> getAllPlaces(){

		List<Place> result = new ArrayList<Place>();
		
		FindIterable<Document> docsFound = placeCollection.find();

		for (Document cur : docsFound) {
			try {
				String json = cur.toJson();
				log.debug("\n\n\n\n\ncur.toJson()="+json);
				Place p = BlogService.json2Place(json);
				int id = cur.getInteger("id");
				p.setId(id);
				log.debug("Place p="+p);				
				result.add(p);				
			} catch (Exception e ) {
				log.error("Exception="+e);
			}
        }
		
		return result;
    }

    public Place getPlace(String id){
		log.debug("****** getPlace with id=" + id);
		Place result = null;

		try {
			Document cur=placeCollection.find(eq("id", Integer.parseInt(id))).first();
		
			String json = cur.toJson();
			result = BlogService.json2Place(json);
			int id_ = cur.getInteger("id");
			result.setId(id_);
		} catch (Exception e ) {
			log.error("Exception="+e);
		}
		return result;
    }

	public Place doc2Place(Document d) {
		Place place = null;
		try {
			String json = d.toJson();
			place = BlogService.json2Place(json);			
			int id_ = d.getInteger("id");
			place.setId(id_);
		} catch (Exception e) {
			log.debug("ERROR: " + e.toString());
		}
		return place;
	}
	
    public PlaceUpdateResult updatePlace(String id, Place newPlace){
		log.debug("****** updatePlace with id=" + id);
		PlaceUpdateResult result = null;

		try {
			newPlace.setId(Integer.parseInt(id));
			Document replacementDocument =
				Document.parse(BlogService.dataToJson(newPlace));
			FindOneAndReplaceOptions options =
				new FindOneAndReplaceOptions().returnDocument(AFTER);
			Document oldDocument =
				placeCollection.find(eq("id", Integer.parseInt(id))).first();

			Document newDocument =
				placeCollection.findOneAndReplace(eq("id", Integer.parseInt(id)),
												 replacementDocument,
												 options);
			result = new PlaceUpdateResult(doc2Place(oldDocument),doc2Place(newDocument));
			
		} catch (Exception e ) {
			log.error("Exception="+e);
		}
		return result;
    }

	@lombok.Data
	public static class PlaceUpdateResult {
		private Place before;
		private Place after;
		public PlaceUpdateResult(Place before, Place after) {
			this.before = before;
			this.after = after;
		}
	}
	
    public boolean deletePlace(String id){
		log.debug("****** deletePlace with id=" + id);
		boolean result = false;

		try {
			Document cur=
				placeCollection.findOneAndDelete(eq("id", Integer.parseInt(id)));
			result = (cur!=null);
		} catch (Exception e ) {
			log.error("Exception="+e);
			result = false;
		}
		return result;
    }
	
}
