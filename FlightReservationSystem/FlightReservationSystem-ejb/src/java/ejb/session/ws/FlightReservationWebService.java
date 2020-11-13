/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassType;
import util.exception.FlightReservationNotFoundException;

/**
 *
 * @author xuyis
 */
@WebService(serviceName = "FlightReservationWebService")
@Stateless()
public class FlightReservationWebService {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "retrieveFlightReservationByID")
    public FlightReservation retrieveFlightReservationByID(@WebParam(name = "flightReservationId") Long flightReservationId) throws FlightReservationNotFoundException {
        
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);
        
        if (flightReservation == null) {
            throw new FlightReservationNotFoundException("Flight Reservation with ID: " + flightReservationId + " does not exist!");
        }
        
        for (FlightSchedule flightSchedule: flightReservation.getFlightSchedules()) {
            flightSchedule.getDepartureAirport();
            flightSchedule.getDestinationAirport();
        }
        
        for (FlightSchedule returnFlightSchedule: flightReservation.getReturnFlightSchedules()) {
            returnFlightSchedule.getDepartureAirport();
            returnFlightSchedule.getDestinationAirport();
        }
        
        return flightReservation;
    }
    
    @WebMethod(operationName = "reserveFlight")
    public Long reserveFlight(@WebParam(name = "numOfPassengers") Integer numOfPassengers, @WebParam(name = "passengers") List<String[]> passengers, 
            @WebParam(name = "creditCard") String[] creditCard, @WebParam(name = "cabinClassType") CabinClassType cabinClassType, 
            @WebParam(name = "flightScheduleIds") List<Long> flightScheduleIds, @WebParam(name = "returnFlightScheduleIds") List<Long> returnFlightScheduleIds, 
            @WebParam(name = "customer") Customer customer) {
       
        FlightReservation flightReservation = new FlightReservation(numOfPassengers, passengers, creditCard, cabinClassType, customer);
        em.persist(flightReservation);
        
        for(Long flightScheduleId: flightScheduleIds) {
            FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
            flightReservation.getFlightSchedules().add(flightSchedule);
        }
        
        if (!returnFlightScheduleIds.isEmpty()) {
            for(Long returnFlightScheduleId: returnFlightScheduleIds) {
            FlightSchedule returnFlightSchedule = em.find(FlightSchedule.class, returnFlightScheduleId);
            flightReservation.getReturnFlightSchedules().add(returnFlightSchedule);
        }
        }
        
        em.flush();
        
        return flightReservation.getFlightReservationId();
    }

    
}
