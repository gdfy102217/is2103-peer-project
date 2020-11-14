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
import javax.ejb.Local;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteDisabledException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightSessionBeanLocal {

    public Long createNewFlight(Flight newFlight, String originAirportIATACode, String destinationAirportIATACode, String aircraftConfigurationName) 
            throws FlightExistException, GeneralException, FlightRouteNotFoundException, AircraftConfigurationNotFoundException, FlightRouteDisabledException;
    
    public List<Flight> retrieveAllFlights();

    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException;

    public void deleteFlight(Flight flight) throws FlightNotFoundException, DeleteFlightException;

    public List<FlightSchedule> retrieveFlightSchedulesByFlightNumber(String flightNumber) throws FlightNotFoundException;

    public void updateFlight(Flight flight, String newAircraftConfigurationName) throws AircraftConfigurationNotFoundException;
    
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException;

    public void associateComplementaryFlight(Long flightId, Long complementaryFlightId);
    
}
