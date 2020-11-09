/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Employee;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassType;
import util.exception.ExceedMaximumSeatCapacityException;

/**
 *
 * @author Administrator
 */
public class FlightPlanningModule {
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    
    private Employee employee;

    public FlightPlanningModule() {
    }
    
    public void menuFlightPlanning() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** FRSManagement :: Flight Planning Module ***\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View Staff Details");
            System.out.println("3: View All Staffs");
            System.out.println("-----------------------");
            System.out.println("4: Create New Product");
            System.out.println("5: View Product Details");
            System.out.println("6: View All Products");
            System.out.println("-----------------------");
            System.out.println("7: Back\n");
            response = 0;
            
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try {
                        doCreateNewAircraftConfiguration();
                    } catch (ExceedMaximumSeatCapacityException ex) {
                        System.out.println("The New Aircraft Configuration Exceed MaximumSeat Capacity of the aircraft");
                    }
                }
                else if(response == 2)
                {
                    viewAllAircraftConfigurations();
                }
                else if(response == 3)
                {
                    viewAircraftConfigurationDetails();
                }
                else if(response == 4)
                {
                    doCreateNewProduct();
                }
                else if(response == 5)
                {
                    doViewProductDetails();
                }
                else if(response == 6)
                {
                    doViewAllProducts();
                }
                else if (response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    
    private void doCreateNewAircraftConfiguration() throws ExceedMaximumSeatCapacityException {
        Scanner scanner = new Scanner(System.in);
        AircraftConfiguration newAircraftConfiguration = new AircraftConfiguration();
        
        System.out.println("*** FRSManagement :: Flight Planning :: Create New Aircraft Configuration ***\n");
        
        System.out.print("Enter Aircraft Configuration Name> ");
        newAircraftConfiguration.setAircraftConfigurationName(scanner.nextLine().trim());
        
        Integer numOfCabinClasses = 0;
        while (numOfCabinClasses < 1 || numOfCabinClasses > 4) {
            System.out.print("Enter Number of Cabin Classes (1 to 4)> ");
            numOfCabinClasses = Integer.valueOf(scanner.nextLine().trim());
        }
        
        //to create each cabin class configuration
        Integer totalMaximumSeatCapacity = 0;
        for (int i = 0; i < numOfCabinClasses; i++) {
            CabinClassConfiguration newCabinClassConfiguration = new CabinClassConfiguration();
            Integer cabinClassTypeSelection = 0;
            while (numOfCabinClasses < 1 || numOfCabinClasses > 4) {
                System.out.println("Select Cabin Class Type to be created> ");
                System.out.println("1: First Class");
                System.out.println("2: Business Class");
                System.out.println("3: Premium Economy Class");
                System.out.println("4: Economy Class");
                cabinClassTypeSelection = Integer.valueOf(scanner.nextLine().trim());
            }
            switch(cabinClassTypeSelection) {
                case 1:
                    newCabinClassConfiguration.setCabinClassType(CabinClassType.FIRSTCLASS);
                    break;
                case 2:
                    newCabinClassConfiguration.setCabinClassType(CabinClassType.BUSINESSCLASS);
                    break;
                case 3:
                    newCabinClassConfiguration.setCabinClassType(CabinClassType.PREMIUMECONOMYCLASS);
                    break;
                case 4:
                    newCabinClassConfiguration.setCabinClassType(CabinClassType.ECONOMYCLASS);
                    break;
            }
            System.out.print("Enter Number of Aisles> ");
            newCabinClassConfiguration.setNumOfAisles(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Number of Rows> ");
            newCabinClassConfiguration.setNumOfRows(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Number of Seats Abreast> ");
            newCabinClassConfiguration.setNumOfSeatsAbreast(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Seating Configuration Per Column> ");
            newCabinClassConfiguration.setSeatingConfigurationPerColumn(scanner.nextLine().trim());
            
            System.out.println("The maximum seat capacity for this cabin class is " + newCabinClassConfiguration.getMaxSeatCapacity());
            totalMaximumSeatCapacity += newCabinClassConfiguration.getMaxSeatCapacity();
            System.out.println("The maximum seat capacity for this cabin class configuration is " + totalMaximumSeatCapacity);
            if (totalMaximumSeatCapacity > newAircraftConfiguration.getAircraftType().getMaxPassengerSeatCapacity()) {
                throw new ExceedMaximumSeatCapacityException();
            }
            newAircraftConfiguration.getCabinClassConfigurations().add(newCabinClassConfiguration);
        }
    }
    
    private void viewAllAircraftConfigurations() {
        System.out.println("*** FRSManagement :: Flight Planning :: View All Aircraft Configurations ***\n");
        for (AircraftConfiguration aircraftConfiguration: aircraftConfigurationSessionBeanRemote.viewAllAircraftConfigurations()){
            System.out.println(aircraftConfiguration);
        }
    }
    
    private void viewAircraftConfigurationDetails() {
        
    }
}
