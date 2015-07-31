//MAIN SOURCE CODE!!!!!!!!!!!!!!!!!!!!!!
package org.usfirst.frc.team5308.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot
{
	
	final int MaxButtonNum = 20; //defining the max num of the buttons on the stick
	buttonStatus[] allButton = new buttonStatus[MaxButtonNum]; //储存所有按钮状态
	
	
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
    
    public void updateButtons() //更新所有按钮的状态
	{
		for(int i = 1;i <= MaxButtonNum;++i)
		{
			boolean raw = stick.getRawButton(i); //好像吧 没装WPILIB
			buttonStatus now = allButton[i];//正在处理的
			if(raw) // 如果按钮第一次被按下
			{
				if(now.press == buttonPress.activate)// 如果按钮第二次被按下
				{
					now.press = buttonPress.on;
				}
				else if(now.press != buttonPress.on) //同时也不等于activate
				{
					now.reverse();
					now.press = buttonPress.activate;
				}
				//off/deactivate(松开) -> activate(第一次按下) -> on(持久按下)
			}
			else // raw == false
			{
				if(now.press == buttonPress.deactivate)
					now.press = buttonPress.off;
				else if(now.press != buttonPress.off) //同时也不等于deactivate
					now.press = buttonPress.deactivate;
				//activate/on(被按下) -> deactivate(第一次松开) -> off(持久松开)
			}
			allButton[i] = now;
		}
	}
    
    
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
        updateButtons();
        if(allButton[1].press == buttonPress.activate || allButton[1].press == buttonPress.deactivate)
        	runSole(sole0);
        
        if(allButton[2].press == buttonPress.activate)
        {
        	dirL.set(0.8);
        	dirR.set(0.8);
        	//RunSafety(dirR,0.8,2);
        	//RunSafety(dirL,0.8,2);
        }
        if(allButton[2].press == buttonPress.deactivate)
        {
        	dirL.set(0);
        	dirR.set(0);
        	//RunSafety(dirR,0.8,2);
        	//RunSafety(dirL,0.8,2);
        }
        
        if(allButton[7].press == buttonPress.activate || allButton[7].press == buttonPress.deactivate)
        {
        	runSole(sole1);
        }
        if(allButton[8].press == buttonPress.activate)
        {
        	actuator1.set(1.0);
        	actuator2.set(1.0);
        	//RunSafety(actuator1,1.0,3);
        	//RunSafety(actuator2,1.0,3);
        }
        if(allButton[8].press == buttonPress.deactivate)
        {
        	actuator1.set(0.0);
        	actuator2.set(0.0);
        }
        /*switch(press)
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
        	
        };*/
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
    public void runSole(Solenoid S)
    {
    	sole1.set(sole1.get()?false:true);
    }
}
