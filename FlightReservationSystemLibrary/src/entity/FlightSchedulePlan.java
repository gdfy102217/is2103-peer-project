/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.FlightScheduleType;


@Entity

public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    @Column(nullable = false)
    private FlightScheduleType flightScheduleType;
    
    //only for n days recurrence
    private Integer recurrence;
    
    //(Monday - Sunday) Only for reccurent by week
    private Date reccurrentDay;
    private Date startDate;
    private Date endDate;
    private Date layoverDuration;
    
    @OneToOne(optional = true)
    private FlightSchedulePlan complementaryReturnSchedulePlan;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
    
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightSchedule> flightSchedules;
    
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Fare> fares;
    
    private Long firstDepartureTimeLong;
    
    private Boolean disabled;

    public FlightSchedulePlan() {
        flightSchedules = new ArrayList<>();
        this.disabled = false;
    }

    public FlightSchedulePlan(FlightScheduleType flightScheduleType) {
        this.flightScheduleType = flightScheduleType;
    }

    public FlightSchedulePlan(FlightScheduleType flightScheduleType, List<Fare> fares, Long firstDepartureTimeLong) {
        this.flightScheduleType = flightScheduleType;
        this.fares = fares;
        this.firstDepartureTimeLong = firstDepartureTimeLong;
    }
    
   

    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    public FlightScheduleType getFlightScheduleType() {
        return flightScheduleType;
    }

    public void setFlightScheduleType(FlightScheduleType flightScheduleType) {
        this.flightScheduleType = flightScheduleType;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public Date getLayoverDuration() {
        return layoverDuration;
    }

    public void setLayoverDuration(Date layoverDuration) {
        this.layoverDuration = layoverDuration;
    }

    public FlightSchedulePlan getComplementaryReturnSchedulePlan() {
        return complementaryReturnSchedulePlan;
    }

    public void setComplementaryReturnSchedulePlan(FlightSchedulePlan complementaryReturnSchedulePlan) {
        this.complementaryReturnSchedulePlan = complementaryReturnSchedulePlan;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat outputEndDateFormat = new SimpleDateFormat("dd MMM yy", Locale.US);
        SimpleDateFormat outputLayoverFormat = new SimpleDateFormat("hh Hours mm Minutes");
        String endDateString = outputEndDateFormat.format(endDate);
        String layoverString = outputLayoverFormat.format(layoverDuration);
        if (this.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYWEEK)) {
            return "[Flight = " + flight + "Flight Schedule Type = " + flightScheduleType + ", End Date = " + endDateString + ", Layover Duration = " + layoverString +  ']';
        } else if (this.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYDAY)) {
            return "[Flight = " + flight + "Flight Schedule Type = " + flightScheduleType + ", Recurrence = " + recurrence + ", End Date = " + endDateString + ", Layover Duration = " + layoverString +  ']';
        } else {
            return "[Flight = " + flight + "Flight Schedule Type = " + flightScheduleType + ", Layover Duration = " + layoverString +  ']';
        }
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    public Integer getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Integer recurrence) {
        this.recurrence = recurrence;
    }

    public Long getFirstDepartureTime() {
        Long firstDepartureTime = Long.MAX_VALUE;
        for(FlightSchedule flightSchedule: this.flightSchedules) {
            if (flightSchedule.getDepartureTime().getTime() < firstDepartureTime) {
                firstDepartureTime = flightSchedule.getDepartureTime().getTime();
            }
        }
        return firstDepartureTime;
    }

    public void setFirstDepartureTime(Long firstDepartureTime) {
        this.setFirstDepartureTimeLong(firstDepartureTime);
    }

    public Date getReccurrentDay() {
        return reccurrentDay;
    }

    public void setReccurrentDay(Date reccurrentDay) {
        this.reccurrentDay = reccurrentDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getFirstDepartureTimeLong() {
        return firstDepartureTimeLong;
    }

    public void setFirstDepartureTimeLong(Long firstDepartureTimeLong) {
        this.firstDepartureTimeLong = firstDepartureTimeLong;
    }
    
}
