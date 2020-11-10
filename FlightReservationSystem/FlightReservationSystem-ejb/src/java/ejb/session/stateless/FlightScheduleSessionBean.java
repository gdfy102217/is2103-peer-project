/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.FlightScheduleExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

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
}
