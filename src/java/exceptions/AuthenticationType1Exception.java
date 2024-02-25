package exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jam
 */

// Correct Username, Incorrect Password
public class AuthenticationType1Exception extends RuntimeException {
    public AuthenticationType1Exception (String e) {
        super(e);
    }
}
