/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewFlight(Flight newFlight, String originAirportIATACode, String destinationAirportIATACode, String aircraftConfigurationName) 
            throws FlightExistException, GeneralException, FlightRouteNotFoundException, AircraftConfigurationNotFoundException, FlightRouteDisabledException {
        
        try {
            
            FlightRoute flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOdPair(originAirportIATACode, destinationAirportIATACode);
            
            if(flightRoute.getDisabled() == true) {

                throw new FlightRouteDisabledException("The flight route from " + originAirportIATACode + " to " + destinationAirportIATACode + " is disabled!");
            
            } else {
                
                AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName(aircraftConfigurationName);
                em.persist(newFlight);
                flightRoute.getFlights().add(newFlight);
                newFlight.setFlightRoute(flightRoute);
                newFlight.setAircraftConfiguration(aircraftConfiguration);
                aircraftConfiguration.getFlights().add(newFlight);
                
                em.flush();

                return newFlight.getFlightId();
            }
            
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new FlightExistException("The flight already exists!");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void associateComplementaryFlight(Long flightId, Long complementaryFlightId) {
        Flight flight = em.find(Flight.class, flightId);
        Flight complementaryFlight = em.find(Flight.class, complementaryFlightId);
        
        flight.setComplementaryReturnFlight(complementaryFlight);
        complementaryFlight.setComplementaryReturnFlight(flight);
    }

    @Override
    public List<Flight> retrieveAllFlights() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        
        List<Flight> flights = query.getResultList();
        for(Flight flight: flights) {
            flight.getComplementaryReturnFlight();
        }
        return flights;
    }
    
    @Override
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException
    {
        Flight flight = em.find(Flight.class, flightId);
        
        if(flight != null)
        {
            flight.getFlightSchedulePlans().size();
            return flight;
        }
        else
        {
            throw new FlightNotFoundException("Flight ID " + flightId + " does not exist");
        }
    }

    @Override
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        
        Flight flight = (Flight) query.getSingleResult();
        if (flight != null) {
            flight.getFlightRoute();
            flight.getComplementaryReturnFlight();
            for (CabinClass cabinClass: flight.getAircraftConfiguration().getCabinClasses()) {
                cabinClass.getCabinClassType();
                cabinClass.getCabinClassConfiguration().getCabinClassCapacity();
            }
            return flight;
        } else {
            throw new FlightNotFoundException("Flight number " + flightNumber + " is not found!");
        }
    }

    @Override
    public void deleteFlight(Flight flight) throws FlightNotFoundException, DeleteFlightException {
        em.merge(flight);
        
        if (flight.getFlightSchedulePlans().isEmpty()) {
            flight.getFlightRoute().getFlights().remove(flight);
            flight.getAircraftConfiguration().getFlights().remove(flight);
            flight.getComplementaryReturnFlight().setComplementaryReturnFlight(null);
            flight.setComplementaryReturnFlight(null);
            em.remove(flight);
        } else {
            flight.setDisabled(true);
            throw new DeleteFlightException("Flight no. " + flight.getFlightNumber() + " is in use and cannot be deleted! Instaed, it is set as diabled. ");
        }
    }

    @Override
    public List<FlightSchedule> retrieveFlightSchedulesByFlightNumber(String flightNumber) throws FlightNotFoundException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        List<FlightSchedule> list = new ArrayList<>();
        List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlans();
        flightSchedulePlans.size();
        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            if (flightSchedulePlan.getDisabled() == false) {
                List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
                flightSchedules.size();
                for (FlightSchedule flightSchedule : flightSchedules) {
                    list.add(flightSchedule);
                }
            }
        }
        return list;
    }
        
    @Override
    public void updateFlight(Flight flight, String newAircraftConfigurationName) throws AircraftConfigurationNotFoundException {
        em.merge(flight);
        
        AircraftConfiguration newAircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName(newAircraftConfigurationName);
        
        flight.getAircraftConfiguration().getFlights().remove(flight);
        flight.setAircraftConfiguration(newAircraftConfiguration);
        
        Flight complementaryFlight = flight.getComplementaryReturnFlight();
        complementaryFlight.getAircraftConfiguration().getFlights().remove(complementaryFlight);
        complementaryFlight.setAircraftConfiguration(newAircraftConfiguration);
    }
    
    
}
