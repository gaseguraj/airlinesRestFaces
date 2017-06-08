package edu.mum.cs545.ws;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import cs545.airline.model.Flight;
import cs545.airline.service.FlightService;

@Named
@Path("flight")
public class FlightRest {
	@Inject
	FlightService flightService;

	@GET
	@Path("/")
	public List<Flight> getFlights() {
		return flightService.findAll();
	}
	
	@GET
	@Path("/number/{id}")
	public List<Flight> getFlightById(@PathParam("id") String flightnr) {
		return flightService.findByNumber(flightnr);
	}
	
}
