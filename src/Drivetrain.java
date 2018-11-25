import java.util.ArrayList;

public class Drivetrain extends Subsystem{

	double wheelRadius; 
	double gearRatio; 
	double outputVoltage; 
	double positionError; 
	double maxVoltage = 12.0; 
	double speedDerating = 0.95; // A fudged percentage of free speed to target as a cruising velocity
	double accelDist; // a "measured" (simulation generated) value to reference for time decel
	double pos; 
	double wheelBaseWidth; 
	
	double timeToMaxSpeed, timeToZeroSpeed, upDownDistance, distance, cruisingDistance;
	double dt = 0.020;
	
	double acceleration = 0.0, velocity = 0.0, position = 0.0, simulatedTime = 0.0, previousTime = 0.0;
	
	
	ArrayList<Double> accelerations = new ArrayList();
	ArrayList<Double> velocities = new ArrayList();
	ArrayList<Double> positions = new ArrayList();
	ArrayList<Double> voltages = new ArrayList();
	
	double kp = 0.0;
	double ki = 0.0;
	double kd = 0.0;
	
	double KVdumb;
	double A, P, D = 0.0;
	
	public Drivetrain(double weight, int mID, int motors, double g, double wheelDiameter, double width) {
		super(weight, mID, motors, g);
		wheelRadius = wheelDiameter/2;
		gearRatio = g;
		freeSpeed = super.motorSpecs[0][mID]/60*Math.PI*wheelDiameter/g * maxVoltage/12;
		KVdumb = 1/freeSpeed;
		maxAcceleration = 12*nMotors*(gearRatio*kt/(mass*wheelRadius*R))*(maxVoltage); //First twelve feet to inches
		maxDeceleration = maxAcceleration;
		wheelBaseWidth = width;
		//timeToMaxSpeed = freeSpeed/maxAcceleration;
		//timeToZeroSpeed = freeSpeed/maxDeceleration;
		//upDownDistance = (freeSpeed)*(timeToMaxSpeed);
		System.out.println("Free Speed: " + freeSpeed + " Max Acceleration: " + maxAcceleration); 
	}

	public double simulate(double volts, double time){
		while(currentSimTime < time) {
			
			pos += velocity * simulationTimeStep;
			velocity = Math.min(velocity + acceleration*simulationTimeStep, freeSpeed);
			//velocity += acceleration*simulationTimeStep;
			acceleration = 12*nMotors*(gearRatio*kt/(mass*wheelRadius*R))*(volts - (velocity*gearRatio)/(kv*2*Math.PI*wheelRadius)); 
			
			System.out.println(currentSimTime + "\t" + acceleration + "\t" + velocity + "\t" + pos );
			
			currentSimTime += simulationTimeStep;
		}
		return pos;
	}
	
	//Generate an acceleration decay based profile using a speedDerating to determine when to hold a cruising velocity
	
	void generateProfile(double d){
		distance = d; 
		int numAccelPoints = 0;
		
		System.out.println("Adjusted free speed: " + speedDerating * freeSpeed);
		while(velocity < speedDerating * freeSpeed && position < distance /2) {  //Add acceleration waypoints
			
			position += velocity*dt;
			velocity = Math.min(velocity + acceleration*dt,freeSpeed);  
			acceleration = getMaxAcceleration(velocity);
			
			
			accelerations.add(acceleration);
			velocities.add(velocity);
			positions.add(position);
			simulatedTime += dt;
			
			
		}
		numAccelPoints = velocities.size();
		accelDist = position;
		System.out.println("Acceleration waypoints added");
		//velocity = freeSpeed; // we use the actual velocity for cruising
		if(velocity >= speedDerating * freeSpeed) {    //Add cruising waypoints if we made it to derated free speed
			System.out.println("Generating cruising waypoints with velocity: " + velocity);
			while(position < distance-accelDist) { 
				position += velocity*dt;
				velocity += acceleration*dt;
				acceleration = 0.0;
				
				accelerations.add(acceleration);
				velocities.add(velocity);
				positions.add(position);
				simulatedTime +=dt;
			}
			System.out.println("Cruising waypoints added");
		}	
		for(int i = 0; i < numAccelPoints; i++) { //Add deceleration waypoints using the mirrored points from the acceleration portion of the graph
			
			position += velocity*dt;
			velocity = Math.max(velocity + acceleration*dt, 0.0);
			acceleration = -1*accelerations.get(i);
			
			
			accelerations.add(acceleration);
			velocities.add(velocity);
			positions.add(position);
			simulatedTime += dt;
		}
		//if(velocity > )
		System.out.println("Deceleration waypoints added");
		System.out.println("Profile generated");
	}
	
	// Updated method for generating acceleration decay profiles // not finished
	
	public void generateProfileV2(double d){  
		distance = d; 
		int numAccelPoints = 0;
		
		System.out.println("Adjusted free speed: " + speedDerating * freeSpeed);
		while(velocity < speedDerating * freeSpeed && position < distance /2) {  //Add acceleration waypoints
			
			position += velocity*dt;
			velocity = Math.min(velocity + acceleration*dt,freeSpeed);  
			acceleration = getMaxAcceleration(velocity);
			
			
			//accelerations.add(acceleration, numAccelPoints);
			//velocities.add(velocity, numAccelPoints);
			//positions.add(position, numAccelPoints);
			numAccelPoints++;
			
			
			
			
			simulatedTime += dt;
		}
		//numAccelPoints = velocities.size();
		accelDist = position;
		System.out.println("Acceleration waypoints added");
		//velocity = freeSpeed; // we use the actual velocity for cruising
		if(velocity >= speedDerating * freeSpeed) {    //Add cruising waypoints if we made it to derated free speed
			System.out.println("Generating cruising waypoints with velocity: " + velocity);
			while(position < distance-accelDist) { 
				position += velocity*dt;
				velocity += acceleration*dt;
				acceleration = 0.0;
				
				accelerations.add(acceleration);
				velocities.add(velocity);
				positions.add(position);
				simulatedTime +=dt;
			}
			System.out.println("Cruising waypoints added");
		}	
		for(int i = 0; i < numAccelPoints; i++) { //Add deceleration waypoints using the mirrored points from the acceleration portion of the graph
			
			position += velocity*dt;
			velocity = Math.max(velocity + acceleration*dt, 0.0);
			acceleration = -1*accelerations.get(i);
			
			
			accelerations.add(acceleration);
			velocities.add(velocity);
			positions.add(position);
			simulatedTime += dt;
		}
		//if(velocity > )
		System.out.println("Deceleration waypoints added");
		System.out.println("Profile generated");
	}
	
	// "Run" a profile with physics accurate voltage command to achieved given acceleratio and velocity
	
	public void executeProfile() {
		System.out.println("Beginning profile execution");
		for(int i = 0; i< accelerations.size(); i++) {
			System.out.println(accelerations.get(i) + "\t" + velocities.get(i) + "\t" + positions.get(i) + "\t" + controllerOutput(accelerations.get(i), velocities.get(i)));
			//System.out.println(controllerOutput(accelerations.get(i), velocities.get(i))); //Output voltage
		}
		System.out.println("Simulated position: " + position);
	}
	
	//Return physics accurate voltage required to achieve a given acceleration and velocity
	
	public double controllerOutput(double accel, double velocity) {  //, double targetPos, double currentPos) {
		
		outputVoltage = (this.mass*accel/12*wheelRadius*this.R/(gearRatio*this.kt))/this.nMotors + // I*R term    "acceleration"
				velocity*(60)*gearRatio/(this.kv*2*Math.PI*wheelRadius);                           // W/kv term  "velocity"
		//positionError = currentPos - targetPos;
		
		return outputVoltage; 
	}
	
	//Returns the maximum linear acceleration given the max voltage and current linear velocity (used for acceleration decay profile generation)
	
	public double getMaxAcceleration(double velocity) {
		return 12*nMotors*(gearRatio*kt/(this.mass*wheelRadius*this.R))*(maxVoltage-velocity*gearRatio/(this.kv/60*2*Math.PI*wheelRadius));
	}
	
	public double getMaxReverseAcceleration(double velocity) {
		return 12*nMotors*(gearRatio*kt/(this.mass*wheelRadius*this.R))*((-1)*maxVoltage-velocity*gearRatio/(this.kv/60*2*Math.PI*wheelRadius));
	}
	
	//Generates a simple trapezoidal profile without regard to maximum concurrent acceleration and velocity 
	
	void generateTrapezoidalProfile(double d){  
		distance = d;
		double specifiedAcceleration = 100.0;
		
		if(upDownDistance<distance) { //Generate a trapezoidal profile
			cruisingDistance = distance - upDownDistance;
			System.out.println("accel decel dist: " + upDownDistance + " cruising distance" + cruisingDistance);
			while(simulatedTime < timeToMaxSpeed) {  //Add acceleration waypoints
				position += velocity*dt;
				accelerations.add(specifiedAcceleration); //was maxAcceleration not 50.0
				velocity = Math.min(velocity + specifiedAcceleration*dt,freeSpeed);   //was maxAcceleration not 50.0
				
				velocities.add(velocity);
				positions.add(position);
				simulatedTime += dt;
			}
			//velocity = freeSpeed; // ***
			for(int i =0; i < cruisingDistance/freeSpeed/0.02; i++) { //Add cruising waypoints
				position += velocity*dt;
				accelerations.add(0.0);
				velocities.add(velocity);
				positions.add(position);
				simulatedTime +=dt;
			}
			while(simulatedTime < timeToMaxSpeed+cruisingDistance/freeSpeed+timeToZeroSpeed) { //Add deceleration waypoints
				position += velocity*dt;
				accelerations.add(specifiedAcceleration); //was maxAcceleration not 50.0
				velocity = Math.max(velocity - specifiedAcceleration*dt, 0.0); //was maxAcceleration not 50.0
				
				velocities.add(velocity);
				positions.add(position);
				simulatedTime += dt;
			}
			System.out.println("Profile generated");
		}else { //Otherwise generate a triangular profile or a lower max velocity trapezoidal profile
			System.out.println("Code a triangular or lower max velocity trapezoidal profile generator already!");
			
		}
	}
	
	//Cheesy Poofs and Pathfinder based controller with accel and velocity dependent feed forward and position and velocity dependent feedback
	
	public double controllerPDVA(double accel, double velocity) {  //, double targetPos, double currentPos) {
		
		outputVoltage = 12*(acceleration*A + velocity*KVdumb); //multiply by twelve to convert throttle to output voltage
		
		//positionError = currentPos - targetPos; //still ignoring feedback for now
		
		return outputVoltage; 
	}
	
	// "Run" the simple trapezoidal profile uses PDVA controller for voltage calculation
	
	void executeTrapezoidalProfile(boolean forGraphing) {
		if(forGraphing) {
			for(int i = 0; i< accelerations.size(); i++) {
				System.out.println(accelerations.get(i) + "\t" + velocities.get(i) + "\t" + positions.get(i) + "\t" + voltages.get(i));
				//System.out.println(controllerOutput(accelerations.get(i), velocities.get(i))); //Output voltage
			}
			System.out.println("Simulated position: " + position);
		} else {
			String voltString = "";
			String posString = "";
			String velString = "";
			String accString = "";
			for(int i = 0; i < voltages.size(); i++) {
				voltString = voltString.concat(voltages.get(i).toString());    voltString = voltString.concat(", ");
				posString = posString.concat(positions.get(i).toString());     posString = posString.concat(", ");
				velString = velString.concat(velocities.get(i).toString());    velString = velString.concat(", ");
				accString = accString.concat(accelerations.get(i).toString()); accString = accString.concat(", ");
			}
			System.out.println(voltString);
			System.out.println(posString);
			System.out.println(velString);
			System.out.println(accString);
		}
		System.out.println("Traveled: " + positions.get(positions.size()-1) + " inches in");
		System.out.println((positions.size()*dt) + " s"); //Total amount of time spent on a movement
		
	}
	
	public void trapezoidalV2(double dist) {
		distance = dist;
		double specifiedAcceleration = 150.0;
		double specifiedVelocity = 120.0; //120.0
		timeToMaxSpeed = specifiedVelocity/specifiedAcceleration; //delta t = vf / a
		timeToZeroSpeed = timeToMaxSpeed; //
		upDownDistance = specifiedVelocity*(timeToMaxSpeed); 
		double downDistance = upDownDistance/2;
		// 2 for up and down * 1/2 * vf * delta t
		
		if(upDownDistance<distance) { //Generate a trapezoidal profile
			cruisingDistance = distance - upDownDistance;
			System.out.println("accel decel dist: " + upDownDistance + " cruising distance" + cruisingDistance);
			while(velocity < specifiedVelocity) {  //Add acceleration waypoints
				acceleration = specifiedAcceleration;
				velocity += acceleration*(simulatedTime - previousTime);
				position += velocity*(simulatedTime - previousTime);
				
				positions.add(position);
				velocities.add(velocity);
				accelerations.add(acceleration);
				voltages.add(this.controllerOutput(acceleration, velocity));
				
				previousTime = simulatedTime;
				simulatedTime += dt;
			}
			System.out.println("Acceleration Points added. Cruising velocity: " + velocity);
			while(position < distance - downDistance) {      //for (int i = 0; i < cruisingDistance/specifiedVelocity/0.02; i++) { //Add cruising waypoints
				acceleration = 0.0;
				velocity += acceleration*(simulatedTime - previousTime);
				position += velocity*(simulatedTime - previousTime);
				
				positions.add(position);
				velocities.add(velocity);
				accelerations.add(acceleration);
				voltages.add(this.controllerOutput(acceleration, velocity));
				
				previousTime = simulatedTime;
				simulatedTime +=dt;
			}
			System.out.println("Cruising Points added. Remaining distance: " + (distance - position));
			while(velocity > 0) {     //formerly position < distance //Add deceleration waypoints
				acceleration = -1*specifiedAcceleration;
				velocity += acceleration*(simulatedTime - previousTime);
				position += velocity*(simulatedTime - previousTime);
				
				positions.add(position);
				velocities.add(velocity);
				accelerations.add(acceleration);
				voltages.add(this.controllerOutput(acceleration, velocity));
				
				previousTime = simulatedTime;
				simulatedTime += dt;
			}
			System.out.println("Profile generated");
			System.out.println("upDownDistance: " + upDownDistance);
			System.out.println("timetomaxspeed: " + timeToMaxSpeed);
		}else { //Otherwise generate a triangular profile or a lower max velocity trapezoidal profile
			System.out.println("Triangular profile");
			while(position < distance/2) {
				acceleration = specifiedAcceleration;
				velocity += acceleration*(simulatedTime - previousTime);
				position += velocity*(simulatedTime - previousTime);
				
				positions.add(position);
				velocities.add(velocity);
				accelerations.add(acceleration);
				voltages.add(this.controllerOutput(acceleration, velocity));
				
				previousTime = simulatedTime;
				simulatedTime += dt;
			}
			while(velocity > 0) {
				acceleration = -1*specifiedAcceleration;
				velocity += acceleration*(simulatedTime - previousTime);
				position += velocity*(simulatedTime - previousTime);
				
				positions.add(position);
				velocities.add(velocity);
				accelerations.add(acceleration);
				voltages.add(this.controllerOutput(acceleration, velocity));
				
				previousTime = simulatedTime;
				simulatedTime += dt;
			}
		}
		
		
	}
	
	public void generatePointTurn(double angle) {
		double arcDistance = angle/360*Math.PI*wheelBaseWidth;
		this.trapezoidalV2(arcDistance);
		this.executeTrapezoidalProfile(true);
	}
	
	public void generatePointTurnV2(double angle) {
		
	}
	
}
