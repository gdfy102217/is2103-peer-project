/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightRouteSessionBeanRemote {
    
    public FlightRoute createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException, GeneralException;
    
    public List<FlightRoute> retrieveAllFlightRoutes();
    
    public FlightRoute retrieveFlightRouteById(Long flightRouteId) throws FlightRouteNotFoundException;
    
    public FlightRoute retrieveFlightRouteByOdPair(String originCode, String destinationCode) throws FlightRouteNotFoundException;

    public void deleteFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException, DeleteFlightRouteException;
    
    public Long createNewFlightRoute(FlightRoute flightRoute, String originIataCode, String destinationIataCode) throws FlightRouteExistException, GeneralException, AirportNotFoundException;

    public void associateComplementaryFlightRoute(Long newFlightRouteId, Long newComplementaryFlightRouteId);
}

