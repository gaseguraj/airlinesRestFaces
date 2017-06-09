package edu.mum.cs545.ws;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.eclipse.persistence.oxm.MediaType;

import cs545.airline.model.Airline;
import cs545.airline.service.AirlineService;

@Named
@Path("airline")

public class AirlineRest {

	@Inject
	private AirlineService airlineService;

	@GET
	@Path("/")
	public List<Airline> getAirlines() {
			return airlineService.findAll();
	}
	
	@GET
	@Path("/{id}")
	public Airline getAirlineById(@PathParam("id") Long id) {
				return airlineService.findById(id);
	}
	
//	@POST
//	@Path("/{name}")
//	public String createAirline(@PathParam("name") String name){
//		Airline airline = new Airline(name);
//		airlineService.create(airline);
//		return "Ok";
//	}
	
	@POST
	@Path("/")
	public Response createAirline(Airline airline){
		System.out.println("Airline: " + airline.getName());
		airlineService.create(airline);
		return Response.status(200).entity("Success").build();
	}
	
	@PUT
	@Path("/")
	public Response updateAirline(Airline airlineUpdate){
		System.out.println("Airline Update: " + airlineUpdate.getName() +  " ID: " + airlineUpdate.getId());
		Airline airline = airlineService.findById(airlineUpdate.getId());
		airline.setName(airlineUpdate.getName());
		airlineService.update(airline);
		return Response.status(200).entity("Success").build();
	}
	
	@PUT
	@Path("/{id}")
	public String updateAirlineById(@PathParam("id") Long id){
		airlineService.update(airlineService.findById(id));
		return "Ok";
	}
	
	@DELETE
	@Path("/{id}")
	public String deleteAirline(@PathParam("id") Long id){
		airlineService.delete(airlineService.findById(id));
		return "Ok";
	}
	
}
