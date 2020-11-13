/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftTypeExistException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.GeneralException;

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
    public AircraftType createNewAircraftType(AircraftType aircraftType) throws AircraftTypeExistException, GeneralException {
           
        try {         
            em.persist(aircraftType);
            aircraftType.setConfigurations(new ArrayList<>());
            em.flush();               

            return aircraftType;
        }catch(PersistenceException ex) {
            if(ex.getCause() != null && 
                ex.getCause().getCause() != null &&
                ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new AircraftTypeExistException("Aircraft Type with Name: " + aircraftType.getAircraftTypeName() + " does not exist!");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
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
