package cars;

public class SilverServiceCar extends Car {
	// Defining new variables
	private double bookingFee;
	private String[] refreshments;
	private double tripFee = bookingFee * 0.4; 
	
	//Adding constructor for this new class
	public SilverServiceCar(String regNo, String make, String model, String driverName, 
			int passengerCapacity, double bookingFee, String[] refreshments)
	{
		//Super: Calling the previous variables for use in this class
		super(regNo, make, model, driverName, passengerCapacity);
		
		//Booking Fee must be greater than or equal to $3.00
		if (bookingFee >= 3.0) {
			this.bookingFee = bookingFee;
		}
		
		
		
		
	}

}
