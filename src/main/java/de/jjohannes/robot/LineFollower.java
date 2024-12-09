package de.jjohannes.robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.RotateMoveController;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

/**
 * Demonstration of use of the Behavior and Pilot classes to
 * implement a simple line following robot.
 *
 * @author Lawrie Griffiths
 */
public class LineFollower {

	private static final int THRESHOLD = 55;
	private static final Random RANDOM = new Random();

	public static void main (String[] aArg) {
		float wheelDiameter = 5.6f;
		float trackWidth = 17.0f;
		RegulatedMotor leftMotor = Motor.B;
		RegulatedMotor rightMotor = Motor.C;
		boolean reverse = false;

		final RotateMoveController pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		final LightSensor light = new LightSensor(SensorPort.S1);
		pilot.setRotateSpeed(80);
		pilot.setTravelSpeed(10);

		// this behavior wants to take control when the light sensor sees the line
		Behavior driveForward = new Behavior() {
			public boolean takeControl() {return light.readValue() <= THRESHOLD;}

			public void suppress() {
				pilot.stop();
			}
			public void action() {
				pilot.forward();
				while(light.readValue() <= THRESHOLD) {
					Thread.yield(); //action complete when not on line
				}
			}
		};

		Behavior offLine = new Behavior() {
			private boolean suppress = false;

			public boolean takeControl() {
				return light.readValue() > THRESHOLD;
			}

			public void suppress() {
				suppress = true;
			}

			public void action() {
				double sweep = 2;
				while (!suppress) {
					pilot.rotate(sweep, true);
					while (!suppress && pilot.isMoving()) { Thread.yield(); }
					sweep *= -1.2;
					LCD.drawString(" " + sweep, 0, 1);
				}
				pilot.stop();
				suppress = false;
			}
		};

		Behavior[] bArray = {offLine, driveForward};
		LCD.drawString("Line ", 0, 1);
		Button.waitForAnyPress();
		Arbitrator arbitrator = new Arbitrator(bArray);
		arbitrator.start();
	}
}

