/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightSessionBeanRemote {
    
    public Flight createNewFlight(Flight flight) throws FlightExistException, GeneralException;
    
    public List<Flight> retrieveAllFlights();
    
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException;
    
    public void deleteFlight(Flight flight) throws FlightNotFoundException, DeleteFlightException;
}
