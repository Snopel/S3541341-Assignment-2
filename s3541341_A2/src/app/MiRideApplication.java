package app;

import java.util.*;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker
 */
public class MiRideApplication {
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;

	public MiRideApplication() {
		// seedData();
	}

	// UPDATE: Creating two creation methods; one for Standard, one for Silver
	// Service
	public String createStandardCar(String id, String make, String model, String driverName, int numPassengers) {
		String validId = isValidId(id);
		if (isValidId(id).contains("Error:")) {
			return validId;
		}
		if (!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Standard Car added successfully for registion number: "
					+ cars[itemCount - 1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	public String createSilverCar(String id, String make, String model, String driverName, int numPassengers,
			double bookingFee, String[] refreshments) {
		String validId = isValidId(id);
		if (isValidId(id).contains("Error:")) {
			return validId;
		}
		if (!checkIfCarExists(id)) {
			cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee,
					refreshments);
			itemCount++;
			return "New Silver Service Car added successfully for registion number: "
					+ cars[itemCount - 1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	public String[] book(DateTime dateRequired) {
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (!cars[i].isCarBookedOnDate(dateRequired)) {
					numberOfAvailableCars++;
				}
			}
		}
		if (numberOfAvailableCars == 0) {
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for (int i = 0; i < cars.length; i++) {

			if (cars[i] != null) {
				if (!cars[i].isCarBookedOnDate(dateRequired)) {
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}

	public String book(String firstName, String lastName, DateTime required, int numPassengers,
			String registrationNumber) {
		Car car = getCarById(registrationNumber);
		if (car != null) {
			car.book(firstName, lastName, required, numPassengers);
			String message = "Thank you for your booking. \n" + car.getDriverName() + " will pick you up on "
					+ required.getFormattedDate() + ". \n" + "Your booking reference is: "
					+ car.getBookingID(firstName, lastName, required);
			return message;
		} else {
			return "Car with registration number: " + registrationNumber + " was not found.";
		}
	}

	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) {
		String result = "";

		// Search all cars for bookings on a particular date.
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				result = cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				if (!result.equals("Booking not found")) {
					return result;
				}
			}
		}
		return "Booking not found.";
	}

	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers) {
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equals(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}

		if (car == null) {
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1) {
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}

	public boolean getBookingByName(String firstName, String lastName, String registrationNumber) {
		String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equals(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}

		if (car == null) {
			return false;
		}
		if (car.getBookingByName(firstName, lastName) == -1) {
			return false;
		}
		return false;
	}

	public String displaySpecificCar(String regNo) {
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equals(regNo)) {
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}

	public boolean seedData() {
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				return false;
			}
		}
		// 2 cars not booked
		Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
		cars[itemCount] = honda;
		honda.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;

		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
		lexus.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;

		// 2 cars booked
		Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
		cars[itemCount] = bmw;
		itemCount++;
		bmw.book("Craig", "Cocker", new DateTime(1), 3);

		Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = audi;
		itemCount++;
		audi.book("Rodney", "Cocker", new DateTime(1), 4);

		// 1 car booked five times (not available)
		Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
		cars[itemCount] = toyota;
		itemCount++;
		toyota.book("Rodney", "Cocker", new DateTime(1), 3);
		toyota.book("Craig", "Cocker", new DateTime(2), 7);
		toyota.book("Alan", "Smith", new DateTime(3), 3);
		toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
		toyota.book("Paul", "Scarlett", new DateTime(5), 7);
		toyota.book("Paul", "Scarlett", new DateTime(6), 7);
		toyota.book("Paul", "Scarlett", new DateTime(7), 7);

		// 1 car booked five times (not available)
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		rover.book("Rodney", "Cocker", new DateTime(1), 3);
		// rover.completeBooking("Rodney", "Cocker", 75);
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays, 75);
		
		String[] refreshments = { "Souffle", "Pudding", "Trifle" };
		SilverServiceCar bugatti = new SilverServiceCar("BUG450", "Bugatti", "Veyron", "Ricki Boyd", 4, 5.5, refreshments);
		cars[itemCount] = bugatti;
		itemCount++;
		
		SilverServiceCar maybach = new SilverServiceCar("MAY999", "Maybach", "Excelero", "Jay Z", 6, 1000.0, refreshments);
		cars[itemCount] = maybach;
		itemCount++;
		
		return true;
	}

	public String displayAllBookings() {
		StringBuilder sb = new StringBuilder();
		String[] filteredCars = new String[15];
		Scanner console = new Scanner(System.in);
		System.out.print("Enter Type (SD/SS): ");
		String type = console.nextLine();
		
		if (type.equalsIgnoreCase("SD")) {
			if (itemCount == 0) {
				return "No cars have been added to the system.";
			}
			
			for (int g = 0; g < cars.length; g++) {
				if (cars[g] instanceof Car && !(cars[g] instanceof SilverServiceCar)) {
					if (cars[g] != null) {
						filteredCars[g] = cars[g].getDetails();
					}
				}
			}
			
			System.out.println("Enter Sort Order (A/D): ");
			String order = console.nextLine();
			
			if (order.equalsIgnoreCase("A")) {
				ascSortString(filteredCars);
			} else if (order.equalsIgnoreCase("D")) {
				descSortString(filteredCars);
			}
			
			
			sb.append("Summary of all cars: ");
			sb.append("\n");

			// build the string from the filtered array

			for (int i = 0; i < itemCount; i++) {
				if (filteredCars[i] != null) {
					sb.append(filteredCars[i]);
				}
			}
		}
		
		if (type.equalsIgnoreCase("SS")) {
			if (itemCount == 0) {
				return "No cars have been added to the system.";
			}
			
			for (int g = 0; g < cars.length; g++) {
				if (cars[g] instanceof SilverServiceCar) {
					if (cars[g] != null) {
						filteredCars[g] = cars[g].getDetails();
					}
				}
			}
			
			System.out.println("Enter Sort Order (A/D): ");
			String order = console.nextLine();
			
			if (order.equalsIgnoreCase("A")) {
				ascSortString(filteredCars);
			} else if (order.equalsIgnoreCase("D")) {
				descSortString(filteredCars);
			}
			
			
			sb.append("Summary of all cars: ");
			sb.append("\n");

			// build the string from the filtered array

			for (int i = 0; i < itemCount; i++) {
				if (filteredCars[i] != null) {
					sb.append(filteredCars[i]);
				}
			}
		}
		return sb.toString();
	}

	public String displayBooking(String id, String seatId) {
		Car booking = getCarById(id);
		if (booking == null) {
			return "Booking not found";
		}
		return booking.getDetails();
	}

	public String isValidId(String id) {
		return MiRidesUtilities.isRegNoValid(id);
	}

	public boolean checkIfCarExists(String regNo) {
		Car car = null;
		if (regNo.length() != 6) {
			return false;
		}
		car = getCarById(regNo);
		if (car == null) {
			return false;
		} else {
			return true;
		}
	}

	private Car getCarById(String regNo) {
		Car car = null;

		for (int i = 0; i < cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegistrationNumber().equals(regNo)) {
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	//SORTING ALGORITHMS
	private void ascSortString(String[] sortArr) {
		// SORTING ALGORITHM
		/*
		 * SORTING ALGORITHM - ASCENDING 
		 * START
		 *   GET Registration 
		 *   INSERT into array
		 *	 COMPARE the phrases 
		 * 	 IF current value is GREATER THAN next value ` 
		 * 		SWAP current and next value
		 *  ELSE IF current value is less than or equal to next value 
		 *  	do nothing 
		 *  END IF 
		 *END
		 * 
		* SWAP ALGORITHM 
		* 	START 
		* 		CREATE temporary variable 
		* 		IF SWAP 
		* 			ASSIGN current value to temporary 
		* 			ASSIGN next value to the current spot 
		* 			ASSIGN temporary value into next spot 
		* 		ELSE do nothing 
		* 	END IF 
		* END
		*/

		// sort the filtered array
	    // run a sorting algorithm over the filtered car array

		String temp = "";
		System.out.println();

		for (int i = 0; i < sortArr.length - 1; i++) {
			for (int j = i + 1; j < sortArr.length; j++) {
				if (sortArr[i] != null && sortArr[j] != null) {
					if (sortArr[i].compareToIgnoreCase(sortArr[j]) > 0) {
						temp = sortArr[i];
						sortArr[i] = sortArr[j];
						sortArr[j] = temp;
					}
				}
			}
		}
	}
	private void descSortString(String[] sortArr) {
		// SORTING ALGORITHM
		/*
		 * SORTING ALGORITHM - DESCENDING 
		 * START
		 *   GET Registration 
		 *   INSERT into array
		 *	 COMPARE the phrases 
		 * 	 IF current value is LESS THAN next value ` 
		 * 		SWAP current and next value
		 *  ELSE IF current value is greater than or equal to next value 
		 *  	do nothing 
		 *  END IF 
		 *END
		 * 
		* SWAP ALGORITHM 
		* 	START 
		* 		CREATE temporary variable 
		* 		IF SWAP 
		* 			ASSIGN current value to temporary 
		* 			ASSIGN next value to the current spot 
		* 			ASSIGN temporary value into next spot 
		* 		ELSE do nothing 
		* 	END IF 
		* END
		*/

		// sort the filtered array
	    // run a sorting algorithm over the filtered car array

		String temp = "";
		System.out.println();

		for (int i = 0; i < sortArr.length - 1; i++) {
			for (int j = i + 1; j < sortArr.length; j++) {
				if (sortArr[i] != null && sortArr[j] != null) {
					if (sortArr[i].compareToIgnoreCase(sortArr[j]) < 0) {
						temp = sortArr[i];
						sortArr[i] = sortArr[j];
						sortArr[j] = temp;
					}
				}
			}
		}
	}
}
