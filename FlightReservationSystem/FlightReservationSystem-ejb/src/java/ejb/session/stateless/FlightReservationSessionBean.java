/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassType;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author xuyis
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public FlightReservationSessionBean() {
    }
    
    public FlightReservation retrieveFlightReservationByID(Long flightReservationId) throws FlightReservationNotFoundException {
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);
        
        if (flightReservation == null) {
            throw new FlightReservationNotFoundException("Flight Reservation with ID: " + flightReservationId + " does not exist!");
        }
        flightReservation.getFlightSchedule();
        flightReservation.getReturnFlightSchedule();
        
        return flightReservation;
    }
    
    @Override
    public Long reserveFlight(Integer numOfPassengers, List<String[]> passengers, String[] creditCard, CabinClassType cabinClassType, Long flightScheduleId, Long returnFlightScheduleId, Customer customer) {
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
        FlightSchedule returnFlightSchedule = em.find(FlightSchedule.class, returnFlightScheduleId);
        
        FlightReservation flightReservation = new FlightReservation(numOfPassengers, passengers, creditCard, cabinClassType, flightSchedule, returnFlightSchedule, customer);
        
        em.persist(flightReservation);
        flightSchedule.getFlightReservations().add(flightReservation);
        returnFlightSchedule.getFlightReservations().add(flightReservation);
        em.flush();
        
        return flightReservation.getFlightReservationId();
    }

    
}
