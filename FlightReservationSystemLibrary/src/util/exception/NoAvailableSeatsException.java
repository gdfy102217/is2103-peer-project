/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author xuyis
 */
public class NoAvailableSeatsException extends Exception{

    public NoAvailableSeatsException() {
    }

    public NoAvailableSeatsException(String string) {
        super(string);
    }
    
}
