/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Flight createNewFlight(Flight flight) throws FlightExistException, GeneralException
    {
        try
        {
            flight.getAircraftConfiguration().setFlight(flight);
            flight.getFlightRoute().getFlights().add(flight);
            flight.getComplementaryReturnFlight().setComplementaryReturnFlight(flight);
            em.persist(flight);
            em.flush();

            return flight;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightExistException("This flight already exists!");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<Flight> retrieveAllFlights()
    {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        
        return query.getResultList();
    }
    
    @Override
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNotFoundException
    {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        
        
        if ((Flight) query.getSingleResult() != null) {
            return (Flight) query.getSingleResult();
        } else {
            throw new FlightNotFoundException("Flight number " + flightNumber + " is not found!");
        }
    }
    
    @Override
    public void deleteFlight(Flight flight) throws FlightNotFoundException, DeleteFlightException
    {
        
        if(flight.getFlightSchedulePlans().isEmpty())
        {
            flight.getFlightRoute().getFlights().remove(flight);
            flight.setFlightRoute(null);
            flight.getAircraftConfiguration().setFlight(null);
            flight.setAircraftConfiguration(null);
            flight.getComplementaryReturnFlight().setComplementaryReturnFlight(null);
            flight.setComplementaryReturnFlight(null);
            em.remove(flight);
        }
        else
        {
            flight.setDisabled(true);
            System.out.println("Flight no. " + flight.getFlightNumber() + " is set disabled!");
            throw new DeleteFlightException("Flight no. " + flight.getFlightNumber() + " is in use and cannot be deleted!");
        }
    }
}
