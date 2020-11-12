/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import entity.CabinClass;
import entity.Customer;
import entity.Fare;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassType;
import util.exception.AirportNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFountException;
import util.exception.GeneralException;


public class MainApp {
    
    
    private Customer customer = null;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;

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
                System.out.println("> ");

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
        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Integer tripType;
            String departureAirportName = "";
            String destinationAirportName = "";
            Date departureDate;
            Date returnDate;
            Integer numOfPassengers;
            Integer flightTypePreference;
            Integer cabinClass;
            
            System.out.println("*** FRS Reservation :: Search Flight ***\n");
            System.out.println("Enter Trip Type:  1: One-way, 2: Round-trip> ");
            tripType = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter Departure Airport Name> ");
            departureAirportName = scanner.nextLine().trim();
            System.out.println("Enter Destination Airport Name> ");
            destinationAirportName = scanner.nextLine().trim();
            System.out.println("Enter Departure Date (dd/mm/yyyy)> ");
            departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.println("Enter Return Date (dd/mm/yyyy)> ");
            returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.println("Enter Number Of Passengers> ");
            numOfPassengers = scanner.nextInt();
            System.out.println("Enter Flight Type Preference:  1: Direct Flight, 2: Connecting Flight> ");
            flightTypePreference = scanner.nextInt();
            System.out.println("Enter Cabin Class Preference:  1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class> ");
            cabinClass = scanner.nextInt();
            CabinClassType cabinClassType;
            if (cabinClass == 1) {
                cabinClassType = CabinClassType.FIRSTCLASS;
            } else if (cabinClass == 2) {
                cabinClassType = CabinClassType.BUSINESSCLASS;
            } else if (cabinClass == 3) {
                cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
            } else {
                cabinClassType = CabinClassType.ECONOMYCLASS;
            }
            
            System.out.println("\nDeparture Flight Information: \n");
            //on required departure date
            List<FlightSchedule> flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(departureAirportName, destinationAirportName, departureDate, numOfPassengers, flightTypePreference, cabinClassType);
            System.out.println("----- Departure On " + departureDate + "\n");
            printFlightSchedulesTable(flightSchedules, cabinClassType);
            
            //3 days before
            for (int i=3; i>0; --i) {
                Date newDepartureDate = new Date(departureDate.getTime() - i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(departureAirportName, destinationAirportName, newDepartureDate, numOfPassengers, flightTypePreference, cabinClassType);
                System.out.println("----- Departure On " + departureDate + "\n");
                printFlightSchedulesTable(flightSchedules, cabinClassType);
            }
            
            //3 days after
            for (int i=1; i<4; ++i) {
                Date newDepartureDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(departureAirportName, destinationAirportName, departureDate, numOfPassengers, flightTypePreference, cabinClassType);
                System.out.println("----- Departure On " + departureDate + "\n");
                printFlightSchedulesTable(flightSchedules, cabinClassType);
            }
            
            
            if (tripType == 2) {
                System.out.println("\nReturn Flight Information: \n");
                //on required departure date
                flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(destinationAirportName, departureAirportName, returnDate, numOfPassengers, flightTypePreference, cabinClassType);
                System.out.println("----- Return On " + returnDate + "\n");
                printFlightSchedulesTable(flightSchedules, cabinClassType);

                //3 days before
                for (int i=3; i>0; --i) {
                    Date newReturnDate = new Date(returnDate.getTime() - i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(destinationAirportName, departureAirportName, newReturnDate, numOfPassengers, flightTypePreference, cabinClassType);
                    System.out.println("----- Return On " + returnDate + "\n");
                    printFlightSchedulesTable(flightSchedules, cabinClassType);
                }

                //3 days after
                for (int i=1; i<4; ++i) {
                    Date newReturnDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchFlightScehdules(destinationAirportName, departureAirportName, newReturnDate, numOfPassengers, flightTypePreference, cabinClassType);
                    System.out.println("----- Return On " + returnDate + "\n");
                    printFlightSchedulesTable(flightSchedules, cabinClassType);
                }
            }
            
            System.out.println("Do you wish to reserve a flight? Y/N> ");
            String reserve = scanner.nextLine().trim();
            
            if (reserve.charAt(0) == 'Y') {
                reserveFlight(tripType, numOfPassengers, cabinClassType);   
            }
            
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        } catch (AirportNotFoundException | FlightScheduleNotFountException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        
    }
    
    public void printFlightSchedulesTable(List<FlightSchedule> flightSchedules, CabinClassType cabinClassType) {
        
        if (cabinClassType.equals(CabinClassType.FIRSTCLASS)) {
            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%8s\n", "   ", "Flight Schdule ID","Flight Number","Departure Airport","Departure Time (Local Time)","Flight Duration", "Destination Airport","Arrival Time (Local Time)", "First Class Available Seats", "Price");
        } else if (cabinClassType.equals(CabinClassType.BUSINESSCLASS)) {
            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%8s\n", "   ", "Flight Schdule ID","Flight Number","Departure Airport","Departure Time (Local Time)","Flight Duration", "Destination Airport","Arrival Time (Local Time)", "Business Class Available Seats", "Price");
        } else if (cabinClassType.equals(CabinClassType.PREMIUMECONOMYCLASS)) {
            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%8s\n", "   ", "Flight Schdule ID","Flight Number","Departure Airport","Departure Time (Local Time)","Flight Duration", "Destination Airport","Arrival Time (Local Time)", "Premium Economy Class Available Seats", "Price");
        } else if (cabinClassType.equals(CabinClassType.ECONOMYCLASS)) {
            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%8s\n", "   ", "Flight Schdule ID","Flight Number","Departure Airport","Departure Time (Local Time)","Flight Duration", "Destination Airport","Arrival Time (Local Time)", "Economy Class Available Seats", "Price");
        }
        
        Integer availableSeats = 0;
        Double lowestFare = Double.MAX_VALUE;
        Integer num = 0;
        for (FlightSchedule flightSchedule : flightSchedules) {
            num++;
            //cabin class availability not yet finished
            for (CabinClass cabinClass: flightSchedule.getCabinClasses()) {
                if (cabinClass.equals(cabinClassType)) {
                    availableSeats = cabinClass.getNumOfBalanceSeats();
                    for (Fare fare: cabinClass.getCabinClassConfiguration().getFares()) {
                        lowestFare = Math.min(lowestFare, fare.getFareAmount());
                    }
                    break;
                }
            }
            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%8s\n", num, flightSchedule.getFlightNumber(), flightSchedule.getDepartureAirport().getAirportName(), flightSchedule.getDepartureDateTime(), flightSchedule.getFlightDuration(),
                    flightSchedule.getDestinationAirport().getAirportName(), flightSchedule.getArrivalDateTime(), availableSeats,lowestFare);
        }
    }
    
    public void reserveFlight(Integer tripType, Integer numOfPassengers, CabinClassType cabinClassType) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Reservation :: Search Flight :: Reserve Flight***\n");
        System.out.println("Enter Flight Schedule ID to Reserve> ");
        Long flightScheduleId = scanner.nextLong();
        
        Long returnFlightScheduleId = Long.MIN_VALUE; 
        if (tripType == 2) {
            System.out.println("Enter Return Flight Schedule ID to Reserve> ");
            returnFlightScheduleId = scanner.nextLong();
        }
        
        List<String[]> passengers = new ArrayList<>();
        for (int i=1; i<= numOfPassengers; ++i) {
            String[] passenger = new String[4];
            System.out.println("Enter First Name of Passenger " + i + "> ");
            passenger[0] = scanner.nextLine().trim();
            System.out.println("Enter Last Name of Passenger " + i + "> ");
            passenger[1] = scanner.nextLine().trim();
            System.out.println("Enter Passport Number of Passenger " + i + "> ");
            passenger[2] = scanner.nextLine().trim();
            //form of seat number tbc
            System.out.println("Select Seat Number For Passenger " + i + "> ");
            passenger[3] = scanner.nextLine().trim();
            
            passengers.add(passenger);
        }
        
        String[] creditCard = new String[4];
        System.out.println("Enter Credit Card Number> ");
        creditCard[0] = scanner.nextLine().trim();
        System.out.println("Enter Name On the Card> ");
        creditCard[1] = scanner.nextLine().trim();
        System.out.println("Enter Expiry Date> ");
        creditCard[2] = scanner.nextLine().trim();
        System.out.println("Enter CVV Number> ");
        creditCard[3] = scanner.nextLine().trim();
        
        Long newFlightReservationId = flightReservationSessionBeanRemote.reserveFlight(numOfPassengers, passengers, creditCard, cabinClassType, flightScheduleId, returnFlightScheduleId, customer);
        System.out.println("Reserved Successfully! Flight Reservation ID: " + newFlightReservationId + "\n");
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
        System.out.println("Enter First Name> ");
        firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        lastName = scanner.nextLine().trim();
        System.out.println("Enter Email> ");
        email = scanner.nextLine().trim();
        System.out.println("Enter Mobile Phone Number> ");
        phoneNumber = scanner.nextLine().trim();
        System.out.println("Enter Address> ");
        address = scanner.nextLine().trim();
        System.out.println("Enter Username> ");
        username = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
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
        System.out.println("Enter Username> ");
        username = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
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
                System.out.println("> ");

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
        
        System.out.println("Press any key to continue...> ");
        scanner.nextLine();
        
    }
    
    public void viewMyFlightReservationDetails(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** FRS Reservation :: View My Flight Reservation Details ***\n");
        System.out.println("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        FlightReservation currentFlightReservation;
        
        try {
            currentFlightReservation = flightReservationSessionBeanRemote.retrieveFlightReservationByID(reservationId);
            FlightSchedule flightSchedule = currentFlightReservation.getFlightSchedule();
            FlightSchedule returnFlightSchedule = currentFlightReservation.getReturnFlightSchedule();
            
            System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s%18s\n", "   ", "Passenger Name", "Passport Number","Cabin Class", "Seat Number", "Flight Number", "Departure Time", "Duration", "ArrivalTime");
            System.out.println("Departure Flights:");
            Integer num = 0;
            
            for (String[] passenger: currentFlightReservation.getPassengers()) {
                num++;
                //duration tbc
                System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s%18s\n", num, passenger[0] + " " + passenger[1], passenger[2], currentFlightReservation.getCabinClassType(), passenger[3], 
                        flightSchedule.getFlightNumber(), flightSchedule.getDepartureDateTime(),flightSchedule.getFlightDuration(), flightSchedule.getArrivalDateTime());
            }
            
            if (returnFlightSchedule != null) {
                System.out.println("--------------------");
                System.out.println("Return Flights:");
                
                for (String[] passenger: currentFlightReservation.getPassengers()) {
                    num++;
                    //duration tbc
                    System.out.printf("%3s%18s%18s%15s%14s%15s%18s%12s\n", num, passenger[0] + " " + passenger[1], passenger[2], currentFlightReservation.getCabinClassType(), passenger[3],
                            returnFlightSchedule.getFlightNumber(), returnFlightSchedule.getDepartureDateTime(),returnFlightSchedule.getFlightDuration(), returnFlightSchedule.getArrivalDateTime());
                }
            }
        
            System.out.println("Press any key to continue...> ");
            scanner.nextLine();
        
        } catch (FlightReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }         
    }
    
}
