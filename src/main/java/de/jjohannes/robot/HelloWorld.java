package de.jjohannes.robot;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("!HI!");
        Motor.B.setSpeed(200);
        Motor.B.forward();
        Button.waitForAnyPress();
    }
}