/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigurationExistExcetpion;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, AircraftType aircraftType, List<CabinClass> cabinClasses) throws AircraftConfigurationExistExcetpion, GeneralException
    {
        try {
        em.persist(newAircraftConfiguration);
        } catch(PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new AircraftConfigurationExistExcetpion("Aircraft Configuration with the name: " + newAircraftConfiguration.getAircraftConfigurationName() + " already exists!"); 
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        
        em.merge(aircraftType);
        
        for (CabinClass cabinClass : cabinClasses) {
            em.persist(cabinClass);
            cabinClass.setAircraftConfiguration(newAircraftConfiguration);
        }
        
        newAircraftConfiguration.setCabinClasses(cabinClasses);
        newAircraftConfiguration.setAircraftType(aircraftType);
        aircraftType.getConfigurations().add(newAircraftConfiguration);

        em.flush();
           
        return newAircraftConfiguration.getAircraftConfigurationId();
    }
    
    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations() {
        Query query = em.createQuery("SELECT ac FROM AircraftConfiguration ac ORDER BY ac.aircraftType, ac.aircraftConfigurationName");
        
        return query.getResultList();
    }
    
    @Override
    public AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException
    {
        Query query = em.createQuery("SELECT ac FROM AircraftConfiguration ac WHERE ac.aircraftConfigurationName = :name");
        query.setParameter("name", name);
        
        try
        {
            AircraftConfiguration aircraftConfiguration = (AircraftConfiguration) query.getSingleResult();
            aircraftConfiguration.getCabinClasses().size();
            
            return aircraftConfiguration;
        }
        catch(NoResultException ex)
        {
            throw new AircraftConfigurationNotFoundException("Aircraft configuration " + name + " does not exist!\n");
        }                
    }
}
