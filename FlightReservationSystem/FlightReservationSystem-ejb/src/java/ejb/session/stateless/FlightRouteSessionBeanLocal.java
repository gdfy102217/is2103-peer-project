/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
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

    public Long createNewFlightRoute(String originIataCode, String destinationIataCode) throws FlightRouteExistException,
            GeneralException, AirportNotFoundException;
    
    public List<FlightRoute> retrieveAllFlightRoutes();

    public FlightRoute retrieveFlightRouteByOdPair(String originCode, String destinationCode) throws FlightRouteNotFoundException;
    
    public Long deleteFlightRoute(String originCode, String destinationCode) throws FlightRouteNotFoundException, DeleteFlightRouteException;
    
    public FlightRoute retrieveFlightRouteById(Long flightRouteId) throws FlightRouteNotFoundException;

    public void associateComplementaryFlightRoute(Long newFlightRouteId, Long newComplementaryFlightRouteId);
    
    public Boolean checkIfComplementaryFlightRouteExist(String originCode, String destinationCode) throws FlightRouteNotFoundException;
    
}
