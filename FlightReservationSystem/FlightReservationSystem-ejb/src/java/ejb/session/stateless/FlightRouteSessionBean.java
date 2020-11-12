/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightRoute createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException
    {
        try
        {
            flightRoute.getOrigin().getFlightsFromAirport().add(flightRoute);
            flightRoute.getDestination().getFlightsToAirport().add(flightRoute);
            em.persist(flightRoute);
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
        
        
        if ((FlightRoute) query.getSingleResult() != null) {
            return (FlightRoute) query.getSingleResult();
        } else {
            throw new FlightRouteNotFoundException("Flight route with origin " + originCode + " and destination " + destinationCode + " is not found!");
        }
    }

    @Override
    public void deleteFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException, DeleteFlightRouteException
    {
        
        if(flightRoute.getFlights().isEmpty())
        {
            flightRoute.getOrigin().getFlightsFromAirport().remove(flightRoute);
            flightRoute.getOrigin().getFlightsToAirport().remove(flightRoute);
            flightRoute.setOrigin(null);
            flightRoute.setDestination(null);
            flightRoute.getComplementaryReturnRoute().setComplementaryReturnRoute(null);
            flightRoute.setComplementaryReturnRoute(null);
            em.remove(flightRoute);
        }
        else
        {
            flightRoute.setDisabled(true);
            System.out.println("Flight route from " + flightRoute.getOrigin() + " to " + flightRoute.getDestination() + " is set disabled!");
            throw new DeleteFlightRouteException("Flight route from " + flightRoute.getOrigin() + " to " + flightRoute.getDestination() +
                    " is in use and cannot be deleted!");
        }
    }
}
