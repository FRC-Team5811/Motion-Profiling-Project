
public class Subsystem {
	
	double mass; //mass in... slugs
	int motorID; //0-CIM, 1-MiniCIM, 2-BAG, 3-775Pro
	int nMotors;
	double gearRatio; // Driven/driving should be greater than 1
	double freeSpeed; 
	double maxAcceleration;
	double maxDeceleration;
	
	double kt, kv, R;
	double simulationTimeStep = 0.020;
	double currentSimTime = 0.0;
	
	//double acceleration, velocity, position = 0.0;
	
	// Motor characteristic information for calculation of torque and velocity constants (kt, kv) and internal resistance (R)
	public double[][] motorSpecs =  {
		{5330.0, 5840.0, 13180.0, 18730.0}, // Free Speed (RPM)
		{2.7, 3.0, 1.8, 0.7}, // Free Current (A)
		{21.3285, 12.4785, 3.8055, 6.2835}, // Stall Torque (in*lbs)     //{2.41, 1.41, 0.43, 0.71} N m
		{131.0, 89.0, 53.0, 134.0} // Stall Current (A)
	};
	//Motor Identification: 0-CIM, 1-MiniCIM, 2-BAG, 3-775Pro
	//Motor Specs: 0-Free Speed, 1-Free Current, 2-Stall Torque, 3-Stall Current
	
	Subsystem(double weight, int mID, int motors, double g){
		mass = weight / 32.174; //convert lbs to... slugs... yes, really
		motorID = mID;
		nMotors = motors;
		gearRatio = g;
		
		//Motor characteristic information from motorSpecs array
		kv = motorSpecs[0][mID]/12; // (RPM / V)
		kt = motorSpecs[2][mID]/motorSpecs[3][mID]; // (in*lbs / A) 
		R = 12/motorSpecs[3][mID]; // (ohms)
		System.out.println("Mass in slugs: " + mass);
		System.out.println("kv = " + kv + "  kt = " + kt + "  R = " + R);
	}
	
	
}
