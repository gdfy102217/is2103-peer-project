/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.CabinClassConfiguration;
import javax.ejb.Remote;

/**
 *
 * @author Administrator
 */
@Remote
public interface CabinClassSessionBeanRemote {
    
    public CabinClass createNewCabinClass(CabinClass newCabinClass, CabinClassConfiguration newCabinClassConfiguration);
    
}
