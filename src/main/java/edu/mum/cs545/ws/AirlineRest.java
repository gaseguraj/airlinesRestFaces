package edu.mum.cs545.ws;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

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
	
	@POST
	@Path("/")
	public String createAirline(@QueryParam("name") String name){
		Airline airline = new Airline(name);
		
		airlineService.create(airline);
		return "Ok";
	}
}
