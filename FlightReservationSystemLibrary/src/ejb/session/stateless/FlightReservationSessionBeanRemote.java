/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.CabinClassType;
import util.exception.FlightReservationNotFoundException;
import util.exception.NoAvailableSeatsException;

/**
 *
 * @author xuyis
 */
@Remote
public interface FlightReservationSessionBeanRemote {
    
     public Long reserveFlight(Integer numOfPassengers, List<String[]> passengers, String[] creditCard, 
            List<Long> flightScheduleIds, List<Long> returnFlightScheduleIds, String departureAirportiATACode, 
            String destinationAirportiATACode, Date departureDate, Date returnDate, Customer customer) throws NoAvailableSeatsException;

    public FlightReservation retrieveFlightReservationByID(Long flightReservationId) throws FlightReservationNotFoundException;
    
}
