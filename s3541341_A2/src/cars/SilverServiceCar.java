package cars;

public class SilverServiceCar extends Car {
	//Adding constructor for this new class
	public SilverServiceCar(String regNo, String make, String model, String driverName, 
			int passengerCapacity, double bookingFee, String[] refreshments)
	{
		//Super: Calling the previous variables for use in this class
		super(regNo, make, model, driverName, passengerCapacity);
		
		
	}

}
