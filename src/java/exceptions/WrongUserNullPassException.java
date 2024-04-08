package exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jam
 */

// Incorrect Username and Password
public class WrongUserNullPassException extends RuntimeException {
    public WrongUserNullPassException (String e) {
        super(e);
    }
}

