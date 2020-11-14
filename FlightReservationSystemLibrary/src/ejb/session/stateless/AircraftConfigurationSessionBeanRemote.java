/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationNotFoundException;

/**
 *
 * @author Administrator
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration);
    
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations();
    
    public AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException;
    
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, List<CabinClass> cabinClasses);
    
}
