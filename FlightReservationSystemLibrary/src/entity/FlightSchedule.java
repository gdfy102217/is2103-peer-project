/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;


@Entity

public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date flightDuration;
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalDateTime = null;
    @Column(nullable = false, length = 6, unique = true)
    private String flightNumber;
  
    @OneToOne(optional = false)
    private Airport departureAirport;

    @OneToOne(optional = false)
    private Airport destinationAirport;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    @ManyToMany(mappedBy = "flightSchedules")
    private List<FlightReservation> flightReservations;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<CabinClass> cabinClasses;
    
    

    public FlightSchedule() {
        flightReservations = new ArrayList<>();
        cabinClasses = new ArrayList<>();
    }

    public FlightSchedule(Date departureDate, Date departureTime, Date flightDuration, String flightNumber, Airport departureAirport, Airport destinationAirport, FlightSchedulePlan flightSchedulePlan, List<CabinClass> cabinClasses) {
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.flightSchedulePlan = flightSchedulePlan;
        this.cabinClasses = cabinClasses;
    }

    

    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }


    public Date getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Date flightDuration) {
        this.flightDuration = flightDuration;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }
    
    @XmlTransient
    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Date getArrivalDateTime() {
        if (this.arrivalDateTime == null) {
            calculateArrivalTime();
        }
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
    
    public void calculateArrivalTime(){

        this.arrivalDateTime = new Date(this.arrivalDateTime.getTime() + this.flightDuration.getTime() + 
                (this.destinationAirport.getTimeZone() - this.departureAirport.getTimeZone()) * 60 * 60 * 1000);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        DateFormat departureTimeFormat = new SimpleDateFormat("hh:mm aa");
        DateFormat departureDateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat outputDurationFormat = new SimpleDateFormat("hh Hours mm Minutes");
        String outputDepartDateString = departureDateFormat.format(departureDate);
        String outputDepartTimeString = departureTimeFormat.format(departureTime);
        String flightDurationString = outputDurationFormat.format(this.flightDuration);
        return "[ Departure date = " + outputDepartDateString + ", Departure time = " +  outputDepartTimeString + ", Flight duration = " + flightDurationString + " ]";
    }

    public List<CabinClass> getCabinClasses() {
        return cabinClasses;
    }

    public void setCabinClasses(List<CabinClass> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }
    
}
