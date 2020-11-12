/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;

/**
 *
 * @author Administrator
 */
public class Main {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, flightSessionBeanRemote,
            flightScheduleSessionBeanRemote, flightRouteSessionBeanRemote,
            aircraftConfigurationSessionBeanRemote, flightSchedulePlanSessionBeanRemote,
            fareSessionBeanRemote, airportSessionBeanRemote);
        mainApp.runApp();
    }
    
}
