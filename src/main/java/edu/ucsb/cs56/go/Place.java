package edu.ucsb.cs56.go;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
public class Place {
	private string title; 
	private int temperature; 
	private boolean open;   // if false, the place is closed
	private string type;    // ie beach, city, ntl park, etc
}