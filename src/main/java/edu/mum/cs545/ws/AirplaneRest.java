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
import cs545.airline.model.Airplane;
import cs545.airline.service.AirplaneService;
import cs545.airline.service.FlightService;

/**
 * WebService RestFul to expose the services for Airplane, this service belongs
 * to the project for the course CSWAA - Orlando Arrocha
 * created on 2017/06/09 
 * 
 * @author      German Segura
 * @version     1.0
 */
@Path("airplane")
public class AirplaneRest {
	/*
	 * Inject the Service AirplaneService
	 */
	@Inject
	private AirplaneService airplaneService;
	@Inject
	private FlightService flightService;

	/**
	 * Return all the Airplane objects
	 * @return List of airplanes
	 */
	@GET
	@Path("/")
	public List<Airplane> getAirlines() {
		return airplaneService.findAll();
	}

	/**
	 * Return Airplane object depending of the value of the parameters
	 * Type and Value. 
	 *
	 * @param  type  Type to find by MODEL or by SERIAL, or FLIGTH "SERIAL"
	 * @param  value Value of the Type, for MODEL and SERIAL is a String 
	 * @return The object Airline
	 */
	@GET
	@Path("/{type}/{value}")
	public List<Airplane> getAirplaneById(@PathParam("type") String type, 
									@PathParam("value") String value) {
		List<Airplane> airplane = new ArrayList<>();
		
		switch(type){
			case "MODEL" : 	airplane = airplaneService.findByModel(value);
				break;
			case "SERIAL": 	airplane.add(airplaneService.findBySrlnr(value));
				break;
			case "FLIGHT": 	airplane = airplaneService.findByFlight(flightService.findByNumber(value).get(0));
				break;
			default		 : 	airplane = null;  
				break;
		}
		return airplane;
	}
	
	/**
	 * Create a new Airplane receiving in JSON format the new object
	 *
	 * @param  airplane	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@POST
	@Path("/")
	public Response createAirplane(Airplane airplane){
		System.out.println("Airplane: " + airplane);
		airplaneService.create(airplane);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Update an airplane, receiving the object airline by JSON format
	 *
	 * @param  clientAirplane	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@PUT
	@Path("/")
	public Response updateAirplane(Airplane clientAirplane){
		System.out.println("Airplane Update: " + clientAirplane);
		Airplane airplane = airplaneService.find(clientAirplane);
		airplane.setCapacity(clientAirplane.getCapacity());
		airplane.setModel(clientAirplane.getModel());
		airplane.setSerialnr(clientAirplane.getSerialnr());
		airplaneService.update(airplane);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Delete an airplane, receiving the ID identifier
	 * DELETE does not allow to receive a body request
	 *
	 * @param  id	Long id 
	 * @return Response OK if is correct
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteAirplane(@PathParam("id") Long id){
		System.out.println("Airplane Delete: " + id);
		Airplane airplaneDel = new Airplane();
		airplaneDel.setId(id);
		airplaneService.delete(airplaneService.find(airplaneDel));
		return Response.status(200).entity("Success").build();
	}
}
