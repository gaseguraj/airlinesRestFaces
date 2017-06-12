package cs545.beans;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cs545.airline.model.Flight;
import cs545.airline.service.FlightService;
 
@Named
@ViewScoped
public class FlightView implements Serializable {
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Flight> flight;
     
    @Inject
    private FlightService flightService;
 
    public void flightView(){
    	
    }
    
    @PostConstruct
    public void init() {
    	System.out.println("Post");
    	flight = flightService.findAll();
    	System.out.println("Flight:" + flight);
    }
     
    public List<Flight> getFlight() {
        return flight;
    }
 
 
}