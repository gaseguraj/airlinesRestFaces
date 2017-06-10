package edu.mum.cs545.ws;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cs545.airline.model.Airport;
import cs545.airline.model.Flight;
import cs545.airline.service.AirlineService;
import cs545.airline.service.AirplaneService;
import cs545.airline.service.AirportService;
import cs545.airline.service.FlightService;

/**
 * WebService RestFul to expose the services for Flight, this service belongs to
 * the project for the course CSWAA - Orlando Arrocha created on 2017/06/09
 * 
 * @author German Segura
 * @version 1.0
 */
@Path("flight")
public class FlightRest {

	/*
	 * Inject the Service
	 */
	@Inject
	private FlightService flightService;
	@Inject
	private AirlineService airlineService;
	@Inject
	private AirportService airportService;
	@Inject
	private AirplaneService airplaneService;

	/**
	 * Return all the Airport objects
	 * 
	 * @return List of airports
	 */
	@GET
	@Path("/")
	public List<Flight> getFlights() {
		return flightService.findAll();
	}

	/**
	 * Return Flight List depending of the value of the parameters Type and
	 * Value.
	 *
	 * @param type
	 *            Type to find by CITY or by COUNTRY, NAME, CODE; DEPARTURE and
	 *            ARRIVAL by number of the flight
	 * @param value
	 *            Value of the Type CITY, COUNTRY, NAME, DEPARTURE and ARRIVAL
	 *            String
	 * @return The List Flight
	 * @throws ParseException
	 */
	@GET
	@Path("/{type}/{value}")
	public List<Flight> getBy(@PathParam("type") String type, 
							  @PathParam("value") String value) throws ParseException {
		List<Flight> flight = new ArrayList<>();

		switch (type) {
		case "NUMBER":
			flight = flightService.findByNumber(value);
			break;
		case "AIRLINE":
			flight = flightService.findByAirline(airlineService.findByName(value));
			break;
		case "ORIGIN":
			flight = flightService.findByOrigin(airportService.findByCode(value));
			break;
		case "DESTINY":
			flight = flightService.findByDestination(airportService.findByCode(value));
			break;
		case "AIRPLANE":
			flight = getFlightByAirplaneModel(value);
			break;
		case "ARR_DATE":
			flight = findByArrivalDate(value);
			break;
		case "DEP_DATE":
			flight = findByDepartureDate(value);
			break;
		default:
			flight = null;
			break;
		}
		return flight;
	}
	
	/**
	 * Return Flight List by dates depending of the value of the parameters Type and
	 * Value.
	 *
	 * @param type
	 *            Type to find by ARR_DATES and DEP_DATES
	 * @param value
	 *            Value of the Type CITY, COUNTRY, NAME, DEPARTURE and ARRIVAL
	 *            String
	 * @return The List Flight
	 * @throws ParseException
	 */
	@GET
	@Path("/{type}/{datefrom}/{dateto}")
	public List<Flight> getFlightByDestinyCode(@PathParam("type") String type, 
											   @PathParam("datefrom") String dateFrom,
											   @PathParam("dateto") String dateTo) {

		System.out.println("Date From: " + dateFrom + " DateTo: " + dateTo);
		try {
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			Date dateFromDate = (Date) formatter.parse(dateFrom + " 00:00:00");
			Date dateToDate = (Date) formatter.parse(dateTo + " 23:59:59");
			
			List<Flight> flight = new ArrayList<>();
			switch (type) {
			case "ARR_DATES":
				flight = flightService.findByArrivalBetween(dateFromDate, dateToDate);
				break;
			case "DEP_DATES":
				flight = flightService.findByDepartureBetween(dateFromDate, dateToDate);
				break;
			default:
				flight = null;
				break;
			}
			return flight;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return Flight List by model of airplane
	 *
	 * @param model 	Model of airplane
	 * @return The List of Flights
	 */
	private List<Flight> getFlightByAirplaneModel(String model) {
		List<Airplane> listAirplane = null;
		List<Flight> listFlights = null;
		System.out.println("Airplane Model: " + model);
		listAirplane = airplaneService.findByModel(model);

		for (Airplane airplane : listAirplane) {
			listFlights = flightService.findByArrival(airplane);
		}
		return listFlights;
	}

	/**
	 * Return Flight List by arrival dates
	 *
	 * @param date 	Date
	 * @return The List of Flights
	 */
	private List<Flight> findByArrivalDate(String date) throws ParseException {
		try {
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			Date dateArrival = (Date) formatter.parse(date);
			return flightService.findByArrival(dateArrival);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return Flight List by departure dates
	 *
	 * @param date 	Date
	 * @return The List of Flights
	 */
	private List<Flight> findByDepartureDate(String date) throws ParseException {
		try {
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			Date dateNew = (Date) formatter.parse(date);
			return flightService.findByDeparture(dateNew);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create a new Flight receiving in JSON format the new object
	 *
	 * @param  flight	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@POST
	@Path("/")
	public Response createFlight(Flight flight){
		System.out.println("Flight: " + flight);
		flightService.create(flight);
		return Response.status(200).entity("Success").build();
	}
	
	/**
	 * Update a flight, receiving the object airline by JSON format
	 *
	 * @param  clientFlight	Object airline in JSON Format
	 * @return Response OK if is correct
	 */
	@PUT
	@Path("/")
	public Response updateFlight(Flight clientFlight){
		System.out.println("Flight Update: " + clientFlight);
		Flight flight = flightService.find(clientFlight);
		flight.setFlightnr(clientFlight.getFlightnr());
		flight.setArrivalDate(clientFlight.getArrivalDate());
		flight.setArrivalTime(clientFlight.getArrivalTime());
		flight.setDepartureDate(clientFlight.getDepartureDate());
		flight.setDepartureTime(clientFlight.getDepartureTime());
		flightService.update(flight);
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
	public Response deleteFlight(@PathParam("id") Long id){
		System.out.println("Flight Delete: " + id);
		Flight flightDel = new Flight();
		flightDel.setId(id);
		flightService.delete(flightService.find(flightDel));
		return Response.status(200).entity("Success").build();
	}

}