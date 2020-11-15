/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity

public class Flight implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    @Column(nullable = false, length = 6, unique = true)
    private String flightNumber;
    
    @OneToOne(optional = true)
    private Flight complementaryReturnFlight;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightRoute flightRoute;
    
    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
    private List<FlightSchedulePlan> flightSchedulePlans;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    private Boolean disabled;

    public Flight() {
        this.flightSchedulePlans = new ArrayList<>();
        this.disabled = false;
    }

    public Flight(String flightNumber) {
        this();
        this.flightNumber = flightNumber;
    }


    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Flight getComplementaryReturnFlight() {
        return complementaryReturnFlight;
    }

    public void setComplementaryReturnFlight(Flight complementaryReturnFlight) {
        this.complementaryReturnFlight = complementaryReturnFlight;
    }

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }
    
    public List<FlightSchedulePlan> getFlightSchedulePlans() {
        return flightSchedulePlans;
    }

    public void setFlightSchedulePlans(List<FlightSchedulePlan> flightSchedulePlans) {
        this.flightSchedulePlans = flightSchedulePlans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightId fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightId == null && other.flightId != null) || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Flight Number = " + flightNumber;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    
}
