/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author Administrator
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public AircraftType createNewAircraftType(AircraftType aircraftType)
    {
            em.persist(aircraftType);
            em.flush();

            return aircraftType;

    }
    
    @Override
    public AircraftType retrieveAircraftTypeByName(String aircraftTypeName) throws AircraftTypeNotFoundException {
        Query query = em.createQuery("SELECT at FROM AircraftType at WHERE at.aircraftTypeName = :inAircraftTypeName");
        query.setParameter("inAircraftTypeName", aircraftTypeName);
        
        try
        {
            return (AircraftType)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AircraftTypeNotFoundException("AircraftType " + aircraftTypeName + " does not exist!");
        }
    }
}
