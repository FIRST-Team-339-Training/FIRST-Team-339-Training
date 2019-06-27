// ====================================================================
// FILE NAME: Hardware.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 2, 2011
// CREATED BY: Bob Brown
// MODIFIED ON:
// MODIFIED BY:
// ABSTRACT:
// This file contains all of the global definitions for the
// hardware objects in the systemr
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================
package frc.Hardware;

import frc.HardwareInterfaces.DoubleSolenoid;
import frc.HardwareInterfaces.DoubleThrowSwitch;
import frc.HardwareInterfaces.KilroyEncoder;
import frc.HardwareInterfaces.KilroySPIGyro;
import frc.HardwareInterfaces.LVMaxSonarEZ;
import frc.HardwareInterfaces.LightSensor;
import frc.HardwareInterfaces.MomentarySwitch;
import frc.HardwareInterfaces.QuickSwitch;
import frc.HardwareInterfaces.RobotPotentiometer;
import frc.HardwareInterfaces.SingleThrowSwitch;
import frc.HardwareInterfaces.SixPositionSwitch;
import frc.Utils.drive.Drive;
import frc.Utils.drive.DrivePID;
import frc.Utils.drive.Drive.BrakeType;
import frc.robot.Teleop;
import frc.vision.VisionProcessor;
import frc.vision.VisionProcessor.CameraModel;
import frc.HardwareInterfaces.Transmission.TankTransmission;
import frc.Utils.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

/**
 * ------------------------------------------------------- puts all of the
 * hardware declarations into one place. In addition, it makes them available to
 * both autonomous and teleop.
 *
 * @class HardwareDeclarations
 * @author Bob Brown
 * @written Jan 2, 2011 -------------------------------------------------------
 */

public class Hardware
{
// ------------------------------------
// Public Constants
// ------------------------------------
public enum RobotYear
    {
    KILROY_2018, KILROY_2019, TEST_BOARD
    }

public static final RobotYear whichRobot = RobotYear.KILROY_2019;

// -------------------------------------
// Private Constants
// -------------------------------------

// ---------------------------------------
// Hardware Tunables
// ---------------------------------------

// **********************************************************
// DIGITAL I/O CLASSES
// **********************************************************

// ====================================
// PWM classes
// ====================================

// ------------------------------------
// Jaguar classes
// ------------------------------------

// ------------------------------------
// Talon classes
// ------------------------------------

// ------------------------------------
// Victor Classes
// ------------------------------------

// ------------------------------------
// Servo classes
// ------------------------------------

// ====================================
// CAN classes
// ====================================

// ====================================
// Relay classes
// ====================================

// ====================================
// Digital Inputs
// ====================================
// ------------------------------------
// Single and double throw switches
// ------------------------------------

// ------------------------------------
// Gear Tooth Sensors
// ------------------------------------

// ------------------------------------
// Encoders
// ------------------------------------

// -----------------------
// Wiring diagram
// -----------------------
// Orange - Red PWM 1
// Yellow - White PWM 1 Signal
// Brown - Black PWM 1 (or PWM 2)
// Blue - White PWM 2 Signal
// For the AMT103 Encoders UNVERIFIED
// B - White PWM 2
// 5V - Red PWM 1 or 2
// A - White PWM 1
// X - index channel, unused
// G - Black PWM 1 or 2
// see http://www.cui.com/product/resource/amt10-v.pdf page 4 for Resolution
// (DIP Switch) Settings (currently all are off)

// -------------------------------------
// Red Light/IR Sensor class
// -------------------------------------

// the IR on the back right part of the robot

// ====================================
// I2C Classes
// ====================================

// **********************************************************
// SOLENOID I/O CLASSES
// **********************************************************
// ====================================
// Compressor class - runs the compressor
// ====================================

// ====================================
// Pneumatic Control Module
// ====================================

// ====================================
// Solenoids
// ====================================
// ------------------------------------
// Double Solenoids
// ------------------------------------

// ------------------------------------
// Single Solenoids
// ------------------------------------

// **********************************************************
// ANALOG I/O CLASSES
// **********************************************************
// ====================================
// Analog classes
// ====================================
// ------------------------------------
// Gyro class
// ------------------------------------
// P/N ADW22307

// --------------------------------------
// Potentiometers
// --------------------------------------

// -------------------------------------
// Sonar/Ultrasonic
// -------------------------------------

// =====================================
// SPI Bus
// =====================================

// -------------------------------------
// Analog Interfaces
// -------------------------------------
// if you are getting a null pointer exception from the gyro,
// try setting the parameter you are passing into this declaration
// to false. The null pointer exception is probably because
// there is not a gyro on the robot, and passing in a false will
// tell the robot we do not have a gyro without requiring us to
// comment out the gyro declaration.

// **********************************************************
// roboRIO CONNECTIONS CLASSES
// **********************************************************
// -------------------------------------
// Axis/USB Camera class
// -------------------------------------

// -------------------------------------
// declare the USB camera server and the
// USB camera it serves at the same time
// -------------------------------------

// -------------------------------------
// declare the USB camera server and the
// USB camera it serves at the same time
// -------------------------------------

// **********************************************************
// DRIVER STATION CLASSES
// **********************************************************
// ------------------------------------
// DriverStations class
// ------------------------------------


// ------------------------------------
// Joystick classes
// ------------------------------------

// ------------------------------------
// Buttons classes and Quick Switches
// ------------------------------------
// ----- Left Operator -----

// ----- Right Operator -----

// ------------------------------------
// Momentary Switches
// ------------------------------------

// --------------------------------------------------


// ----------Left Driver---------------

// ----------Right Driver--------------

// **********************************************************
// Kilroy's Ancillary classes
// **********************************************************

// -------------------------------------
// PID tuneables
// -------------------------------------

// -------------------------------------
// PID classes
// -------------------------------------

// ------------------------------------
// Utility classes
// ------------------------------------

// ------------------------------------
// Transmission class
// ------------------------------------

// ------------------------------------
// Drive system
// ------------------------------------

// -------------------
// Assembly classes (e.g. forklift)
// -------------------

// ====================================
// Methods
// ====================================

/**
 * This initializes the hardware for the robot depending on which year we
 * are using
 */
public static void initialize ()
{
    // ---------------------------
    // any hardware declarations that
    // are exactly the same between 2018
    // and 2019. Make them only once
    // ---------------------------
    switch (whichRobot)
        {
        case KILROY_2018:
            robotInitialize2018();
            break;

        default:
        case KILROY_2019:
            robotInitialize2019();
            break;

        case TEST_BOARD:
            break;
        } // end switch
          // ------------------------
          // The function calls in commonKilroyAncillary
          // must follow all other hardware declarations
          // -------------------------
    commonInitialization();
} // end initialize()

public static void commonInitialization ()
{

    // **********************************************************
    // DIGITAL I/O CLASSES
    // **********************************************************

    // ====================================
    // PWM classes
    // ====================================

    // ----- Jaguar classes -----
    // ----- Talon classes -----
    // ----- Victor classes ----
    // ----- Servo classes -----

    // ====================================
    // CAN classes
    // ====================================


    // ====================================
    // Relay classes
    // ====================================


    // ====================================
    // Digital Inputs
    // ====================================
    // -------------------------------------
    // Single and double throw switches
    // -------------------------------------

    // Gear Tooth Sensors

    // Encoders
    // -------------------------------------
    // Red Light/IR Sensor class
    // -------------------------------------


    // ====================================
    // I2C Classes
    // ====================================

    // **********************************************************
    // SOLENOID I/O CLASSES
    // **********************************************************
    // ====================================
    // Compressor class - runs the compressor
    // ====================================


    // ====================================
    // Pneumatic Control Module
    // ====================================

    // ====================================
    // Solenoids
    // ====================================

    // Double Solenoids


    // Single Solenoids

    // **********************************************************
    // ANALOG I/O CLASSES
    // **********************************************************
    // ====================================
    // Analog classes
    // ====================================

    // Gyro class

    // P/N ADW22307

    // Potentiometers


    // Sonar/Ultrasonic


    // =====================================
    // SPI Bus
    // =====================================

    // Analog Interfaces

    // **********************************************************
    // roboRIO CONNECTIONS CLASSES
    // **********************************************************

    // Axis/USB Camera class

    // -------------------------------------
    // declare the USB camera server and the
    // USB camera it serves at the same time
    // -------------------------------------
    // TODO: put somewhere useful
    // Camera settings: 320-240, 20fps, 87 ????


    // **********************************************************
    // DRIVER STATION CLASSES
    // **********************************************************

    // DriverStations class


    // Joystick classes

    public static Joystick leftDriver = null;

    public static Joystick rightDriver = null;

    public static Joystick leftOperator = null;

    public static Joystick rightOperator = null;


    // Buttons classes
    // ----- Left Operator -----


    // ----------Left Driver---------------


    // Momentary Switches


    // ----------Right Driver--------------

    // Momentary Switches

    // ----------Right Driver--------------


    // **********************************************************
    // Kilroy's Ancillary classes
    // **********************************************************


    // ------------------------------------
    // Drive system
    // ------------------------------------

} // end of commonInitialization()

/**
 * This initializes all of the components in Hardware
 */
public static void robotInitialize2018 ()
{
    // **********************************************************
    // DIGITAL I/O CLASSES
    // **********************************************************

    // ====================================
    // PWM classes
    // ====================================

    // ----- Jaguar classes -----
    // ----- Talon classes -----
    // ----- Victor classes -----


    // ----- Servo classes -----

    // ====================================
    // CAN classes
    // ====================================


    // ====================================
    // Relay classes
    // ====================================

    // ====================================
    // Digital Inputs
    // ====================================
    // -------------------------------------
    // Single and double throw switches
    // -------------------------------------

    // Gear Tooth Sensors

    // Encoders


    // -------------------------------------
    // Red Light/IR Sensor class
    // -------------------------------------

    // ====================================
    // I2C Classes
    // ====================================

    // **********************************************************
    // SOLENOID I/O CLASSES
    // **********************************************************
    // ====================================
    // Compressor class - runs the compressor
    // ====================================

    // ====================================
    // Pneumatic Control Module
    // ====================================

    // ====================================
    // Solenoids
    // ====================================

    // Double Solenoids


    // Single Solenoids

    // **********************************************************
    // ANALOG I/O CLASSES
    // **********************************************************
    // ====================================
    // Analog classes
    // ====================================

    // Gyro class

    // P/N ADW22307

    // Potentiometers

    // Sonar/Ultrasonic

    // =====================================
    // SPI Bus
    // =====================================

    // Analog Interfaces

    // **********************************************************
    // roboRIO CONNECTIONS CLASSES
    // **********************************************************

    // Axis/USB Camera class

    // -------------------------------------
    // declare the USB camera server and the
    // USB camera it serves at the same time
    // -------------------------------------

    // **********************************************************
    // DRIVER STATION CLASSES
    // **********************************************************

    // DriverStations class

    // Joystick classes

    leftDriver = new Joystick(0);

    rightDriver = new Joystick(1);

    leftOperator = new Joystick(2);

    rightOperator = new Joystick(3);




    // Buttons classes
    // ----- Left Operator -----

    // left trigger

    // ----- Right Operator -----

    // Momentary Switches

}  // end of robotInitialize2018()

/**
 * This initializes all of the components in Hardware
 */
public static void robotInitialize2019 ()
{
    // **********************************************************
    // DIGITAL I/O CLASSES
    // **********************************************************

    // ====================================
    // PWM classes
    // ====================================

    // ----- Jaguar classes -----
    // ----- Talon classes -----
    // ----- Victor classes -----
    // ----- Servo classes -----

    // ====================================
    // CAN classes
    // ====================================


    // ====================================
    // Relay classes
    // ====================================

    // ====================================
    // Digital Inputs
    // ====================================
    // -------------------------------------
    // Single and double throw switches
    // -------------------------------------

    // Gear Tooth Sensors

    // Encoders

    // -------------------------------------
    // Red Light/IR Sensor class
    // -------------------------------------

    // ====================================
    // I2C Classes
    // ====================================

    // **********************************************************
    // SOLENOID I/O CLASSES
    // **********************************************************
    // ====================================
    // Compressor class - runs the compressor
    // ====================================

    // ====================================
    // Pneumatic Control Module
    // ====================================

    // ====================================
    // Solenoids
    // ====================================

    // Double Solenoids

    // Single Solenoids
    // **********************************************************
    // ANALOG I/O CLASSES
    // **********************************************************
    // ====================================
    // Analog classes
    // ====================================

    // Gyro class

    // P/N ADW22307

    // Potentiometers

    // Sonar/Ultrasonic

    // =====================================
    // SPI Bus
    // =====================================

    // Analog Interfaces

    // **********************************************************
    // roboRIO CONNECTIONS CLASSES
    // **********************************************************

    // Axis/USB Camera class

    // -------------------------------------
    // declare the USB camera server and the
    // USB camera it serves at the same time
    // -------------------------------------

    // **********************************************************
    // DRIVER STATION CLASSES
    // **********************************************************

    // DriverStations class
    // DriverStations class

    // Joystick classes

    // Buttons classes

    // ----- Left Operator -----

    // left trigger

    // ----- Right Operator -----

    // Momentary Switches

} // end robotInitialize2019()


} // end class
