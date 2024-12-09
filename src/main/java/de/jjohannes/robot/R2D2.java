package de.jjohannes.robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Demonstration of the Behavior subsumption classes.
 * <p>
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports A and C, and
 * a touch sensor connected to sensor  port 1 and
 * an ultrasonic sensor connected to port 3;
 *
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 */
public class R2D2 {
    // Use these definitions instead if your motors are inverted
    static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
    static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.B);

    public static void main(String[] args) {
        leftMotor.setSpeed(250);
        rightMotor.setSpeed(250);
        Behavior b1 = new DriveForward();
        Behavior b2 = new DetectWall();
        Behavior[] behaviorList = { b1, b2 };
        Arbitrator arbitrator = new Arbitrator(behaviorList);
        LCD.drawString("R2D2", 0, 1);
        Button.waitForAnyPress();
        arbitrator.start();
    }
}


class DriveForward implements Behavior {

    private boolean _suppressed = false;

    public boolean takeControl() {
        return true;
    }

    public void suppress() {
        _suppressed = true;
    }

    public void action() {
        _suppressed = false;
        R2D2.leftMotor.forward();
        R2D2.rightMotor.forward();
        while (!_suppressed) {
            Thread.yield(); //don't exit till suppressed
        }
        R2D2.leftMotor.stop();
        R2D2.leftMotor.stop();
    }
}


class DetectWall implements Behavior {

    // private TouchSensor touch;
    private final UltrasonicSensor sonar;

    public DetectWall() {
        // touch = new TouchSensor(SensorPort.S1);
        sonar = new UltrasonicSensor(SensorPort.S3);
    }

    public boolean takeControl() {
        sonar.ping();

        return sonar.getDistance() < 25;
    }

    public void suppress() {
        //Since  this is highest priority behavior, suppress will never be called.
    }

    public void action() {
        R2D2.leftMotor.rotate(-180, true);// start Motor.A rotating backward
        R2D2.rightMotor.rotate(-360);  // rotate C farther to make the turn
    }
}


