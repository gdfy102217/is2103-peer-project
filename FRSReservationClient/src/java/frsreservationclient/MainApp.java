/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;


public class MainApp {
    
    
    private Customer customer = null;
    private CustomerSessionBeanRemote customerSessionBeanRemote;

    public MainApp() {
    }
    
    
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS - FRS Reservation Client ***\n");
            System.out.println("1: Search Flight");
            System.out.println("2: Register As Customer");
            System.out.println("3: Customer Login");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4) 
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {  
                    searchFlight();
                } else if (response == 2) {
                    register();
                } else if (response == 3) {
                    login();
                    menuMain();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
			
            if(response == 4) {
                break;
            }
	}
    }

    public void searchFlight(){
        
    }
    
    public void register(){
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String address = "";
        String username = ""; //unique
        String password = "";

        System.out.println("*** FRS Reservation :: Register As Customer ***\n");
        System.out.print("Enter First Name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter Email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter Mobile Phone Number> ");
        phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        address = scanner.nextLine().trim();
        System.out.print("Enter Username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        password = scanner.nextLine().trim();
        
        if (firstName.length() > 0 && lastName.length() > 0 && email.length() > 0 && phoneNumber.length() > 0 && address.length() > 0 && username.length() > 0 && password.length() > 0) {
            Customer newCustomer = new Customer(firstName, lastName, email, phoneNumber, address, username, password);
            try {
                Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
                System.out.println("Registered succefully! Customer ID: " + newCustomerId + "\n");
                customer = newCustomer;
            } catch (CustomerExistException | GeneralException ex) {
                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Incomplete information, please try again!\n");
        }
 
    }
    
    public void login(){
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** FRS Reservation :: Customer Login ***\n");
        System.out.print("Enter Username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        password = scanner.nextLine().trim();
        
        if (username.length() > 0 && password.length() > 0) {
            try {
                
                Customer currentCustomer = customerSessionBeanRemote.retrieveCustomerByUsername(username);
                if (password.equals(currentCustomer.getPassword())) {
                    customer = currentCustomer;
                    System.out.println("Login Succesfully! Current Customer ID: " + customer.getCustomerId() + "\n");
                } else {
                    System.out.println("Wrong password, please try again!\n");
                }
                
            } catch (CustomerNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Incomplete information, please try again!\n");
        }     
    }
    
    public void menuMain() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** FRS Reservation :: Main Menu ***\n");
            System.out.println("1: Search Flight");
            System.out.println("2: View My Flight Reservations");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4) 
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) {  
                    searchFlight();
                } else if (response == 2) {
                    viewMyFlightReservations();
                } else if (response == 3) {
                    viewMyFlightReservationDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
			
            if(response == 4) {
                break;
            }
        }
        
    }
    
    public void viewMyFlightReservations(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** FRS Reservation :: View My Flight Reservations ***\n");
        List<FlightReservation> flightReservations = customer.getFlightReservations();
        
        System.out.printf("%3s%21s\n","   ", "Flight Reservation ID");
        Integer num = 0;

        for(FlightReservation flightReservation: customer.getFlightReservations())
        {
            num++;
            System.out.printf("%3s%21s\n", num,flightReservation.getFlightReservationId());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
        
    }
    
    public void viewMyFlightReservationDetails(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** FRS Reservation :: View My Flight Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        FlightReservation currentFlightReservation = null;
        
        for (FlightReservation flightReservation: customer.getFlightReservations()) {
            if (flightReservation.getFlightReservationId().equals(reservationId)) {
                currentFlightReservation = flightReservation;
            }
        }
        
        if (currentFlightReservation == null) {
            System.out.println("The Reservation with ID: " + reservationId + " does not exit!\n");
        } else {
            System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s\n", "   ", "Passenger Name", "Passport Number","Cabin Class", "Seat Number", "Flight Number", "Departure Time", "Duration");
            Integer num = 0;
            
            for (String[] passenger: currentFlightReservation.getPassengers()) {
                num++;
                //duration tbc
                System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s\n", num, passenger[0] + " " + passenger[1], passenger[2], currentFlightReservation.getFlightNumber(), currentFlightReservation.getFlightDateTime(),"duration");
            }
            
            if (!currentFlightReservation.getReturnFlightNumber().isEmpty()) {
                System.out.println("--------------------");
                System.out.println("Return Flights:");
                
                for (String[] passenger: currentFlightReservation.getPassengers()) {
                    num++;
                    //duration tbc
                    System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s\n", num, passenger[0] + " " + passenger[1], passenger[2], currentFlightReservation.getReturnFlightNumber(), currentFlightReservation.getReturnFlightDateTime(),"duration");
                }
            }
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
        
    }
    
}
