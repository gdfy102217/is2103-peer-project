/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.CabinClass;
import entity.Employee;
import entity.FlightSchedule;
import java.util.List;
import java.util.Scanner;
import util.exception.FlightNotFoundException;

/**
 *
 * @author Administrator
 */
public class SalesManagementModule {
    
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    
    private Employee employee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, Employee employee) {
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.employee = employee;
    }
    
    public void menuSalesManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** FRS Management :: Sales Management Module ***\n");
            System.out.println("1: View Seats Inventory ");
            System.out.println("2: View Flight Reservation");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();
                if (response == 1) { 
                    try {          
                        viewSeatsInventory();
                    } catch (FlightNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 2) {
                    try {
                        viewFlightReservation();
                    } catch (FlightNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }
    
    private void viewSeatsInventory() throws FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Sales Management Module :: View Seats Inventory ***\n");

        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        List<FlightSchedule> list = flightSessionBeanRemote.retrieveFlightSchedulesByFlightNumber(flightNumber);
        
        Integer selection = 0;
        System.out.println("Available Flight Schedule: ");
        for(FlightSchedule flightSchedule : list) {
            System.out.println("No." + selection + " " + flightSchedule);
            selection++;
        }
        System.out.print("Enter no. to view details> ");
        Integer userSelection = Integer.valueOf(scanner.nextLine().trim());
        FlightSchedule flightScheduleSelected = list.get(userSelection);
        
        for (CabinClass cabinClass: flightScheduleSelected.getCabinClasses()) {
            System.out.println("Cabin class type: " + cabinClass.getCabinClassType());
            System.out.println("No. of seats available: " + cabinClass.getCabinClassConfiguration().getCabinClassCapacity());
            System.out.println("No. of seats reserved: " + cabinClass.getNumOfReservedSeats());
            System.out.println("No. of balance seats: " + (cabinClass.getCabinClassConfiguration().getCabinClassCapacity() - cabinClass.getNumOfReservedSeats()));
        }
        
    }
    
    private void viewFlightReservation() throws FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Sales Management Module :: View Flight Reservation ***\n");

        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        List<FlightSchedule> list = flightSessionBeanRemote.retrieveFlightSchedulesByFlightNumber(flightNumber);
        
        Integer selection = 0;
        System.out.println("Available Flight Schedule: ");
        for(FlightSchedule flightSchedule : list) {
            System.out.println("No." + selection + " " + flightSchedule);
            selection++;
        }
        System.out.print("Enter no. to view details> ");
        Integer userSelection = Integer.valueOf(scanner.nextLine().trim());
        FlightSchedule flightScheduleSelected = list.get(userSelection);    
        
        flightScheduleSessionBeanRemote.viewFlightReservation(flightScheduleSelected);
    }
}
