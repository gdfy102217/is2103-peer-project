/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.CabinClassType;


@Entity

public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationId;
    private Integer numOfPassengers;
    //passenger: firstName, lastName, passportNumber, cabinClass, seatNumber
    private List<String[]> passengers;
    //creditCard: cardNumber, nameOnCard, expiryDate, CVV
    private String[] creditCard;
    private String departureAirport;
    private String destinationAirport;
    private Date departureDate;
    @Column(nullable = true)
    private Date returnDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToMany
    private List<FlightSchedule> flightSchedules;
    
    @ManyToMany
    private List<FlightSchedule> returnFlightSchedules;

    public FlightReservation() {
        passengers = new ArrayList<>();
        flightSchedules = new ArrayList<>();
        returnFlightSchedules = new ArrayList<>();
    }

    public FlightReservation(Integer numOfPassengers, List<String[]> passengers, String[] creditCard, String departureAirport, String destinationAirport, Date departureDate, Date returnDate, Customer customer) {
        this.numOfPassengers = numOfPassengers;
        this.passengers = passengers;
        this.creditCard = creditCard;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.customer = customer;
    }

    
    public Long getFlightReservationId() {
        return flightReservationId;
    }

    public void setFlightReservationId(Long flightReservationId) {
        this.flightReservationId = flightReservationId;
    }

    public Integer getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(Integer numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public List<String[]> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String[]> passengers) {
        this.passengers = passengers;
    }

    public String[] getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String[] creditCard) {
        this.creditCard = creditCard;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public List<FlightSchedule> getReturnFlightSchedules() {
        return returnFlightSchedules;
    }

    public void setReturnFlightSchedules(List<FlightSchedule> returnFlightSchedules) {
        this.returnFlightSchedules = returnFlightSchedules;
    }
    
    @XmlTransient
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightReservationId != null ? flightReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightReservationId fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.flightReservationId == null && other.flightReservationId != null) || (this.flightReservationId != null && !this.flightReservationId.equals(other.flightReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FlightReservation{" + "numOfPassengers=" + numOfPassengers + ", passengers=" + passengers + ", creditCard=" + creditCard + 
                 ", customer=" + customer;
    }

    
}
