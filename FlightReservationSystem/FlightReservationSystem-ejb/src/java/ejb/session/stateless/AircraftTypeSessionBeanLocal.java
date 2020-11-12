/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Local;
import util.exception.AircraftTypeExistException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface AircraftTypeSessionBeanLocal {

    public AircraftType createNewAircraftType(AircraftType aircraftType) throws AircraftTypeExistException, GeneralException;

    public AircraftType retrieveAircraftTypeByName(String aircraftTypeName) throws AircraftTypeNotFoundException;
    
}
