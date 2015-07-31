package org.usfirst.frc.team5308.robot;

enum buttonPress{activate,on,deactivate,off};

public class buttonStatus {

	
	public buttonPress press;
	public boolean reversed;
	
	buttonStatus() //the initializer, called once the object of the class is created
	{
		press = buttonPress.off;
		reversed = false;
	}
	public void reverse()
	{
		reversed = !reversed; //取反
	}
}