package edu.mum.cs545.ws;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import cs545.airline.model.Airline;
import cs545.airline.model.Airplane;
import cs545.airline.model.Airport;
import cs545.airline.model.Flight;
import cs545.airline.service.AirlineService;
import cs545.airline.service.AirplaneService;
import cs545.airline.service.AirportService;
import cs545.airline.service.FlightService;

@Named
@Path("flight")
public class FlightRest {
	@Inject
	private FlightService flightService;
	@Inject
	private AirlineService airlineService;
	@Inject
	private AirportService airportService;
	@Inject
	private AirplaneService airplaneService;

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
	
	@GET
	@Path("/airline/{name}")
	public List<Flight> getFlightByAirline(@PathParam("name") String name) {
		Airline airline;
		System.out.println("Flight Name: " + name  );
		airline = airlineService.findByName(name);
		return flightService.findByAirline(airline);
	}
	
	@GET
	@Path("/airport_origin_code/{code}")
	public List<Flight> getFlightByOriginCode(@PathParam("code") String code) {
		Airport airport;
		System.out.println("Airpot Origin Code: " + code  );
		airport = airportService.findByCode(code);
		return flightService.findByOrigin(airport);
	}
	
	@GET
	@Path("/airport_destiny_code/{code}")
	public List<Flight> getFlightByDestinyCode(@PathParam("code") String code) {
		Airport airport;
		System.out.println("Airpot Destiny Code: " + code  );
		airport = airportService.findByCode(code);
		return flightService.findByDestination(airport);
	}
	
	@GET
	@Path("/arrival_date/{datefrom}/{dateto}")
	public List<Flight> getFlightByDestinyCode(
			@PathParam("datefrom") String dateFrom,
			@PathParam("dateto") String dateTo) {
		
		System.out.println("Date From: " + dateFrom + " DateTo: " + dateTo);
		try{
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); 
			Date dateFromDate = (Date)formatter.parse(dateFrom + " 00:00:00"); 
			Date dateToDate = (Date)formatter.parse(dateTo + " 23:59:59");
			
			return flightService.findByArrivalBetween(dateFromDate, dateToDate);
		}catch(ParseException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("/airplane_model/{model}")
	public List<Flight> getFlightByAirplaneModel(@PathParam("model") String model) {
		List<Airplane> listAirplane = null;
		List<Flight> listFlights = null;
		System.out.println("Airplane Model: " + model );
		listAirplane = airplaneService.findByModel(model);
		
		for(Airplane airplane : listAirplane){
			listFlights = flightService.findByArrival(airplane);
		}
			
			
		
				
		return listFlights;
	}
	
}