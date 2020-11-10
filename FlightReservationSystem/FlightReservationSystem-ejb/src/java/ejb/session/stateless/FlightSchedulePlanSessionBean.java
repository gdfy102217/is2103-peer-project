/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.FlightSchedulePlanExistException;
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
}
