/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.EmployeeType;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Administrator
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    public MainApp() {
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
                        employee = doLogin(); // Reserved for future use
                    } catch (EmployeeNotFoundException | InvalidLoginCredentialException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                    System.out.println("Login successful!\n");

                    if (employee.getEmployeeType().equals(EmployeeType.FLEETMANAGER)) {
                        flightPlanningModule();
                    } else if (employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER)) {
                        flightPlanningModule();
                    } else if (employee.getEmployeeType().equals(EmployeeType.SCHEDULEMANAGER)) {
                        flightOperationModule();
                    } else if (employee.getEmployeeType().equals(EmployeeType.SALESMANAGER)) {
                        salesManagementModule();
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
