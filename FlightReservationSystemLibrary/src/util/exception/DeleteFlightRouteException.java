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
public class DeleteFlightRouteException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightRouteException</code> without
     * detail message.
     */
    public DeleteFlightRouteException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightRouteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightRouteException(String msg) {
        super(msg);
    }
}
