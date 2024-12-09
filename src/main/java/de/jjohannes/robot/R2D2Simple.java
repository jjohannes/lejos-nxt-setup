package de.jjohannes.robot;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class R2D2Simple {
    public static void main(String[] args) {
        System.out.println("!R2D2!");

        Motor.A.setSpeed(200);
        Motor.A.backward();
        Motor.B.setSpeed(200);
        Motor.B.backward();

        // Head
        Motor.C.setSpeed(50);
        Motor.C.forward();

        Button.waitForAnyPress();
    }
}