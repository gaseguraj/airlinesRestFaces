package cs545.airline.ws;

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

@Path("airplane")
public class AirplaneRest {
	@Inject
	private AirplaneService airplaneService;
	@Inject
	private FlightService flightService;

	@GET
	@Path("/")
	public List<Airplane> getAirlines() {
		return airplaneService.findAll();
	}

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
	
	@POST
	@Path("/")
	public Response createAirplane(Airplane airplane){
		System.out.println("Airplane: " + airplane);
		airplaneService.create(airplane);
		return Response.status(200).entity("Success").build();
	}
	
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
