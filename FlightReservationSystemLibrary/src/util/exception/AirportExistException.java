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
public class AirportExistException extends Exception {

    /**
     * Creates a new instance of <code>AirportExistException</code> without
     * detail message.
     */
    public AirportExistException() {
    }

    /**
     * Constructs an instance of <code>AirportExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportExistException(String msg) {
        super(msg);
    }
}
