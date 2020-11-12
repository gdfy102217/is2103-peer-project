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
public class FlightDisabledException extends Exception {

    /**
     * Creates a new instance of <code>FlightDisabledException</code> without
     * detail message.
     */
    public FlightDisabledException() {
    }

    /**
     * Constructs an instance of <code>FlightDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightDisabledException(String msg) {
        super(msg);
    }
}
