/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightRouteSessionBeanLocal {

    public FlightRoute createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException;

    public List<FlightRoute> retrieveAllFlightRoutes();

    public FlightRoute retrieveFlightRouteByOdPair(String originCode, String destinationCode) throws FlightRouteNotFoundException;

    public void deleteFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException, DeleteFlightRouteException;
    
}
