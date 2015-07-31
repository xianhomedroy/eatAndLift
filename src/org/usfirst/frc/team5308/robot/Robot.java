//MAIN SOURCE CODE!!!!!!!!!!!!!!!!!!!!!!
package org.usfirst.frc.team5308.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
    RobotDrive myRobot;
    Joystick stick;
    int autoLoopCounter;
    CameraServer server;
    Solenoid sole0;
    Solenoid sole1;
    Compressor com1;
    Talon dirL;
    Talon dirR;
    Talon actuator1;
    Talon actuator2;
    //DigitalInput limswitchL;
    //DigitalInput limswitchR;
    //boolean lastLimSwitchL, lastLimSwitchR;
    boolean[] arr;
    int press;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        myRobot = new RobotDrive(0, 1, 2, 3);
        stick = new Joystick(0);
        server = CameraServer.getInstance();
        server.setQuality(80);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
        sole0 = new Solenoid(0);
        sole1 = new Solenoid(1);
        com1 = new Compressor(0);
        com1.setClosedLoopControl(true);
        dirL = new Talon(4);
        dirR = new Talon(5);
        actuator1 = new Talon(6);
        actuator2 = new Talon(7);
        //limswitchL = new DigitalInput(0);
        //limswitchR = new DigitalInput(1);
        //lastLimSwitchL = lastLimSwitchR = false;
        arr = new boolean[12];
        press = -1;
    }

    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit()
    {
        autoLoopCounter = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
        {
            myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
            autoLoopCounter++;
        }
        else if(autoLoopCounter < 130)
        {
            myRobot.drive(0.0, 0.0);
            autoLoopCounter++;
        }

        else if(autoLoopCounter < 230)
        {
            myRobot.drive(0.5, 0.0);
            autoLoopCounter++;
        }

        else
        {
            myRobot.drive(0.0, 0.0);
        } 	// stop robot
    }

    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit()
    {
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        //Drive
        myRobot.arcadeDrive(stick, true);
        //System.out.print("Distance Traveled:");
        //System.out.println(enc.getDistance());

        //Limit switch
       /* if(limswitchL.get() != lastLimSwitchL)
        {
            lastLimSwitchL = !lastLimSwitchL;
            System.out.println("Limit switch L " + (lastLimSwitchL ? "engaged." : "disengaged."));
        }
        if(limswitchR.get() != lastLimSwitchR)
        {
            lastLimSwitchR = !lastLimSwitchR;
            System.out.println("Limit switch R " + (lastLimSwitchR ? "engaged." : "disengaged."));
        }*/
        //Button
        for(int i = 1; i <= stick.getButtonCount(); ++i)
        {
            if(stick.getRawButton(i))
            {
                press = i;
                break;
            }
        }
        switch(press)
        {
        case 1 ://tractor push out
        	RunSafety(sole0,1);
            break;
        case 2 ://collect
        	RunSafety(dirR,0.8,2);
        	RunSafety(dirL,0.8,2);
            break;
        case 7:
        	RunSafety(sole1,7);
        case 8 ://Lifting motor
        	RunSafety(actuator1,1.0,3);
        	RunSafety(actuator2,1.0,3);
            break;     	
        case -1 :
            break;
        default:
            System.out.println("Button " + press + " engaged.");
        };
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
        LiveWindow.run();
    }
    public void RunSafety(Talon T,double speed,int index)
    {
    	if(!arr[index-1])
    	{
    		T.set(speed);
    		arr[index-1]=true;
    	}
    	else 
    		if(arr[index-1]&&press!=index)
    	{
    		arr[index-1]=false;
    		T.set(0);
    	}
    }
    public void RunSafety(Solenoid S,int index)
    {
    	if(!arr[index-1])
    	{
    		sole1.set(sole1.get()?false:true);
    	}
    	else
    		if(arr[index-1]&&press!=index)
    			arr[index-1] = false;
    }
}
