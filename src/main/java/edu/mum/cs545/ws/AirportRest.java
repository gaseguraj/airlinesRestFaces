package edu.mum.cs545.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import cs545.airline.model.Airport;
import cs545.airline.service.AirportService;
import cs545.airline.service.FlightService;

/**
 * WebService RestFul to expose the services for Airport, this service belongs
 * to the project for the course CSWAA - Orlando Arrocha
 * created on 2017/06/09 
 * 
 * @author      German Segura
 * @version     1.0
 */
@Path("airport")
public class AirportRest {
	/*
	 * Inject the Service AirplaneService
	 */
	@Inject
	private AirportService airportService;
	@Inject
	private FlightService flightService;

	/**
	 * Return all the Airport objects
	 * @return List of airports
	 */
	@GET
	@Path("/")
	public List<Airport> getAirport() {
		return airportService.findAll();
	}

	/**
	 * Return Airport object depending of the value of the parameters
	 * Type and Value. 
	 *
	 * @param  type  Type to find by CITY or by COUNTRY, NAME, CODE; 
	 * 				 DEPARTURE and ARRIVAL by number of the flight
	 * @param  value Value of the Type CITY, COUNTRY, NAME, DEPARTURE and ARRIVAL String 
	 * @return The List Airports
	 */
	@GET
	@Path("/{type}/{value}")
	public List<Airport> getAirportBy(@PathParam("type") String type, 
									  @PathParam("value") String value) {
		List<Airport> airport = new ArrayList<>();
		
		switch(type){
			case "CITY"		: 	airport = airportService.findByCity(value);
				break;
			case "COUNTRY"	: 	airport = airportService.findByCountry(value);
				break;
			case "NAME"		: 	airport = airportService.findByName(value);
				break;
			case "CODE"		: 	airport.add(airportService.findByCode(value));
				break;
			case "DEPARTURE": 	airport = airportService.findByDeparture(flightService.findByNumber(value).get(0));
				break;
			case "ARRIVAL"	: 	airport = airportService.findByArrival(flightService.findByNumber(value).get(0));
				break;
			default		 : 	airport = null;  
				break;
		}
		return airport;
	}
	
	/**
	 * Create a new Airport receiving in JSON format the new object
	 *
	 * @param  airport	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@POST
	@Path("/")
	public Response createAirport(Airport airport){
		System.out.println("Airport: " + airport);
		airportService.create(airport);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Update an airport, receiving the object airline by JSON format
	 *
	 * @param  clientAirport	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@PUT
	@Path("/")
	public Response updateAirport(Airport clientAirport){
		System.out.println("Airport Update: " + clientAirport);
		Airport airport = airportService.find(clientAirport);
		airport.setAirportcode(clientAirport.getAirportcode());
		airport.setCity(clientAirport.getCity());
		airport.setCountry(clientAirport.getCountry());
		airport.setName(clientAirport.getName());
		airportService.update(airport);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Delete an airport, receiving the ID identifier
	 * DELETE does not allow to receive a body request
	 *
	 * @param  id	Long id 
	 * @return Response OK if is correct
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteAirport(@PathParam("id") Long id){
		System.out.println("Airport Delete: " + id);
		Airport airportDel = new Airport();
		airportDel.setId(id);
		airportService.delete(airportService.find(airportDel));
		return Response.status(200).entity("Success").build();
	}
}
