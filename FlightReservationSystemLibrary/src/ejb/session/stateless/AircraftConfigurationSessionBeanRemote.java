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
import util.exception.AircraftConfigurationExistExcetpion;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    
    public Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, List<CabinClass> cabinClasses) throws AircraftConfigurationExistExcetpion, GeneralException;
    
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations();
    
    public AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException;
    
}
