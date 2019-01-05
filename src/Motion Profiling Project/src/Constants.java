//package motionProfilingProject.util;  //I don't know how to do this or if it is necessary

public class Constants {
//	Inspired by FRC254 The Cheesy Poofs
	
	public static final double dtStandard = 0.02; //20 ms cycle time on roboRio   //consider use of 10 ms with notifier
	
	// Physical Constants
	public static final double wheelDiameter = 6.0; //in to m 
	public static final double wheelRadius = wheelDiameter / 2; //m
	public static final double drivebaseWidth = 24.0; //in to m
	
	public static final double mass = 40.0; //kg used for linear inertia
	public static final double angularInertia = 10.0; //kg * m^2 
	
	public static final double driveGearRatio = 10.71;
	
	// Electrical Constants
	public static final double vIntercept = 1.03; //friction voltage
	public static final double resistance = 0.0916; // electical resistance of motor, may need to multiply  
											 // by number of motors per gearbox or just tune overall
	public static final double kt = 2.41 / 131;        // N*m / A    calculated at stall
	public static final double kv = 5330*2*Math.PI/60; // rad/s / V  calculated at free speed 
	
	// Path Following Constants
	public static final double standardVelocity = 3.0;  // m/s
	public static final double maxVelocity = 3.95;		// m/s
	public static final double standardAccel = 3.81;	// m/s^2
	public static final double maxAccel = 27.0; 		// m/s^2
	
	public static final double standardAngularVelocity = 0.0;  // rad/s
	public static final double maxAngularVelocity = 0.0;      // rad/s
	public static final double standardAngularAccel = 0.0;   // rad/s^2
	public static final double maxAngularAccel = 0.0;        // rad/s^2
	
//	Method from 254
	public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }
	
}
