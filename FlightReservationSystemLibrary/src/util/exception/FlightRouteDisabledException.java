/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Administrator
 */
public class FlightRouteDisabledException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteDisabledException</code>
     * without detail message.
     */
    public FlightRouteDisabledException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteDisabledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteDisabledException(String msg) {
        super(msg);
    }
}
