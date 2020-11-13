/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Administrator
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private CabinClassSessionBeanRemote cabinClassSessionBeanRemote;

    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            FareSessionBeanRemote fareSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote,
            CabinClassSessionBeanRemote cabinClassSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.cabinClassSessionBeanRemote = cabinClassSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to FRS - FRS Management Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    Employee employee = new Employee();
                    try {
                        employee = doLogin();
                    } catch (EmployeeNotFoundException | InvalidLoginCredentialException ex) {
                        System.out.println(ex.getMessage() + "\n");
                        break;
                    }
                    System.out.println("Login successful!\n");

                    if (employee.getEmployeeType().equals(EmployeeType.FLEETMANAGER)) {
                        FlightPlanningModule flightPlanningModule = new FlightPlanningModule (aircraftConfigurationSessionBeanRemote, airportSessionBeanRemote,
                                flightRouteSessionBeanRemote, aircraftTypeSessionBeanRemote, cabinClassSessionBeanRemote, employee);
                        flightPlanningModule.menuFlightPlanning();
                    } else if (employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER)) {
                        FlightPlanningModule flightPlanningModule = new FlightPlanningModule (aircraftConfigurationSessionBeanRemote, airportSessionBeanRemote,
                                flightRouteSessionBeanRemote, aircraftTypeSessionBeanRemote, cabinClassSessionBeanRemote, employee);
                        flightPlanningModule.menuFlightPlanning();
                    } else if (employee.getEmployeeType().equals(EmployeeType.SCHEDULEMANAGER)) {
                        FlightOperationModule flightOperationModule = new FlightOperationModule(flightSessionBeanRemote, flightRouteSessionBeanRemote,
                                aircraftConfigurationSessionBeanRemote, flightScheduleSessionBeanRemote, flightSchedulePlanSessionBeanRemote, fareSessionBeanRemote, employee);
                        flightOperationModule.menuFlightOperation();
                    } else if (employee.getEmployeeType().equals(EmployeeType.SALESMANAGER)) {
                        SalesManagementModule salesManagementModule = new SalesManagementModule(flightSessionBeanRemote, flightScheduleSessionBeanRemote, employee);
                        salesManagementModule.menuSalesManagement();
                    } 
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private Employee doLogin() throws InvalidLoginCredentialException, EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        Employee employee;

        System.out.println("*** FRS Management :: Employee Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                employee = employeeSessionBeanRemote.employeeLogin(username, password);
            } catch (EmployeeNotFoundException ex) {
                throw new EmployeeNotFoundException("Employee with username typed in is not found!");
            } catch (InvalidLoginCredentialException ex) {
                throw new InvalidLoginCredentialException("Wrong password!");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
        return employee;
    }
}
