/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.FareExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Fare createNewFare(Fare fare) throws FareExistException, GeneralException
    {
        try
        {
            em.persist(fare);
            fare.getFlightSchedulePlan().getFares().add(fare);
            fare.getCabinClassConfiguration().getFares().add(fare);
            em.flush();

            return fare;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new FareExistException("This fare already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void deleteFare(Fare fare) {
        fare.getCabinClassConfiguration().getFares().remove(fare);
        fare.setCabinClassConfiguration(new CabinClassConfiguration());
        fare.getFlightSchedulePlan().getFares().remove(fare);
        fare.setFlightSchedulePlan(new FlightSchedulePlan());
        
        em.remove(fare);
        System.out.println("This fare has been successfully deleted!");
    }
}
