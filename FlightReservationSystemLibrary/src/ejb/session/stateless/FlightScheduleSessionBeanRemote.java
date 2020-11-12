/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFountException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    
    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException;
    
    public List<FlightSchedule> retrieveFlightScheduleByDepartureDestinationAndDepartureDate(Airport departureAirport, Airport destinationAirport, Date departureDate) throws FlightScheduleNotFountException;
    
    public List<FlightSchedule> searchFlightScehdules(String departureAirportName, String destinationAirportName, Date departureDate, Integer numOfPassengers, Integer flightTypePreference, Integer cabinClass) throws AirportNotFoundException, FlightScheduleNotFountException;
    
}
