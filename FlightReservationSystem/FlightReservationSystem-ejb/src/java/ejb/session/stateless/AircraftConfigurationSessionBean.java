/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration)
    {
        newAircraftConfiguration.getAircraftType().getConfigurations().add(newAircraftConfiguration);
        for (CabinClassConfiguration cabinClassConfiguration: newAircraftConfiguration.getCabinClassConfigurations()) {
            cabinClassConfiguration.setAircraftConfiguration(newAircraftConfiguration);
            em.persist(cabinClassConfiguration);
        }
        em.persist(newAircraftConfiguration);
        em.flush();
        
        return newAircraftConfiguration.getAircraftConfigurationId();
    }
    
    @Override
    public List<AircraftConfiguration> viewAllAircraftConfigurations() {
        Query query = em.createQuery("SELECT ac FROM AircraftConfiguration ac ORDER BY ac.aircraftType, ac.aircraftConfigurationName");
        
        return query.getResultList();
    }
}
