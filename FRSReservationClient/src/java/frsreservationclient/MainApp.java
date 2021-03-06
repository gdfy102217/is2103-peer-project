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
import java.util.Locale;
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
import util.exception.NoAvailableSeatsException;
import util.exception.WrongPasswordException;

public class MainApp {

    private Customer customer = null;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to FRS - FRS Reservation Client ***\n");
            System.out.println("1: Search Flight");
            System.out.println("2: Register As Customer");
            System.out.println("3: Customer Login");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.println("> ");

                response = scanner.nextInt();

                if (response == 1) {
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

            if (response == 4) {
                break;
            }
        }
    }

    public void searchFlight() {

        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM", Locale.US);
            Integer tripType;
            String departureAirportiATACode = "";
            String destinationAirportiATACode = "";
            Date departureDate;
            Date returnDate = null;
            Integer numOfPassengers;
            Integer flightTypePreference;
            Integer cabinClass;

            System.out.println("*** FRS Reservation :: Search Flight ***\n");
            System.out.print("Enter Trip Type:  1: One-way, 2: Round-trip > ");
            tripType = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Departure Airport IATA Code> ");
            departureAirportiATACode = scanner.nextLine().trim();
            System.out.print("Enter Destination Airport IATA Code> ");
            destinationAirportiATACode = scanner.nextLine().trim();
            System.out.print("Enter Departure Date (dd MMM)> ");
            departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            if (tripType == 2) {
                System.out.println("Enter Return Date (dd MMM)> ");
                returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            }
            System.out.print("Enter Number Of Passengers> ");
            numOfPassengers = scanner.nextInt();
            System.out.print("Enter Flight Type Preference:  1: Direct Flight, 2: Connecting Flight, 3: No Preference > ");
            flightTypePreference = scanner.nextInt();
            System.out.print("Enter Cabin Class Preference:  1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class, 5: No Preference > ");
            cabinClass = scanner.nextInt();

            CabinClassType cabinClassType = null;
            if (cabinClass == 1) {
                cabinClassType = CabinClassType.FIRSTCLASS;
            } else if (cabinClass == 2) {
                cabinClassType = CabinClassType.BUSINESSCLASS;
            } else if (cabinClass == 3) {
                cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
            } else if (cabinClass == 4) {
                cabinClassType = CabinClassType.ECONOMYCLASS;
            }

            if (flightTypePreference == 1) {
                searchDirectFlight(departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, numOfPassengers, tripType, cabinClassType);
            } else if (flightTypePreference == 2) {
                searchConnectingFlights(departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, numOfPassengers, tripType, cabinClassType);
            } else {
                searchDirectFlight(departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, numOfPassengers, tripType, cabinClassType);
                searchConnectingFlights(departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, numOfPassengers, tripType, cabinClassType);
            }

            scanner.nextLine();
            System.out.print("Do you wish to reserve a flight? (Y/N) > ");
            String reserve = scanner.nextLine().trim();

            if (reserve.equals("Y")) {
                reserveFlight(tripType, numOfPassengers);
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        } catch (AirportNotFoundException | FlightScheduleNotFountException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println();
        }
    }

    public void searchDirectFlight(String departureAirportiATACode, String destinationAirportiATACode, Date departureDate, Date returnDate, Integer numOfPassengers,
            Integer tripType, CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException {
        try {
            System.out.println("Departure Flight Information :: Direct Flight\n");
            //on required departure date
            List<FlightSchedule> flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(departureAirportiATACode, destinationAirportiATACode, departureDate, cabinClassType);

            if (flightSchedules.isEmpty()) {
                System.out.println("No available flights departing on " + departureDate + " !\n");
            } else {
                System.out.println("----- Departure On " + departureDate + "\n");
                printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
            }

            //3 days before
            for (int i = 3; i > 0; --i) {
                Date newDepartureDate = new Date(departureDate.getTime() - i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(departureAirportiATACode, destinationAirportiATACode, newDepartureDate, cabinClassType);
                if (flightSchedules.isEmpty()) {
                    System.out.println("No available flights departing on " + newDepartureDate + " !\n");
                } else {
                    System.out.println("----- Departure On " + departureDate + "\n");
                    printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                }
            }

            //3 days after
            for (int i = 1; i < 4; ++i) {
                Date newDepartureDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(departureAirportiATACode, destinationAirportiATACode, departureDate, cabinClassType);
                if (flightSchedules.isEmpty()) {
                    System.out.println("No available flights departing on " + newDepartureDate + " !\n");
                } else {
                    System.out.println("----- Departure On " + departureDate + "\n");
                    printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                }
            }

            if (tripType == 2) {
                System.out.println("\nReturn Flight Information :: Direct Flights\n");
                //on required departure date
                flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(destinationAirportiATACode, departureAirportiATACode, returnDate, cabinClassType);
                if (flightSchedules.isEmpty()) {
                    System.out.println("No available flights departing on " + returnDate + " !\n");
                } else {
                    System.out.println("----- Return On " + returnDate + "\n");
                    printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                }

                //3 days before
                for (int i = 3; i > 0; --i) {
                    Date newReturnDate = new Date(returnDate.getTime() - i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(destinationAirportiATACode, departureAirportiATACode, newReturnDate, cabinClassType);
                    if (flightSchedules.isEmpty()) {
                        System.out.println("No available flights departing on " + newReturnDate + " !\n");
                    } else {
                        System.out.println("----- Return On " + newReturnDate + "\n");
                        printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                    }
                }

                //3 days after
                for (int i = 1; i < 4; ++i) {
                    Date newReturnDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchDirectFlightScehdules(destinationAirportiATACode, departureAirportiATACode, newReturnDate, cabinClassType);
                    if (flightSchedules.isEmpty()) {
                        System.out.println("No available flights departing on " + newReturnDate + " !\n");
                    } else {
                        System.out.println("----- Return On " + newReturnDate + "\n");
                        printDirectFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);

                    }
                }
            }
        } catch (AirportNotFoundException | FlightScheduleNotFountException ex) {
            throw ex;
        }
    }

    public void searchConnectingFlights(String departureAirportiATACode, String destinationAirportiATACode, Date departureDate, Date returnDate, Integer numOfPassengers, Integer tripType, CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException {
        try {
            System.out.println("\nDeparture Flight Information :: Connecting Flights\n");
            //on required departure date
            List<List<FlightSchedule>> flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(departureAirportiATACode, destinationAirportiATACode, departureDate, cabinClassType);
            System.out.println("----- Departure On " + departureDate + "\n");
            printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);

            //3 days before
            for (int i = 3; i > 0; --i) {
                Date newDepartureDate = new Date(departureDate.getTime() - i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(departureAirportiATACode, destinationAirportiATACode, newDepartureDate, cabinClassType);
                System.out.println("----- Departure On " + departureDate + "\n");
                printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
            }

            //3 days after
            for (int i = 1; i < 4; ++i) {
                Date newDepartureDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(departureAirportiATACode, destinationAirportiATACode, departureDate, cabinClassType);
                System.out.println("----- Departure On " + departureDate + "\n");
                printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
            }

            if (tripType == 2) {
                System.out.println("\nReturn Flight Information: \n");
                //on required departure date
                flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(destinationAirportiATACode, departureAirportiATACode, returnDate, cabinClassType);
                System.out.println("----- Return On " + returnDate + "\n");
                printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);

                //3 days before
                for (int i = 3; i > 0; --i) {
                    Date newReturnDate = new Date(returnDate.getTime() - i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(destinationAirportiATACode, departureAirportiATACode, newReturnDate, cabinClassType);
                    System.out.println("----- Return On " + returnDate + "\n");
                    printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                }

                //3 days after
                for (int i = 1; i < 4; ++i) {
                    Date newReturnDate = new Date(departureDate.getTime() + i * 24 * 60 * 60 * 1000);
                    flightSchedules = flightScheduleSessionBeanRemote.searchConnectingFlightScehdules(destinationAirportiATACode, departureAirportiATACode, newReturnDate, cabinClassType);
                    System.out.println("----- Return On " + returnDate + "\n");
                    printConnectingFlightSchedulesTable(flightSchedules, cabinClassType, numOfPassengers);
                }
            }
        } catch (AirportNotFoundException | FlightScheduleNotFountException ex) {

            throw ex;

        }
    }

    public void printDirectFlightSchedulesTable(List<FlightSchedule> flightSchedules, CabinClassType cabinClassType, Integer numOfPassengers) {

        System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%37s%37s%37s%8s\n", "   ", "Flight Schdule ID", "Flight Number", "Departure Airport", "Departure Time (Local Time)",
                "Flight Duration", "Destination Airport", "Arrival Time (Local Time)", "First Class Available Seats", "First Class Price", "Business Class Available Seats",
                "Business Class Price", "Premium Economy Class Available Seats", "Premium Economy Class Price", "Economy Class Available Seats", "Economy Class Price");

        String firstClassAvailableSeats = "/";
        String businessClassAvailableSeats = "/";
        String premiumEcoClassAvailableSeats = "/";
        String economyClassAvailableSeats = "/";

        Double lowestFareFirstClass = Double.MAX_VALUE;
        Double lowestFareBusinessClass = Double.MAX_VALUE;
        Double lowestFarePremiumEconomyClass = Double.MAX_VALUE;
        Double lowestFareEconomyClass = Double.MAX_VALUE;

        Integer num = 1;
        for (FlightSchedule flightSchedule : flightSchedules) {
            for (CabinClass cabinClass : flightSchedule.getCabinClasses()) {
                if (cabinClassType.equals(CabinClassType.FIRSTCLASS)) {
                    firstClassAvailableSeats = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareFirstClass = Math.min(lowestFareFirstClass, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.BUSINESSCLASS)) {
                    businessClassAvailableSeats = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareBusinessClass = Math.min(lowestFareBusinessClass, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.PREMIUMECONOMYCLASS)) {
                    premiumEcoClassAvailableSeats = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFarePremiumEconomyClass = Math.min(lowestFarePremiumEconomyClass, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.ECONOMYCLASS)) {
                    economyClassAvailableSeats = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareEconomyClass = Math.min(lowestFareEconomyClass, fare.getFareAmount());
                    }
                }
            }

            String firstClassFare;
            String businessClassFare;
            String premiumEconomyClassFare;
            String economyClassFare;

            if (lowestFareFirstClass == Double.MAX_VALUE) {
                firstClassFare = "/";
            } else {
                firstClassFare = lowestFareFirstClass.toString();
            }
            if (lowestFareBusinessClass == Double.MAX_VALUE) {
                businessClassFare = "/";
            } else {
                businessClassFare = lowestFareBusinessClass.toString();
            }
            if (lowestFarePremiumEconomyClass == Double.MAX_VALUE) {
                premiumEconomyClassFare = "/";
            } else {
                premiumEconomyClassFare = lowestFarePremiumEconomyClass.toString();
            }
            if (lowestFareEconomyClass == Double.MAX_VALUE) {
                economyClassFare = "/";
            } else {
                economyClassFare = lowestFareEconomyClass.toString();
            }

            System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%37s%37s%37s%8s\n", num, flightSchedule.getFlightScheduleId(), flightSchedule.getFlightNumber(), flightSchedule.getDepartureAirport().getIataAirportcode(), flightSchedule.getDepartureDate(), flightSchedule.getDepartureTime(), flightSchedule.getFlightDuration(),
        flightSchedule.getDestinationAirport().getIataAirportcode(), flightSchedule.getArrivalDateTime(), firstClassAvailableSeats,firstClassFare, businessClassAvailableSeats, businessClassFare, premiumEcoClassAvailableSeats, premiumEconomyClassFare, economyClassAvailableSeats, economyClassFare);

            num++;
        }
    }

    public void printConnectingFlightSchedulesTable(List<List<FlightSchedule>> flightSchedules, CabinClassType cabinClassType, Integer numOfPassengers) {

        System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%37s%37s%37s%8s\n", "   ", "Flight Schdule ID", "Flight Number", "Departure Airport", "Departure Time (Local Time)",
                "Flight Duration", "Destination Airport", "Arrival Time (Local Time)", "First Class Available Seats", "First Class Price", "Business Class Available Seats",
                "Business Class Price", "Premium Economy Class Available Seats", "Premium Economy Class Price", "Economy Class Available Seats", "Economy Class Price");

        String firstClassAvailableSeats1 = "/";
        String businessClassAvailableSeats1 = "/";
        String premiumEcoClassAvailableSeats1 = "/";
        String economyClassAvailableSeats1 = "/";
        String firstClassAvailableSeats2 = "/";
        String businessClassAvailableSeats2 = "/";
        String premiumEcoClassAvailableSeats2 = "/";
        String economyClassAvailableSeats2 = "/";

        Double lowestFareFirstClass1 = Double.MAX_VALUE;
        Double lowestFareBusinessClass1 = Double.MAX_VALUE;
        Double lowestFarePremiumEconomyClass1 = Double.MAX_VALUE;
        Double lowestFareEconomyClass1 = Double.MAX_VALUE;
        Double lowestFareFirstClass2 = Double.MAX_VALUE;
        Double lowestFareBusinessClass2 = Double.MAX_VALUE;
        Double lowestFarePremiumEconomyClass2 = Double.MAX_VALUE;
        Double lowestFareEconomyClass2 = Double.MAX_VALUE;

        Integer num = 1;
        for (List<FlightSchedule> firstFlightSchedules : flightSchedules) {
            FlightSchedule firstFlightSchedule = firstFlightSchedules.remove(0);
            for (CabinClass cabinClass : firstFlightSchedule.getCabinClasses()) {
                if (cabinClassType.equals(CabinClassType.FIRSTCLASS)) {
                    firstClassAvailableSeats1 = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareFirstClass1 = Math.min(lowestFareFirstClass1, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.BUSINESSCLASS)) {
                    businessClassAvailableSeats1 = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareBusinessClass1 = Math.min(lowestFareBusinessClass1, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.PREMIUMECONOMYCLASS)) {
                    premiumEcoClassAvailableSeats1 = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFarePremiumEconomyClass1 = Math.min(lowestFarePremiumEconomyClass1, fare.getFareAmount());
                    }
                }
                if (cabinClassType.equals(CabinClassType.ECONOMYCLASS)) {
                    economyClassAvailableSeats1 = cabinClass.getNumOfBalanceSeats().toString();
                    for (Fare fare : cabinClass.getFares()) {
                        lowestFareEconomyClass1 = Math.min(lowestFareEconomyClass1, fare.getFareAmount());
                    }
                }
            }

            String firstClassFare1;
            String businessClassFare1;
            String premiumEconomyClassFare1;
            String economyClassFare1;

            if (lowestFareFirstClass1 == Double.MAX_VALUE) {
                firstClassFare1 = "/";
            } else {
                firstClassFare1 = lowestFareFirstClass1.toString();
            }
            if (lowestFareBusinessClass1 == Double.MAX_VALUE) {
                businessClassFare1 = "/";
            } else {
                businessClassFare1 = lowestFareBusinessClass1.toString();
            }
            if (lowestFarePremiumEconomyClass1 == Double.MAX_VALUE) {
                premiumEconomyClassFare1 = "/";
            } else {
                premiumEconomyClassFare1 = lowestFarePremiumEconomyClass1.toString();
            }
            if (lowestFareEconomyClass1 == Double.MAX_VALUE) {
                economyClassFare1 = "/";
            } else {
                economyClassFare1 = lowestFareEconomyClass1.toString();
            }

            for (FlightSchedule secondFlightSchedule : firstFlightSchedules) {
                for (CabinClass cabinClass : secondFlightSchedule.getCabinClasses()) {
                    if (cabinClassType.equals(CabinClassType.FIRSTCLASS)) {
                        firstClassAvailableSeats2 = cabinClass.getNumOfBalanceSeats().toString();
                        for (Fare fare : cabinClass.getFares()) {
                            lowestFareFirstClass2 = Math.min(lowestFareFirstClass2, fare.getFareAmount());
                        }
                    }
                    if (cabinClassType.equals(CabinClassType.BUSINESSCLASS)) {
                        businessClassAvailableSeats2 = cabinClass.getNumOfBalanceSeats().toString();
                        for (Fare fare : cabinClass.getFares()) {
                            lowestFareBusinessClass2 = Math.min(lowestFareBusinessClass2, fare.getFareAmount());
                        }
                    }
                    if (cabinClassType.equals(CabinClassType.PREMIUMECONOMYCLASS)) {
                        premiumEcoClassAvailableSeats2 = cabinClass.getNumOfBalanceSeats().toString();
                        for (Fare fare : cabinClass.getFares()) {
                            lowestFarePremiumEconomyClass2 = Math.min(lowestFarePremiumEconomyClass2, fare.getFareAmount());
                        }
                    }
                    if (cabinClassType.equals(CabinClassType.ECONOMYCLASS)) {
                        economyClassAvailableSeats2 = cabinClass.getNumOfBalanceSeats().toString();
                        for (Fare fare : cabinClass.getFares()) {
                            lowestFareEconomyClass2 = Math.min(lowestFareEconomyClass2, fare.getFareAmount());
                        }
                    }
                }

                String firstClassFare2;
                String businessClassFare2;
                String premiumEconomyClassFare2;
                String economyClassFare2;

                if (lowestFareFirstClass2 == Double.MAX_VALUE) {
                    firstClassFare2 = "/";
                } else {
                    firstClassFare2 = lowestFareFirstClass2.toString();
                }
                if (lowestFareBusinessClass2 == Double.MAX_VALUE) {
                    businessClassFare2 = "/";
                } else {
                    businessClassFare2 = lowestFareBusinessClass2.toString();
                }
                if (lowestFarePremiumEconomyClass2 == Double.MAX_VALUE) {
                    premiumEconomyClassFare2 = "/";
                } else {
                    premiumEconomyClassFare2 = lowestFarePremiumEconomyClass2.toString();
                }
                if (lowestFareEconomyClass2 == Double.MAX_VALUE) {
                    economyClassFare2 = "/";
                } else {
                    economyClassFare2 = lowestFareEconomyClass2.toString();
                }

                System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%37s%37s%37s%8s\n", num, firstFlightSchedule.getFlightScheduleId(), firstFlightSchedule.getFlightNumber(), firstFlightSchedule.getDepartureAirport().getAirportName(), firstFlightSchedule.getDepartureDate(), firstFlightSchedule.getDepartureTime(), firstFlightSchedule.getFlightDuration(),
                        firstFlightSchedule.getDestinationAirport().getIataAirportcode(), firstFlightSchedule.getArrivalDateTime(), firstClassAvailableSeats1, firstClassFare1, businessClassAvailableSeats1, businessClassFare1, premiumEcoClassAvailableSeats1, premiumEconomyClassFare1, economyClassAvailableSeats1, economyClassFare1);

                System.out.printf("%3s%15s%15s%35s%30s%18s%35s%30s%37s%37s%37s%37s%8s\n", num, secondFlightSchedule.getFlightScheduleId(), secondFlightSchedule.getFlightNumber(), secondFlightSchedule.getDepartureAirport().getAirportName(), secondFlightSchedule.getDepartureDate(), secondFlightSchedule.getDepartureTime(), secondFlightSchedule.getFlightDuration(),
                        secondFlightSchedule.getDestinationAirport().getIataAirportcode(), secondFlightSchedule.getArrivalDateTime(), firstClassAvailableSeats2, firstClassFare2, businessClassAvailableSeats2, businessClassFare2, premiumEcoClassAvailableSeats2, premiumEconomyClassFare2, economyClassAvailableSeats2, economyClassFare2);

                num++;
            }

        }
    }

    public void reserveFlight(Integer tripType, Integer numOfPassengers) {
        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            System.out.println("*** FRS Reservation :: Search Flight :: Reserve Flight***\n");

            List<Long> flightScheduleIds = new ArrayList<>();
            List<Long> returnFlightScheduleIds = new ArrayList<>();

            System.out.print("Enter Departure Airport IATA Code> ");
            String departureAirportiATACode = scanner.nextLine().trim();
            System.out.print("Enter Destination Airport IATA Code> ");
            String destinationAirportiATACode = scanner.nextLine().trim();
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            Date departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            Date returnDate = null;
            if (tripType == 2) {
                System.out.print("Enter Return Date (dd/mm/yyyy)> ");
                returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            }

            boolean next = true;
            while (next) {
                System.out.print("Enter Flight Schedule ID to Reserve> ");
                flightScheduleIds.add(scanner.nextLong());

                System.out.print("More Flights to Reserve? Y/N> ");
                String option = scanner.nextLine().trim();

                if (option.charAt(0) == 'N') {
                    next = false;
                }

            }

            next = true;
            if (tripType == 2) {
                while (next) {
                    System.out.print("Enter Return Flight Schedule ID to Reserve> ");
                    returnFlightScheduleIds.add(scanner.nextLong());

                    System.out.print("More Flights to Reserve? Y/N> ");
                    String option = scanner.nextLine().trim();

                    if (option.charAt(0) == 'N') {
                        next = false;
                    }
                }
            }

            List<String[]> passengers = new ArrayList<>();
            for (int i = 1; i <= numOfPassengers; ++i) {
                String[] passenger = new String[5];
                System.out.print("Enter First Name of Passenger " + i + "> ");
                passenger[0] = scanner.nextLine().trim();
                System.out.print("Enter Last Name of Passenger " + i + "> ");
                passenger[1] = scanner.nextLine().trim();
                System.out.print("Enter Passport Number of Passenger " + i + "> ");
                passenger[2] = scanner.nextLine().trim();
                System.out.print("Select Cabin Class For Passenger " + i + ": 1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class> ");
                passenger[3] = scanner.nextLine().trim();
                System.out.print("Select Seat Number For Passenger " + i + "> ");
                passenger[4] = scanner.nextLine().trim();

                passengers.add(passenger);
            }

            String[] creditCard = new String[4];
            System.out.print("Enter Credit Card Number> ");
            creditCard[0] = scanner.nextLine().trim();
            System.out.print("Enter Name On the Card> ");
            creditCard[1] = scanner.nextLine().trim();
            System.out.print("Enter Expiry Date> ");
            creditCard[2] = scanner.nextLine().trim();
            System.out.print("Enter CVV Number> ");
            creditCard[3] = scanner.nextLine().trim();

            try {
                Long newFlightReservationId = flightReservationSessionBeanRemote.reserveFlight(numOfPassengers, passengers, creditCard,
                        flightScheduleIds, returnFlightScheduleIds, departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, customer);
                System.out.println("Reserved Successfully! Flight Reservation ID: " + newFlightReservationId + "\n");

            } catch (NoAvailableSeatsException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println();
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }

    }

    public void register() {
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

    public void login() {
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
                System.out.println("Error: " + ex.getMessage());
            }
        } else {
            System.out.println("Incomplete information, please try again!\n");
        }
    }

    public void menuMain() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Reservation :: Main Menu ***\n");
            System.out.println("1: Search Flight");
            System.out.println("2: View My Flight Reservations");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.println("> ");

                response = scanner.nextInt();

                if (response == 1) {
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

            if (response == 4) {
                break;
            }
        }

    }

    public void viewMyFlightReservations() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS Reservation :: View My Flight Reservations ***\n");
        List<FlightReservation> flightReservations = customer.getFlightReservations();
        if (flightReservations.isEmpty()) {
            System.out.println("No available reservations!");
        } else {
            System.out.printf("%3s%21s%15s%20s%20s%15s\n", "   ", "Flight Reservation ID", "Departure Date", "Departure Airport", "Destination Airport", "Return Date");
            Integer num = 0;

            for (FlightReservation flightReservation : customer.getFlightReservations()) {
                num++;
                System.out.printf("%3s%21s%15s%20s%15s%20s\n", num, flightReservation.getFlightReservationId(), flightReservation.getDepartureDate(), flightReservation.getDepartureAirport(), flightReservation.getDestinationAirport(), flightReservation.getReturnDate());
            }

            System.out.println("Press any key to continue...> ");
            scanner.nextLine();
        }

    }

    public void viewMyFlightReservationDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS Reservation :: View My Flight Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        FlightReservation currentFlightReservation;

        try {
            currentFlightReservation = flightReservationSessionBeanRemote.retrieveFlightReservationByID(reservationId);
            List<FlightSchedule> flightSchedules = currentFlightReservation.getFlightSchedules();
            List<FlightSchedule> returnFlightSchedules = currentFlightReservation.getReturnFlightSchedules();

            System.out.printf("%3s%18s%18s%15s%14s%15s%18s%20s%12s%18s%20s\n", "   ", "Passenger Name", "Passport Number", "Cabin Class", "Seat Number", "Flight Number", "Departure Time", "Departure Airport", "Duration", "ArrivalTime", "Destination Airport");
            System.out.println("Departure Flights:");
            Integer num = 0;

            for (FlightSchedule flightSchedule : flightSchedules) {
                for (String[] passenger : currentFlightReservation.getPassengers()) {
                    num++;
                    //duration tbc
                    System.out.printf("%3s%18s%18s%15s%14s%15s%18s%20s%12s%18s%20s\n", num, passenger[0] + " " + passenger[1], passenger[2], passenger[3], passenger[4], 
                            flightSchedule.getFlightNumber(), flightSchedule.getDepartureDate(), flightSchedule.getDepartureTime(), flightSchedule.getDepartureAirport().getIataAirportcode(),flightSchedule.getFlightDuration(), 
                            flightSchedule.getArrivalDateTime(), flightSchedule.getDestinationAirport().getIataAirportcode());
                }
            }

            if (!returnFlightSchedules.isEmpty()) {
                System.out.println("--------------------");
                System.out.println("Return Flights:");

                for (FlightSchedule returnFlightSchedule : returnFlightSchedules) {
                    for (String[] passenger : currentFlightReservation.getPassengers()) {
                        num++;
                        //duration tbc
                        System.out.printf("%3s%18s%18s%15s%14s%15s%18s%20s%12s%18s%20s\n", num, passenger[0] + " " + passenger[1], passenger[2], passenger[3], passenger[4],
                                returnFlightSchedule.getFlightNumber(), returnFlightSchedule.getDepartureDate(), returnFlightSchedule.getDepartureTime(), returnFlightSchedule.getDepartureAirport().getIataAirportcode(), 
                                returnFlightSchedule.getFlightDuration(), returnFlightSchedule.getArrivalDateTime(), returnFlightSchedule.getDestinationAirport().getIataAirportcode());
                    }
                }
            }

            System.out.println("Press any key to continue...> ");
            scanner.nextLine();

        } catch (FlightReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
    }

}
