/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @EJB(name = "AirportSessionBeanLocal")
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightRoute createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException
    {
        try
        {
            em.persist(flightRoute);
            
            flightRoute.getOrigin().getFlightsFromAirport().add(flightRoute);
            flightRoute.getDestination().getFlightsToAirport().add(flightRoute);
            
            em.flush();
            return flightRoute;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightRouteExistException("This flight route already exists!");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public Long createNewFlightRoute(FlightRoute flightRoute, String originIataCode, String destinationIataCode) throws FlightRouteExistException,
            GeneralException, AirportNotFoundException
    {
        try
        {
            Airport origin = airportSessionBeanLocal.retrieveAirportByIataCode(originIataCode);
            Airport destination = airportSessionBeanLocal.retrieveAirportByIataCode(destinationIataCode);
            flightRoute.setOrigin(origin);
            flightRoute.setDestination(destination);
            origin.getFlightsFromAirport().add(flightRoute);
            destination.getFlightsToAirport().add(flightRoute);
            
            em.persist(flightRoute);     
            em.flush();
                     
            System.out.println(flightRoute + " is created!");
            
            return flightRoute.getFlightRouteId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightRouteExistException("This flight route already exists!");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override 
    public void associateComplementaryFlightRoute(Long newFlightRouteId, Long newComplementaryFlightRouteId) {
        try {
            FlightRoute newFlightRoute = retrieveFlightRouteById(newFlightRouteId);
            FlightRoute newComplementaryFlightRoute = retrieveFlightRouteById(newComplementaryFlightRouteId);
            newFlightRoute.setComplementaryReturnRoute(newComplementaryFlightRoute);
            newComplementaryFlightRoute.setComplementaryReturnRoute(newFlightRoute);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(FlightRouteSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public FlightRoute retrieveFlightRouteById(Long flightRouteId) throws FlightRouteNotFoundException
    {
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
        
        if(flightRoute != null)
        {
            flightRoute.getFlights().size();
            return flightRoute;
        }
        else
        {
            throw new FlightRouteNotFoundException("Flight Route ID " + flightRouteId + " does not exist");
        }
    }
    
    @Override
    public List<FlightRoute> retrieveAllFlightRoutes()
    {
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr ORDER BY fr.origin ASC");
        
        return query.getResultList();
    }
    
    @Override
    public FlightRoute retrieveFlightRouteByOdPair(String originCode, String destinationCode) throws FlightRouteNotFoundException
    {
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.origin.iataAirportcode = :inOrigin AND fr.destination.iataAirportcode = :inDestination");
        query.setParameter("inOrigin", originCode);
        query.setParameter("inDestination", destinationCode);
        
        
        try {
            return (FlightRoute) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FlightRouteNotFoundException("Flight route with origin " + originCode + " and destination " + destinationCode + " is not found!");
        }
    }

    @Override
    public void deleteFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException, DeleteFlightRouteException
    {
        flightRoute.getFlights();
        if(flightRoute.getFlights().isEmpty())
        {
            flightRoute.getOrigin().getFlightsFromAirport().remove(flightRoute);
            flightRoute.getOrigin().getFlightsToAirport().remove(flightRoute);
            flightRoute.setOrigin(null);
            flightRoute.setDestination(null);
            if (flightRoute.getComplementaryReturnRoute() != null) {
                flightRoute.getComplementaryReturnRoute().setComplementaryReturnRoute(null);
            }
            flightRoute.setComplementaryReturnRoute(null);
            em.remove(flightRoute);
        }
        else
        {
            flightRoute.setDisabled(true);
            
            throw new DeleteFlightRouteException("Flight route from " + flightRoute.getOrigin() + " to " + flightRoute.getDestination() +
                    " is in use and cannot be deleted!");
        }
    }
}
