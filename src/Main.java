
public class Main {
	public static void main(String args[]) {
		
		
		//System.out.println("Hello world");
		//Subsystem drivetrain = new Subsystem(60.0, 0, 6, 7.1); 
		//Elevator elevator = new Elevator(10.0, 0, 1, 10, 2); 
		Drivetrain skrtMobile = new Drivetrain(110.0, 0, 4, 10.7, 6.0);
		//System.out.println(elevator.simulate(12.0, 3.0));
		//System.out.println(skrtMobile.simulate(2.0, 3.0));
		//TrapezoidalTest test = new TrapezoidalTest(skrtMobile);
		
		//skrtMobile.generateProfile(120.0);
		//skrtMobile.executeProfile();
		
		//skrtMobile.generateTrapezoidalProfile(60.0);
		skrtMobile.trapezoidalV2(160.0);
		skrtMobile.executeTrapezoidalProfile(false);
		
	}
}
