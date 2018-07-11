
import java.util.*;

public class TrapezoidalTest {

	double timeToMaxSpeed, timeToZeroSpeed, upDownDistance, distance, cruisingDistance, freeSpeed, maxAccel, maxDecel;
	double dt = 0.020;
	double simulatedPos = 0.0, simulatedTime = 0.0;
	double acceleration = 0.0, velocity = 0.0;
	
	ArrayList<Double> accelerations = new ArrayList();
	ArrayList<Double> velocities = new ArrayList();
	ArrayList<Double> positions = new ArrayList();
	//double[] waypoint = new double[2]; //replaced by ugly thing above
	
	public TrapezoidalTest(Subsystem s) { //subsystem object
		freeSpeed = s.freeSpeed;
		maxAccel = s.maxAcceleration;
		maxDecel = s.maxDeceleration;		
		timeToMaxSpeed = freeSpeed/maxAccel;
		timeToZeroSpeed = freeSpeed/maxDecel;
		upDownDistance = (freeSpeed)*(timeToMaxSpeed);// + 1/2*(freeSpeed)*(timeToZeroSpeed);
	}
	
	void generateProfile(double d){
		distance = d;
		if(upDownDistance<distance) { //Generate a trapezoidal profile
			cruisingDistance = distance - upDownDistance;
			System.out.println("accel decel dist: " + upDownDistance + " cruising distance" + cruisingDistance);
			while(simulatedTime < timeToMaxSpeed) {  //Add acceleration waypoints
				simulatedPos += velocity*dt;
				velocity = Math.min(velocity + maxAccel*dt,freeSpeed);
				accelerations.add(maxAccel);
				velocities.add(velocity);
				positions.add(simulatedPos);
				simulatedTime += dt;
			}
			velocity = freeSpeed; 
			for(int i =0; i < cruisingDistance/freeSpeed/0.02; i++) { //Add cruising waypoints
				simulatedPos += velocity*dt;
				accelerations.add(0.0);
				velocities.add(freeSpeed);
				positions.add(simulatedPos);
				simulatedTime +=dt;
			}
			while(simulatedTime < timeToMaxSpeed+cruisingDistance/freeSpeed+timeToZeroSpeed) { //Add cruising waypoints
				simulatedPos += velocity*dt;
				velocity = Math.max(velocity - maxDecel*dt,0.0);
				accelerations.add(-maxDecel);
				velocities.add(velocity);
				positions.add(simulatedPos);
				simulatedTime += dt;
			}
			System.out.println("Profile generated");
		}else { //Otherwise generate a triangular profile or a lower max velocity trapezoidal profile
			System.out.println("Code a triangular or lower max velocity trapezoidal profile generator already!");
			
		}
		
	}
	
	void executeProfile() {
		for(int i = 0; i< accelerations.size(); i++) {
			System.out.println(accelerations.get(i) + "\t" + velocities.get(i) + "\t" + positions.get(i));
			//System.out.println(); //Output voltage
		}
		System.out.println("Simulated position: " + simulatedPos);
	}

}
