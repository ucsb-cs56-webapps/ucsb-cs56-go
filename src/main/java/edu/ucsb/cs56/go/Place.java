package edu.ucsb.cs56.go;


import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;
	
/**
   Place is a class that will have getters and
   setters by virtue of Lombok (<a href="https://projectlombok.org/">https://projectlombok.org</a>)
   
   It has int id, String title, List categories, and String description

*/


@Data
class Place {
    private int id;
    private String title;
    private java.util.List categories;
    private String description;
}
