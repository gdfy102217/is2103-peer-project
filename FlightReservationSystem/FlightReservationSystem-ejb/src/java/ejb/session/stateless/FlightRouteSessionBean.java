/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
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
    public Long createNewFlightRoute(String originIataCode, String destinationIataCode) throws FlightRouteExistException,
            GeneralException, AirportNotFoundException
    {
        try
        {
            try {
                retrieveFlightRouteByOdPair(originIataCode, destinationIataCode);
                throw new FlightRouteExistException("This flight route already exists!");
            } catch (FlightRouteNotFoundException ex) {
                Airport origin = airportSessionBeanLocal.retrieveAirportByIataCode(originIataCode);
                Airport destination = airportSessionBeanLocal.retrieveAirportByIataCode(destinationIataCode);

                FlightRoute flightRoute = new FlightRoute(origin, destination);
                em.persist(flightRoute);    

                origin.getFlightsFromAirport().add(flightRoute);
                destination.getFlightsToAirport().add(flightRoute);


                em.flush();

                return flightRoute.getFlightRouteId();
            }
            
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
    public void associateComplementaryFlightRoute(Long newFlightRouteId, Long newComplementaryFlightRouteId) throws FlightRouteNotFoundException{
            FlightRoute newFlightRoute = retrieveFlightRouteById(newFlightRouteId);
            FlightRoute newComplementaryFlightRoute = retrieveFlightRouteById(newComplementaryFlightRouteId);
            newFlightRoute.setComplementaryReturnRoute(newComplementaryFlightRoute);
            newComplementaryFlightRoute.setComplementaryReturnRoute(newFlightRoute);
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
            FlightRoute flightRoute = (FlightRoute) query.getSingleResult();
            flightRoute.getFlights().size();
            return flightRoute;
            
        } catch (NoResultException ex) {
            throw new FlightRouteNotFoundException("Flight route with origin " + originCode + " and destination " + destinationCode + " is not found!");
        }
    }

    @Override
    public Long deleteFlightRoute(String originCode, String destinationCode) throws FlightRouteNotFoundException, DeleteFlightRouteException
    {
        
        FlightRoute flightRoute = retrieveFlightRouteByOdPair(originCode, destinationCode);
        Long flightRouteId = flightRoute.getFlightRouteId();
        
        flightRoute.getFlights();
        if(flightRoute.getFlights().isEmpty())
        {
            flightRoute.getOrigin().getFlightsFromAirport().remove(flightRoute);
            flightRoute.getOrigin().getFlightsToAirport().remove(flightRoute);
            
            if (flightRoute.getComplementaryReturnRoute() != null) {
                flightRoute.getComplementaryReturnRoute().setComplementaryReturnRoute(null);
            }
            
            flightRoute.setComplementaryReturnRoute(null);
            em.remove(flightRoute);
            
            return flightRouteId;
            
        } else {
            
            flightRoute.setDisabled(true);
            
            throw new DeleteFlightRouteException("Flight route from " + flightRoute.getOrigin() + " to " + flightRoute.getDestination() +
                    " is in use and cannot be deleted! Instaed, it is set as diabled. ");
        }
    }
    
    public Boolean checkIfComplementaryFlightRouteExist(String originCode, String destinationCode) throws FlightRouteNotFoundException {
        
        FlightRoute flightRoute = retrieveFlightRouteByOdPair(originCode, destinationCode);
        
        if (flightRoute.getComplementaryReturnRoute() == null) {
            return false;
        } else {
            return true;
        }
    }
}
