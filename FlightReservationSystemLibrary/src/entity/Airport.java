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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity

public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;
    @Column(nullable = false, length = 64)
    private String airportName;
    @Column(nullable = false, length = 3, unique = true)
    private String iataAirportcode;
    @Column(nullable = false)
    private String city;
    private String stateOrProvince;
    @Column(nullable = false)
    private String country;
    
    @OneToMany(mappedBy = "origin")
    private List<FlightRoute> flightsFromAirport;
    @OneToMany(mappedBy = "destination")
    private List<FlightRoute> flightsToAirport;

    public Airport() {
        flightsFromAirport = new ArrayList<>();
        flightsToAirport = new ArrayList<>();
    }

    public Airport(String airportName, String iataAirportcode, String city, String state, String country) {
        this();
        this.airportName = airportName;
        this.iataAirportcode = iataAirportcode;
        this.city = city;
        this.stateOrProvince = state;
        this.country = country;
    }
    
    

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getIataAirportcode() {
        return iataAirportcode;
    }

    public void setIataAirportcode(String iataAirportcode) {
        this.iataAirportcode = iataAirportcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportId != null ? airportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportId fields are not set
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.airportId == null && other.airportId != null) || (this.airportId != null && !this.airportId.equals(other.airportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Airport[ id=" + airportId + " ]";
    }

    public List<FlightRoute> getFlightsFromAirport() {
        return flightsFromAirport;
    }

    public void setFlightsFromAirport(List<FlightRoute> flightsFromAirport) {
        this.flightsFromAirport = flightsFromAirport;
    }

    public List<FlightRoute> getFlightsToAirport() {
        return flightsToAirport;
    }

    public void setFlightsToAirport(List<FlightRoute> flightsToAirport) {
        this.flightsToAirport = flightsToAirport;
    }
    
}
