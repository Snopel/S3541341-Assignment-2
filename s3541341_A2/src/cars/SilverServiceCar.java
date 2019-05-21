package cars;

import utilities.DateTime;
import utilities.DateUtilities;

public class SilverServiceCar extends Car {
	// Defining new variables
	private double bookingFee;
	private String[] refreshments;
	private double tripFee = bookingFee * 0.4;

	// Adding constructor for this new class
	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity,
			double bookingFee, String[] refreshments) {
		// Super: Calling the previous variables for use in this class
		super(regNo, make, model, driverName, passengerCapacity);
		// Booking Fee must be greater than or equal to $3.00
		if (bookingFee >= 3.0) {
			this.bookingFee = bookingFee;
		}
	}

	// Overriding the Book method so that bookings can only be made 3 days in advance.
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers)
	{
		return true;
	}
	
	// Overriding the getDetails method to display current bookings and refreshments.
	@Override
	public String getDetails() {
		// Add in the booking fee change and the refreshments
		String overString = "";
		overString = super.getDetails();
		overString += ("\nBooking Fee: " + bookingFee + "\nRefreshments Available:\n"); 
		for (int i = 0; i < refreshments.length; i++) {
			overString += ("\n" + refreshments[i]);
		}
		overString += "\n\n";
		
		// Add in the current bookings and past bookings
		
		return overString;
	}
	
}
