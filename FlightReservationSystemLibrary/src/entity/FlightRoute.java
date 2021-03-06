/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport origin;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport destination;
    
    @OneToOne(optional = true)
    private FlightRoute complementaryReturnRoute;
    
    @OneToMany(mappedBy = "flightRoute", fetch = FetchType.EAGER)
    private List<Flight> flights;
    
    private Boolean disabled;

    public FlightRoute() {
        this.flights = new ArrayList<>();
        this.disabled = false;
    }

    public FlightRoute(Airport origin, Airport destination) {
        this();
        this.origin = origin;
        this.destination = destination;
    }
    
    

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public FlightRoute getComplementaryReturnRoute() {
        return complementaryReturnRoute;
    }

    public void setComplementaryReturnRoute(FlightRoute complementaryReturnRoute) {
        this.complementaryReturnRoute = complementaryReturnRoute;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightRouteId fields are not set
        if (!(object instanceof FlightRoute)) {
            return false;
        }
        FlightRoute other = (FlightRoute) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FlightRoute [" + "Origin = " + getOrigin().getIataAirportcode() + ", Destination = " + getDestination().getIataAirportcode() + ']';
    }

    

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
}
