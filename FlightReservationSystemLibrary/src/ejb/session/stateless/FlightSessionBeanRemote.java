/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightSessionBeanRemote {
    
    public Flight createNewFlight(Flight flight, String aircraftConfigurationName, Long flightRouteId) throws FlightExistException, GeneralException, FlightRouteNotFoundException, AircraftConfigurationNotFoundException;
    
    public List<Flight> retrieveAllFlights();
    
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException;
    
    public void deleteFlight(Flight flight) throws FlightNotFoundException, DeleteFlightException;
    
    public List<FlightSchedule> retrieveFlightSchedulesByFlightNumber(String flightNumber) throws FlightNotFoundException;
    
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException;
    
    public void updateComplementaryFlight(Flight flight, Flight newReturnFlight);
    
    public void updateFlightRoute(Flight flight, FlightRoute newFlightRoute);
    
    public void updateAircraftConfiguration(Flight flight, AircraftConfiguration newAircraftConfiguration);
    
    public void associateComplementaryFlight(Flight flight, Flight complementaryFlight);
    
}
