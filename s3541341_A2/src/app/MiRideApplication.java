package app;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;
import exception.*;
import io.Persistence;
/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * 					Further edited by the student assessed
 * Author:			Rodney Cocker
					Edited: Nicholas Balliro - s3541341
 */
public class MiRideApplication {
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;

	// UPDATE: Creating two creation methods; one for Standard, one for Silver Service
	public String createStandardCar(String id, String make, String model, String driverName, int numPassengers){
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
			double bookingFee, String[] refreshments) throws InvalidRefreshmentException{
		String validId = isValidId(id);
		if (isValidId(id).contains("Error:")) {
			return validId;
		}
		
		//Check for duplicate values in the array
		for (int j=0; j < refreshments.length; j++) {
			  for (int k= j + 1; k < refreshments.length ; k++) {
			    if (k != j && refreshments[k].equals(refreshments[j])) {
					throw new InvalidRefreshmentException("Error: Duplicate Refreshments");
				}
			}
		}
		
		//Check if refreshments are less than 3
		if (refreshments.length < 3) {
			throw new InvalidRefreshmentException("Error: Must be at least 3 Refreshments");
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
			String registrationNumber) throws InvalidIDException{
		Car car = getCarById(registrationNumber);
		if (car != null) {
			//ID Exception to ensure names are at least 3 characters wrong
			if(firstName.length() < 3 || lastName.length() < 3) {
				throw new InvalidIDException("Error: Names must be more than 3 characters");
			}
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
				System.out.println("Error: Seed Data cannot run");
				System.out.println("Data has been processed and cannot be overwritten.");
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

		Car mazda = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = mazda;
		itemCount++;
		mazda.book("Rodney", "Cocker", new DateTime(1), 4);

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
		
		//SILVER SERVICE SEEDS ______________________________________________
		// 2 cars not booked
		String [] refreshmentsJ = {"Bubble Tea","Irish Latte","Sconnes"};
		SilverServiceCar ferrari = new SilverServiceCar("NEG289", "Ferrari", "F8 Tributo", "Giuseppe Balliro", 5, 7.50, refreshmentsJ);
		cars[itemCount] = ferrari;
		itemCount++;

		String[] refreshmentsK = {"Monster", "Red Bull", "V"};
		SilverServiceCar kLexus = new SilverServiceCar("KAM666", "Lexus", "LC 500", "Keith Muss", 5, 5.00, refreshmentsK);
		cars[itemCount] = kLexus;
		itemCount++;

		// 2 cars booked
		String[] refreshmentsR = {"Margharita", "Pinot Noir", "Scotch"};
		SilverServiceCar bugatti = new SilverServiceCar("BLZ420", "Bugatti", "Type 57SC", "Ricki Boyd", 5, 50.25, refreshmentsR);
		cars[itemCount] = bugatti;
		itemCount++;
		bugatti.book("Shania", "Kufner", new DateTime(1), 1);
		
		String[] refreshmentsD = {"Gatorade", "V", "Voss Water"};
		SilverServiceCar audi = new SilverServiceCar("STI626", "Audi", "R8", "Dannii Fourie", 2, 10.50, refreshmentsD);
		cars[itemCount] = audi;
		itemCount++;
		audi.book("Robyn", "Fourie", new DateTime(1), 1);

		// 2 cars booked full (not available)
		String[] refreshmentsN = {"Souffle","Pudding","Trifle"};
		SilverServiceCar porsche = new SilverServiceCar("SNO450", "Porsche", "911 GT2-RS", "Nicholas Balliro", 5, 25.00, refreshmentsN);
		cars[itemCount] = porsche;
		itemCount++;
		porsche.book("Jessica", "Wynn", new DateTime(1), 3);
		porsche.book("Immogen", "Fairbank", new DateTime(2), 7);
		porsche.book("Dannii", "Fourie", new DateTime(3), 3);
		porsche.book("Sheridan", "Brice", new DateTime(4), 7);
		porsche.book("Brittany", "Elliot", new DateTime(5), 7);
		porsche.book("Natalie", "Carman", new DateTime(6), 7);
		porsche.book("Claire", "McLeod", new DateTime(7), 7);
		
		String[] refreshmentsC = {"Carbonara","Ravioli","Spaghetti"};
		SilverServiceCar lykan = new SilverServiceCar("CAR006", "Lykan", "Hypersport", "Jarrod Carmichael", 5, 17.50, refreshmentsC);
		cars[itemCount] = lykan;
		itemCount++;
		lykan.book("Shantel", "Macauley", new DateTime(1), 3);
		lykan.book("Bianca", "Bisaggeo", new DateTime(2), 7);
		lykan.book("Simone", "McKay", new DateTime(3), 3);
		lykan.book("Alannah", "Eileen", new DateTime(4), 7);
		lykan.book("Freya", "Cook", new DateTime(5), 7);
		lykan.book("Cathryn", "Treven", new DateTime(6), 7);
		lykan.book("Taylah", "Clayton", new DateTime(7), 7);
		
		System.out.println("Seed Data successful!");
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

			System.out.print("Enter Sort Order (A/D): ");
			String order = console.nextLine();

			if (order.equalsIgnoreCase("A")) {
				ascSortString(filteredCars);
			} else if (order.equalsIgnoreCase("D")) {
				descSortString(filteredCars);
			} else {
				System.out.println("Invalid Seletion");
				return "";
			}

			sb.append("Summary of all cars: ");
			sb.append("\n");

			// build the string from the filtered array

			for (int i = 0; i < itemCount; i++) {
				if (filteredCars[i] != null) {
					sb.append(filteredCars[i]);
				}
			}
		} else if (type.equalsIgnoreCase("SS")) {
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

			System.out.print("Enter Sort Order (A/D): ");
			String order = console.nextLine();

			if (order.equalsIgnoreCase("A")) {
				//initiate Ascending order
				ascSortString(filteredCars);
			} else if (order.equalsIgnoreCase("D")) {
				//initiate Descending order
				descSortString(filteredCars);
			} else {
				System.out.println("Invalid Selection");
				return "";
			}

			sb.append("Summary of all cars: ");
			sb.append("\n");

			// build the string from the filtered array
			for (int i = 0; i < itemCount; i++) {
				if (filteredCars[i] != null) {
					sb.append(filteredCars[i]);
				}
			}
		} else {
			System.out.println("Invalid Selection");
			return "";
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
	/*Search Available
	 * Allows user to search for a car by date
	*/
	public String searchAvailable() {
		boolean located = false;
		StringBuilder sb = new StringBuilder();
		Car[] filteredCars = new Car[15];
		Scanner console = new Scanner(System.in);
		System.out.print("Enter Type (SD/SS): ");
		String type = console.nextLine();
		
		if (type.equalsIgnoreCase("SD")) {
			if (itemCount == 0) {
				return "No cars have been added to the system.";
			}
			
			//Standard Car
			for (int g = 0; g < cars.length; g++) {
				if (cars[g] instanceof Car && !(cars[g] instanceof SilverServiceCar)) {
					if (cars[g] != null) {
						filteredCars[g] = cars[g];
					}
				}
			}

			//Prompt user for date entry
			System.out.print("Date: ");
			String dateEntered = "";
			int day = 0;
			int month = 0;
			int year = 0;
			//Fixing exception for wrong date
			try {
			dateEntered = console.nextLine();
			day = Integer.parseInt(dateEntered.substring(0, 2));
			month = Integer.parseInt(dateEntered.substring(3, 5));
			year = Integer.parseInt(dateEntered.substring(6));
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("Invalid Entry");
				return "";
			}
			DateTime dateRequired = new DateTime(day, month, year);
			
			
			if(!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThanXDays(dateRequired, 7))
			{
				return "Date is invalid, must be within the coming week.";
			}
			//Nested Loops and Statements to match the bookings of entered and existing
			located = false;
			for (int a = 0; a < filteredCars.length; a++) {
				if (filteredCars[a] != null) {
					if (filteredCars[a].getCurrentBookings() != null) {
						for (int b = 0; b < filteredCars[a].getCurrentBookings().length; b++) {
							if (filteredCars[a].getCurrentBookings()[b] != null) {
								if (filteredCars[a].getCurrentBookings()[b].getDateBooked().equals(dateRequired.getEightDigitDate())) {
									sb.append(filteredCars[a].getDetails());
									located = true;
								}
							}
						}
					}
				}
			} if (located == true) {
				return sb.toString();
			}
		} else if (type.equalsIgnoreCase("SS")) {
			if (itemCount == 0) {
				return "No cars have been added to the system.";
			}
			
			//Silver car
			for (int g = 0; g < cars.length; g++) {
				if (cars[g] instanceof SilverServiceCar) {
					if (cars[g] != null) {
						filteredCars[g] = cars[g];
					}
				}
			}

			//Prompt user for date entry
			System.out.print("Date: ");
			String dateEntered = "";
			int day = 0;
			int month = 0;
			int year = 0;
			try {
			dateEntered = console.nextLine();
			day = Integer.parseInt(dateEntered.substring(0, 2));
			month = Integer.parseInt(dateEntered.substring(3, 5));
			year = Integer.parseInt(dateEntered.substring(6));
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("Invalid Entry");
				return "";
			}
			DateTime dateRequired = new DateTime(day, month, year);
			
			if(!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThanXDays(dateRequired, 7))
			{
				return "Date is invalid, must be within the coming week.";
			}
			for (int a = 0; a < filteredCars.length; a++) {
				if (filteredCars[a] != null) {
					if (filteredCars[a].getCurrentBookings() != null) {
						for (int b = 0; b < filteredCars[a].getCurrentBookings().length; b++) {
							if (filteredCars[a].getCurrentBookings()[b] != null) {
								if (filteredCars[a].getCurrentBookings()[b].getDateBooked().equals(dateRequired.getEightDigitDate())) {
									sb.append(filteredCars[a].getDetails());
									located = true;
								} 
							}
						}
					}
				} 
			} 
			if (located == true) {
				return sb.toString();
			}
		} 		
		return "Error: Car could not be located.";
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
	
	//Loads the array from the Persistence file into the current Cars array
	public void loadData() throws IOException {
		Persistence readWrite = new Persistence();
		try {
			//Swap whole array into the Cars array
			cars = readWrite.readCarData("data.txt");
			itemCount = 0;
			//Loop to increase Item Count back to its respective value
			for (int i = 0; i < cars.length - 1; i++) {
				if (cars[i] != null) {
					itemCount++;
				}
			}
		} catch (CorruptedFileException e) {
			System.out.println("ERROR");
		}
	}
	
	public void loadBackup() throws IOException {
		Persistence readWrite = new Persistence();
		try {
			//Swap whole array into the Cars array
			cars = readWrite.readCarData("backup.txt");
			itemCount = 0;
			//Loop to increase Item Count back to its respective value
			for (int i = 0; i < cars.length - 1; i++) {
				if (cars[i] != null) {
					itemCount++;
				}
			}
		} catch (CorruptedFileException e) {
			System.out.println("ERROR");
		}
	}


	// Method used when exiting in Menu
	public void exitScheme() throws IOException{
		//Create a file and save all cars to string
		Persistence readWrite = new Persistence();
		readWrite.saveCars(cars);
	}
}
