package cars;

import utilities.DateTime;

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
	
	/*@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers)
	{
		/*SUPERCLASS: BOOK METHOD***
		 * boolean booked = false;
		// Does car have five bookings
		available = bookingAvailable();
		boolean dateAvailable = notCurrentlyBookedOnDate(required);
		// Date is within range, not in past and within the next week
		boolean dateValid = dateIsValid(required);
		// Number of passengers does not exceed the passenger capacity and is not zero.
		boolean validPassengerNumber = numberOfPassengersIsValid(numPassengers);

		// Booking is permissible
		if (available && dateAvailable && dateValid && validPassengerNumber)
		{
			tripFee = STANDARD_BOOKING_FEE;
			Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
			currentBookings[bookingSpotAvailable] = booking;
			bookingSpotAvailable++;
			booked = true;
		}
		return booked;
	}*/
}
