package edu.mum.cs545.ws;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import cs545.airline.model.Airline;
import cs545.airline.service.AirlineService;

/**
 * WebService RestFul to expose the services for Airline, this service belongs
 * to the project for the course CSWAA - Orlando Arrocha 
 * created on 2017/06/09 
 * 
 * @author      German Segura
 * @version     1.0
 */

@Path("airline")
public class AirlineRest {

	/*
	 * Inject the Service AirlineService
	 */
	@Inject
	private AirlineService airlineService;

	/**
	 * Return all the Airline objects
	 * @return List of airlines
	 */
	@GET
	@Path("/")
	public List<Airline> getAirlines() {
		return airlineService.findAll();
	}

	/**
	 * Return Airline object depending of the value of the parameters
	 * Type and Value. 
	 *
	 * @param  type  Type to find by NAME or by ID
	 * @param  value Value of the Type, for NAME is a String and for
	 * 				 ID is a String converted to Long
	 * @return The object Airline
	 */
	@GET
	@Path("/{type}/{value}")
	public Airline getAirlineById(	@PathParam("type") String type, 
									@PathParam("value") String value) {
		Airline airline;
		switch(type){
			case "ID"	: 	airline = airlineService.findById(Long.valueOf(value));
				break;
			case "NAME"	: 	airline = airlineService.findByName(value);
				break;
			default		: 	airline = null;  
				break;
		}
		return airline;
	}
	
	/**
	 * Create a new Airline receiving in JSON format the new object
	 *
	 * @param  airline	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@POST
	@Path("/")
	public Response createAirline(Airline airline){
		System.out.println("Airline: " + airline.getName());
		airlineService.create(airline);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Update an airline, receiving the object airline by JSON format
	 *
	 * @param  clientAirline	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@PUT
	@Path("/")
	public Response updateAirline(Airline clientAirline){
		System.out.println("Airline Update: " + clientAirline.getName() +  " ID: " + clientAirline.getId());
		Airline airline = airlineService.findById(clientAirline.getId());
		airline.setName(clientAirline.getName());
		airlineService.update(airline);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Delete an airline, receiving the ID identifier
	 * DELETE does not allow to receive a body request
	 *
	 * @param  id	Long id 
	 * @return Response OK if is correct
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteAirline(@PathParam("id") Long id){
		System.out.println("Airline Delete: " + id);
		Airline airline = airlineService.findById(id);
		airlineService.delete(airline);
		return Response.status(200).entity("Success").build();
	}
	
}
