package de.jjohannes.robot;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class LaserSchranke {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("!! ALARM AN !!");

        UltrasonicSensor laser = new UltrasonicSensor(SensorPort.S1);
        TouchSensor knopf = new TouchSensor(SensorPort.S2);

        laser.continuous();
        Thread.sleep(1000);

        int detectCount = 0;

        while (true) {
            while (detectCount < 6) {
                Thread.sleep(100);
                LCD.clear();
                LCD.drawString("" + laser.getDistance(), 1, 1);
                if (laser.getDistance() < 25) {
                    detectCount++;
                } else {
                    detectCount = 0;
                }
            }

            detectCount = 0;
            while (!knopf.isPressed()) {
                Sound.twoBeeps();
            }

            Thread.sleep(3000);
        }
    }
}