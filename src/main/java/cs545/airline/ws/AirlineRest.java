package cs545.airline.ws;

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
	
	@POST
	@Path("/")
	public Response createAirline(Airline airline){
		System.out.println("Airline: " + airline.getName());
		airlineService.create(airline);
		return Response.status(200).entity("Success").build();
	}
	
	@PUT
	@Path("/")
	public Response updateAirline(Airline clientAirline){
		System.out.println("Airline Update: " + clientAirline.getName() +  " ID: " + clientAirline.getId());
		Airline airline = airlineService.findById(clientAirline.getId());
		airline.setName(clientAirline.getName());
		airlineService.update(airline);
		return Response.status(200).entity("Success").build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteAirline(@PathParam("id") Long id){
		System.out.println("Airline Delete: " + id);
		Airline airline = airlineService.findById(id);
		airlineService.delete(airline);
		return Response.status(200).entity("Success").build();
	}
	
}
