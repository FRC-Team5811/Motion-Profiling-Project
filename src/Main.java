
public class Main {
	public static void main(String args[]) {
		
		//Subsystem drivetrain = new Subsystem(60.0, 0, 6, 7.1); 
		//Elevator elevator = new Elevator(10.0, 0, 1, 10, 2); 
		Drivetrain skrtMobile = new Drivetrain(110.0, 0, 4, 10.7, 6.0, 28.0);
		//System.out.println(elevator.simulate(12.0, 3.0));
		//System.out.println(skrtMobile.simulate(2.0, 3.0));
		//TrapezoidalTest test = new TrapezoidalTest(skrtMobile);
		
		//skrtMobile.generateProfile(120.0);
		//skrtMobile.executeProfile();
		
		Translation shift = new Translation(2.54, 1.48); Translation move;
		
		move = shift.translateBy(shift);
		
//		System.out.println(shift.toText());
//		System.out.println(move.toText());
		
		DCMotorTransmission leftTransmission = new DCMotorTransmission(Constants.kv, Constants.kt, Constants.vIntercept, Constants.resistance, Constants.driveGearRatio, 2);
		DCMotorTransmission rightTransmission = new DCMotorTransmission(Constants.kv, Constants.kt, Constants.vIntercept, Constants.resistance, Constants.driveGearRatio, 2);
		
		DifferentialDrive diff = new DifferentialDrive(Constants.mass, Constants.wheelRadius, Constants.drivebaseWidth, Constants.angularInertia, leftTransmission, rightTransmission);
		
		double leftArc = 5.0;
		double rightArc = -5.0;
		
		diff.composeTransformFromArcs(leftArc, rightArc);
		
		//System.out.println((diff.composeTransformFromArcs(leftArc, rightArc)).translation.x);
		//System.out.println((diff.composeTransformFromArcs(leftArc, rightArc)).translation.y);
		//System.out.println((diff.composeTransformFromArcs(leftArc, rightArc)).rotation.angle);
		
		//skrtMobile.generateTrapezoidalProfile(60.0);
		//skrtMobile.trapezoidalV2(17.0);
		//skrtMobile.executeTrapezoidalProfile(false);
		//skrtMobile.generatePointTurn(152.6);
		
		
		
	}
}
