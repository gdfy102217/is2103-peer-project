/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.CabinClassConfiguration;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class CabinClassSessionBean implements CabinClassSessionBeanRemote, CabinClassSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public CabinClass createNewCabinClass(CabinClass newCabinClass, CabinClassConfiguration newCabinClassConfiguration) {
        
        
        em.persist(newCabinClassConfiguration);
        newCabinClass.getAircraftConfiguration().getCabinClasses().add(newCabinClass);
        
        em.persist(newCabinClass);
        em.flush();
        
        return newCabinClass;
    }
}
