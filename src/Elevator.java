
public class Elevator extends Subsystem{
	
	double pulleyRadius; //in inches
	double outputVoltage;
	//double freeSpeed; //in/s
	
	double acceleration = 0.0, velocity = 0.0, position = 0.0, simulatedTime = 0.0;
	
	public Elevator(double weight, int mID, int motors, double g, double pulleyDiameter) {
		super(weight, mID, motors, g);
		pulleyRadius = pulleyDiameter/2; // divide by two to get radius
		freeSpeed = super.motorSpecs[0][mID]/60*Math.PI*pulleyDiameter/g;
		maxAcceleration = acceleration = 12*nMotors*(gearRatio*kt/(mass*pulleyRadius*R))*(12) - 386.22;
		maxDeceleration = maxAcceleration;
		System.out.println("Free Speed: " + freeSpeed + " Max Acceleration: " + maxAcceleration);
	}
	
	public double simulate(double volts, double time){
		while(currentSimTime < time) {
			
			position += velocity * simulationTimeStep;
			velocity = Math.min(velocity + acceleration*simulationTimeStep, freeSpeed);
			acceleration = 12*nMotors*(gearRatio*kt/(mass*pulleyRadius*R))*(volts - (velocity*gearRatio)/(kv*2*Math.PI*pulleyRadius)) -386.22; // - 386.22; 
			
			
			
			System.out.println(currentSimTime + "\t" + acceleration + "\t" + velocity + "\t" + position );
			
			currentSimTime += simulationTimeStep;
		}
		return position;
	}
	
	public double controllerOutput(double accel, double velocity) {
		
		outputVoltage = this.mass *accel*pulleyRadius/(gearRatio*this.kt)+velocity*gearRatio/(this.kv*2*Math.PI*pulleyRadius); //Feed forward
		
		
		return outputVoltage; 
	}
	
	
	
}