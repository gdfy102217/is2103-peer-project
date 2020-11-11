/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AirportNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException
    {
        try
        {
            em.persist(flightSchedule);
            flightSchedule.getFlightSchedulePlan().getFlightSchedules().add(flightSchedule);
            em.flush();

            return flightSchedule;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightScheduleExistException("This flight schedule already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    public List<FlightSchedule> retrieveFlightScheduleByDepartureAndDestination(Airport departureAirport, Airport destinationAirport) {
        Query query = em.createQuery("SELECT f FROM FlightSchedule f JOIN f.flightSchedulePlan p JOIN p.flight t JOIN t.flightRoute r WHERE r.origin = : departureAirport AND r.destination =: destinationAirport ");
        query.setParameter("departureAirport", departureAirport);
        query.setParameter("destinationAirport", destinationAirport);
        
        
    }
    
    public List<FlightSchedule> searchFlightScehdules(String departureAirportName, String destinationAirportName, Date departureDate, Integer numOfPassengers, Integer flightTypePreference, Integer cabinClass) {
        
        Airport departureAirport = airportSessionBeanLocal.retrieveAirportByName(departureAirportName);
        Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByName(destinationAirportName);
        
        if (flightTypePreference == 1) {
            
        }
    } 
            
}
