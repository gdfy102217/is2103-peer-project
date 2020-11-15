/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CabinClassType;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightScheduleException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFountException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightScheduleSessionBeanLocal {
    
    public List<FlightSchedule> searchDirectFlightScehdules(String departureAirportiATACode, String destinationAirportiATACode, Date departureDate, CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException;

    public List<List<FlightSchedule>> searchConnectingFlightScehdules(String departureAirportiATACode, String destinationAirportiATACode, Date departureDate, CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException;

    public List<FlightReservation> viewFlightReservation(FlightSchedule flightSchedule);

    public void deleteFlightSchedule(FlightSchedule flightSchedule) throws DeleteFlightScheduleException;

    public Boolean checkOverlapFlightSchedules(FlightSchedule flightSchedule);
    
}
