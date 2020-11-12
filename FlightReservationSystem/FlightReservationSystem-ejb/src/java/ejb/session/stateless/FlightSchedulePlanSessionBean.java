/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.FlightNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException
    {
        try
        {
            em.persist(flightSchedulePlan);
            flightSchedulePlan.getFlight().getFlightSchedulePlans().add(flightSchedulePlan);
            em.flush();

            return flightSchedulePlan;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FlightSchedulePlanExistException("This flight schedule plan already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans()
    {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp ORDER BY fsp.flight.flightNumber ASC");
        
        return query.getResultList();
    }
    
    @Override
    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException
    {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        
        
        if (!query.getResultList().isEmpty()) {
            return query.getResultList();
        } else {
            throw new FlightSchedulePlanNotFoundException("Flight schedule plan with flight number " + flightNumber + " is not found!");
        }
    }
    
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException {
        if(flightSchedulePlan.getFlightSchedulePlans().isEmpty())
        {
            flightSchedulePlan.getFlight().getFlightSchedulePlans().remove(flightSchedulePlan);
            flightSchedulePlan.setFlight(new Flight());
            for (FlightSchedule flightSchedule: flightSchedulePlan.getFlightSchedules()) {
                flightSchedule.
            }
            for (Fare fare: flightSchedulePlan.getFares()) {
                
            }
            flight.getAircraftConfiguration().setFlight(null);
            flight.setAircraftConfiguration(null);
            flight.getComplementaryReturnFlight().setComplementaryReturnFlight(null);
            flight.setComplementaryReturnFlight(null);
            em.remove(flight);
        }
        else
        {
            flightSchedulePlan.setDisabled(true);
            System.out.println("Flight no. " + flight.getFlightNumber() + " is set disabled!");
            throw new DeleteFlightSchedulePlanException("Flight no. " + flight.getFlightNumber() + " is in use and cannot be deleted!");
        }
    }
}
