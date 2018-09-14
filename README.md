# ucsb-cs56-go
Travel recommendations based on real time data for weather, traffic, and crowd. 


# Names
* Sasha
* Xinyi
* Angie
* Sean
* Sarita
* Siqi

# Agreements
* Communicate! If you don't know how to do something/if you feel in over your head/if you feel like can't pull your own weight, Communicate! 
* Respect each others ideas
* Be present, show up, don't bail! 


# Who is this app for? 
* PERSON A: Already knows what they want to do 
   and want a convenient way to check weather/driving distances 
   for multiple locations at once.
* PERSON B: People who are looking for convenient travel 
   suggestions but do not care about where they go.


# What we want to include:

HOMEPAGE: 

List of activities to choose from (including custom search if activity is not listed) for Person A
OR Questionnaire for people who do not know what they want to do for Person B 

AFTER GENERATING LIST OF ACTIVITIES:

option to filter results based on 
* Weather
* Driving/walking distance from location (May use Google Maps API) 
* Indoor or Outdoor
* Price
* Crowds
* Location (you are here) 
* Campsite availability? 

What can be included later: 
* Reviews/rating
* Places outside of california/international
* A “What’s nearby?” widget

# Current steps to compile and run application 

# 1. Create the database with mLab and MongoDB

* Go into mLab and create a new database name go.
* In the repository, cp env.sh.EXAMPLE env.sh in order to make an env.sh file.
* In the go database create a new user and record the username and password into the env.sh file.
* Also record the name of database, host name, and port name which can be found in the go database page.
* In the database, create two collections (counters and places). 
* Add document in counters and add this code below
'''
{
    "_id": "placeId",
    "seq": 0
}
'''
* Your places collection should be empty.

# 2. Add data to the database  
* Go into the go repository. Type
'''
. env.sh
'''
in command line
* Type mvn compile; mvn exec:java
* In a separate tab in command line, go into the testdata file (in the go repository)
* Typing curl http://localhost:4567/api/places should give you [ ] 
* Typing curl -d @place1.json http://localhost:4567/api/places should add the first place to the database
* Now when you type curl http://localhost:4567/api/places again, the first place and its id should show up. If you go into your places collection in your mLab database, you should see the first place. 
* Now redo curl -d but with place2 and place3

# 3. Run the application  
* If you type localhost:4567/api/places in your web browser's search bar, you should see the list of places that you have added
* If you type localhost:4567/ in the search bar and click on the places tab in the nav bar, you should see the list of places in a more readable format

For more detailed tutorials on databases, visit these repos: 
https://github.com/ucsb-cs56-pconrad/sparkjava-rest-mlab-frontend
https://github.com/ucsb-cs56-pconrad/sparkjava-mongodb-mlab-tutorial
https://github.com/ucsb-cs56-pconrad/sparkjava-rest-with-mlab





