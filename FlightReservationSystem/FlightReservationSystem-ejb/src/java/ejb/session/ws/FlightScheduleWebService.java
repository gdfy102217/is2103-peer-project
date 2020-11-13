/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AirportSessionBeanLocal;
import entity.Airport;
import entity.FlightSchedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassType;
import util.exception.AirportNotFoundException;
import util.exception.FlightScheduleNotFountException;

/**
 *
 * @author xuyis
 */
@WebService(serviceName = "FlightScheduleWebService")
@Stateless()
public class FlightScheduleWebService {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    
    

    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "searchDirectFlightScehdules")
    public List<FlightSchedule> searchDirectFlightScehdules(@WebParam(name = "departureAirportName") String departureAirportName, @WebParam(name = "destinationAirportName") String destinationAirportName, 
            @WebParam(name = "departureDate") Date departureDate, @WebParam(name = "cabinClassType") CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException {
        
        Airport departureAirport = airportSessionBeanLocal.retrieveAirportByName(departureAirportName);
        Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByName(destinationAirportName);

        Query query = em.createQuery("SELECT f FROM FlightSchedule f WHERE f.departureDateTime = :departureDate AND f.departureAirport = :departureAirport AND f.destinationAirport = :destinationAirport");
        query.setParameter("departureDate", departureDate);
        query.setParameter("departureAirport", departureAirport);
        query.setParameter("destinationAirport", destinationAirport);

        List<FlightSchedule> flightSchedules = query.getResultList();

        if (flightSchedules.isEmpty()) {
            throw new FlightScheduleNotFountException("Flight schedule departure from: " + departureAirport.getIataAirportcode() + " to " + departureAirport + " on date: " + departureDate + " does not exist!");
        }

        for (FlightSchedule flightSchedule : flightSchedules) {
            flightSchedule.getCabinClasses().size();
            flightSchedule.getDepartureAirport();
            flightSchedule.getDestinationAirport();
        }
        return flightSchedules;
    }

    @WebMethod(operationName = "searchConnectingFlightScehdules")
    public List<List<FlightSchedule>> searchConnectingFlightScehdules(@WebParam(name = "departureAirportName") String departureAirportName, @WebParam(name = "destinationAirportName") String destinationAirportName, 
            @WebParam(name = "departureDate") Date departureDate, @WebParam(name = "cabinClassType") CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException {
        
        Airport departureAirport = airportSessionBeanLocal.retrieveAirportByName(departureAirportName);
        Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByName(destinationAirportName);
        
        Query query = em.createQuery("SELECT f FROM FlightSchedule f WHERE f.departureDateTime = :departureDate AND f.departureAirport= :departureAirport");
        query.setParameter("departureDate", departureDate);
        query.setParameter("departureAirport", departureAirport);
        
        List<FlightSchedule> firstFlightSchedules = query.getResultList();
        if (firstFlightSchedules.isEmpty()) {
            throw new FlightScheduleNotFountException("Connecting Flight schedule departure from: " + departureAirport.getIataAirportcode() + " to " + departureAirport + " on date: " + departureDate + " does not exist!");
        }

        
        List<List<FlightSchedule>> flightSchedules = new ArrayList<>();
        for (FlightSchedule firstFlightSchedule: firstFlightSchedules) {
            List<FlightSchedule> flightSchedule = new ArrayList<>();
            flightSchedule.add(firstFlightSchedule);
            flightSchedules.add(flightSchedule);
        }
        
        for (List<FlightSchedule> flightSchedule: flightSchedules) {
            FlightSchedule firstFlightSchedule = flightSchedule.get(0);
            Airport transferAirport = firstFlightSchedule.getDestinationAirport();
            query = em.createQuery("SELECT f FROM FlightSchedule f WHERE f.departureAirport = :transferAirport AND f.destinationAirport = :destinationAiport AND f.departureDateTime BETWEEN :firstArrivalTime AND :secondDepartureTime");
            query.setParameter("transferAirport", transferAirport);
            query.setParameter("destinationAirport", destinationAirport);
            query.setParameter("firstArrivalTime", firstFlightSchedule.getArrivalDateTime());
            query.setParameter("secondDepartureTime", new Date(firstFlightSchedule.getArrivalDateTime().getTime() + 24 * 60 * 60 * 1000));
            
            List<FlightSchedule> secondFlightSchedules = query.getResultList();
            
            if (secondFlightSchedules.isEmpty()) {
                flightSchedules.remove(flightSchedule);
            } else {
                flightSchedule.addAll(secondFlightSchedules);
            }
        }
        
        if (flightSchedules.isEmpty()) {
            throw new FlightScheduleNotFountException("Connecting Flight schedule departure from: " + departureAirport.getIataAirportcode() + " to " + departureAirport + " on date: " + departureDate + " does not exist!");
        }
        
        return flightSchedules;
    }
    
}
