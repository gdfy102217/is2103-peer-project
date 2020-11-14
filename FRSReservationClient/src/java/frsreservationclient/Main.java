/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Administrator
 */
public class Main {

    @EJB(name = "FlightReservationSessionBeanRemote")
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;

    @EJB(name = "FlightScheduleSessionBeanRemote")
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;

    @EJB(name = "CustomerSessionBeanRemote")
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
       
        MainApp mainApp = new MainApp(customerSessionBeanRemote, flightScheduleSessionBeanRemote, flightReservationSessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
