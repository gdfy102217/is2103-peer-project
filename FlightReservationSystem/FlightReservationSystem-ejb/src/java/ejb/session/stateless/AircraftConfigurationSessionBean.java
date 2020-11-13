/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.CabinClassConfiguration;
import entity.Flight;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;

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
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration)
    {
        em.persist(newAircraftConfiguration);
        newAircraftConfiguration.getAircraftType().getConfigurations().add(newAircraftConfiguration);
        em.flush();
        
        return newAircraftConfiguration;
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
        query.setParameter(":name", name);
        
        if(query.getSingleResult() != null)
        {
            return (AircraftConfiguration) query.getSingleResult();
        }
        else
        {
            throw new AircraftConfigurationNotFoundException("Aircraft configuration " + name + " does not exist!");
        }                
    }
}
