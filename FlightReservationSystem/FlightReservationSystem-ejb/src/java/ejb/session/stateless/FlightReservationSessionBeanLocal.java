/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CabinClassType;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author xuyis
 */
@Local
public interface FlightReservationSessionBeanLocal {
    
    public Long reserveFlight(Integer numOfPassengers, List<String[]> passengers, String[] creditCard, CabinClassType cabinClassType, List<Long> flightScheduleIds, List<Long> returnFlightScheduleIds, Customer customer);
    
    public FlightReservation retrieveFlightReservationByID(Long flightReservationId) throws FlightReservationNotFoundException;
    
}
