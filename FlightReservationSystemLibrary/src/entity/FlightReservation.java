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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.CabinClassType;


@Entity

public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationId;
    private Integer numOfPassengers;
    //passenger: firstName, lastName, passportNumber, seatNumber
    private List<String[]> passengers;
    //creditCard: cardNumber, nameOnCard, expiryDate, CVV
    private String[] creditCard;
    private String flightNumber;
    private Date flightDateTime;
    private String returnFlightNumber = "";
    private Date returnFlightDateTime;
    private CabinClassType cabinClassType;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CabinClass cabinClass;   
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    public FlightReservation() {
        passengers = new ArrayList<>();
    }

    public FlightReservation(Integer numOfPassengers, List<String[]> passengers, String flightNumber, Date flightDateTime, String returnFlightNumber, Date returnFlightDateTime) {
        this.numOfPassengers = numOfPassengers;
        this.passengers = passengers;
        this.flightNumber = flightNumber;
        this.flightDateTime = flightDateTime;
        this.returnFlightNumber = returnFlightNumber;
        this.returnFlightDateTime = returnFlightDateTime;
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

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public FlightSchedule getReturnFlightSchedule() {
        return returnFlightSchedule;
    }

    public void setReturnFlightSchedule(FlightSchedule returnFlightSchedule) {
        this.returnFlightSchedule = returnFlightSchedule;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getFlightDateTime() {
        return flightDateTime;
    }

    public void setFlightDateTime(Date flightDateTime) {
        this.flightDateTime = flightDateTime;
    }

    public String getReturnFlightNumber() {
        return returnFlightNumber;
    }

    public void setReturnFlightNumber(String returnFlightNumber) {
        this.returnFlightNumber = returnFlightNumber;
    }

    public Date getReturnFlightDateTime() {
        return returnFlightDateTime;
    }

    public void setReturnFlightDateTime(Date returnFlightDateTime) {
        this.returnFlightDateTime = returnFlightDateTime;
    }

    public CabinClassType getCabinClassType() {
        return cabinClassType;
    }

    public void setCabinClassType(CabinClassType cabinClassType) {
        this.cabinClassType = cabinClassType;
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
        return "entity.FlightReservation[ id=" + flightReservationId + " ]";
    }
    
}
