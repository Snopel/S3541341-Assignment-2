package cars;

import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		SilverServiceCar
 * Description:	This is the child class of the Car class.
 * 				Extends upon Car to create a new luxury system
 * 				with refreshments.
 * Author:		Nicholas Balliro - s3541341
 */
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
		this.refreshments = refreshments;
		for (int i = 0; i < refreshments.length; i++) {
			refreshments[i] = refreshments[i].trim();
		}
	}

	// Overriding the Book method so that bookings can only be made 3 days in
	// advance.
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) {
		// Check the restrictive condition i.e not more than 3 days
		// if it falls outside 3 days, then return false;
		DateTime now = new DateTime();
		if (DateTime.diffDays(now, required) > 3) {
			return false;
		}
		return super.book(firstName, lastName, required, numPassengers);
	}

	// Overriding the getDetails method to display current bookings and
	// refreshments.
	@Override
	public String getDetails() {
		// Add in the booking fee change and the refreshments
		String overString = "";
		overString = super.getDetails();
		overString += ("\nBooking Fee: " + bookingFee + "\nRefreshments Available:\n");
		for (int i = 0; i < refreshments.length; i++) {
			overString += ("\nItem " + (i + 1) + ": " + refreshments[i]);
		}
		overString += "\n\n";

		// Add in the current bookings and past bookings
		if (getCurrentBookings()[0] != null) {
			overString += "CURRENT BOOKINGS \n";
			for (int i = 0; i < getCurrentBookings().length; i++) {
				if (getCurrentBookings()[i] == null) {
					continue;
				} else {
					overString += getCurrentBookings()[i].getDetails();
				}
			}
		} 

		if (getPastBookings()[0] != null) {
			overString += "PAST BOOKINGS \n";
			for (int i = 0; i < getPastBookings().length; i++) {
				if (getPastBookings()[i] == null) {
					continue;
				} else {
					overString += getPastBookings()[i].getDetails();
				}
			}
		} 

		return overString;
	}
	
	// Overriding the getString Method
	@Override
	public String toString() {
		String overString = "";
		overString = super.toString();
		overString += (":" + bookingFee + ":");
		for (int i = 0; i < refreshments.length; i++) {
			//Adding "/" delimiter for Persistence
			overString += ("Item " + (i + 1) + " " + refreshments[i] + "/");
		}

		// Add in the current bookings and past bookings
		if (getCurrentBookings() != null) {
			//Appending with '|' so the Persistence operation doesn't include bookings
			overString += "|";
			for (int i = 0; i < getCurrentBookings().length; i++) {
				if (getCurrentBookings()[i] == null) {
					continue;
				} else{
					overString += getCurrentBookings()[i].toString();
				}
			}
		}

		if (getPastBookings() != null) {
			overString += ":";
			for (int i = 0; i < getPastBookings().length; i++) {
				if (getPastBookings()[i] != null) {
					overString += ":";
					for (int j = 0; j < getPastBookings().length; j++) {
						if (getCurrentBookings()[j] == null) {
							continue;
						}
						overString += getPastBookings()[j].toString();
					}
				}
			}
		}

		return overString;
	}

}
