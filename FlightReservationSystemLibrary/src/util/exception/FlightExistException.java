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
public class FlightExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightExistException</code> without
     * detail message.
     */
    public FlightExistException() {
    }

    /**
     * Constructs an instance of <code>FlightExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightExistException(String msg) {
        super(msg);
    }
}
