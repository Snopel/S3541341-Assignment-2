package app;

import java.util.Scanner;

import exception.*;
import utilities.DateTime;
import utilities.DateUtilities;
import io.Persistence;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * 				Further edited by the student assessed
 * Author:		Rodney Cocker
				Edited: Nicholas Balliro - s3541341
 */
public class Menu
{
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	//Instantiating the persistence class
	private Persistence io = new Persistence();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run()
	{
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		do
		{
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH)
			{
				System.out.println("Error - selection must be two characters!");
			} else
			{
				System.out.println();

				switch (input)
				{
				case "CC":
					createCar();
					break;
				case "BC":
					book();
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					System.out.println(application.displayAllBookings());
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					System.out.println(application.searchAvailable());
					break;
				case "SD":
					application.seedData();
					break;
				case "LD":
					break;
				case "EX":
					System.out.println("Exiting Program ... Goodbye!");
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
				}
			}

		} while (choice != "EX");
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar() 
	{
		String id = "", make, model, driverName;
		int numPassengers = 0;
		String service = "";
		double bookingFee = 0.0;
		String refreshmentsString;
		String[] refreshments = new String[3];

		System.out.print("Enter registration number: ");
		id = promptUserForRegNo();
		if (id.length() != 0)
		{
			// Get details required for creating a car.
			System.out.print("Enter Make: ");
			make = console.nextLine();

			System.out.print("Enter Model: ");
			model = console.nextLine();

			System.out.print("Enter Driver Name: ");
			driverName = console.nextLine();
			try { 
			System.out.print("Enter number of passengers: ");
			numPassengers = Integer.parseInt(console.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Error: Only a number can be entered.");
				return;
			}
			
			//UPDATE: Adding the SD / SS choice function
			System.out.print("Enter Service Type (SD/SS): ");
			service = console.nextLine().toUpperCase();
			
			if(service.equals("SD")) {
				boolean result = application.checkIfCarExists(id);

				if (!result)
				{
					String carRegistrationNumber = application.createStandardCar(id, make, model, driverName, numPassengers);
					System.out.println(carRegistrationNumber);
				} else
				{
					System.out.println("Error - Already exists in the system");
				}
			} else if (service.equals("SS")) {
				System.out.print("Enter Standard Fee: ");
				try {
				bookingFee = Double.parseDouble(console.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Error: Invalid input");
					
				}
				
				
				/*ALGORITHM
				 * START
				 * 	GET user input for refreshments separated by comma ,
				 * 	ASSIGN input to a refreshment string
				 * 	SPLIT refreshment string by comma
				 * 	ASSIGN string segments into refreshments array
				 * 	CHECK array length (If length = 1, lines were not separated)
				 *  IF length = 1
				 * 		ERROR
				 * 		RETURN to menu
				 * ELSE
				 * 		PROCEED with creation
				 * 	END
				 */
				
				System.out.print("Enter List of Refreshments\n"
						+ "(Separate Refreshments by comma ','): ");
				refreshmentsString = console.nextLine();
				refreshments = refreshmentsString.split(",");
				if (refreshments.length == 1) {
					System.out.println("Error: Invalid Input");
					return;
				}
				// Continue with booking the car
				boolean result = application.checkIfCarExists(id);
				String carRegistrationNumber = "";
				if (!result) {
					try {
					carRegistrationNumber = application.createSilverCar(id, make, model, driverName,
							numPassengers, bookingFee, refreshments);
					} catch (InvalidRefreshmentException e) {
						System.out.println(e.getMessage());
					}
					System.out.println(carRegistrationNumber);
				} else {
					System.out.println("Error - Already exists in the system");
				}
			} else {
				System.out.println("Error: Invalid Input");
			}
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book()
	{	
		int day = 0;
		int month = 0;
		int year = 0;
		System.out.println("Enter date car required.");
		System.out.print("(Format DD/MM/YYYY): ");
		String dateEntered = "";
		dateEntered = console.nextLine();
		
		try {
		day = Integer.parseInt(dateEntered.substring(0, 2));
		month = Integer.parseInt(dateEntered.substring(3, 5));
		year = Integer.parseInt(dateEntered.substring(6));
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Error: Invalid Entry");
			return false;
		}
		DateTime dateRequired = new DateTime(day, month, year);
		
		if(!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThanXDays(dateRequired, 7))
		{
			System.out.println("Date is invalid, must be within the coming week.");
			return false;
		}
		
		String[] availableCars = application.book(dateRequired);
		for (int i = 0; i < availableCars.length; i++)
		{
			System.out.println(availableCars[i]);
		}
		if (availableCars.length != 0)
		{
			System.out.println("Please enter a number from the list:");
			int itemSelected = Integer.parseInt(console.nextLine());
			
			String regNo = availableCars[itemSelected - 1];
			regNo = regNo.substring(regNo.length() - 6);
			
			String firstName = "";
			String lastName = "";
			System.out.println("Please enter your first name:");
			firstName = console.nextLine();
			System.out.println("Please enter your last name:");
			lastName = console.nextLine();
			System.out.println("Please enter the number of passengers:");
			int numPassengers = 0;
			try {
			numPassengers = Integer.parseInt(console.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Error: Invalid input");
				return false;
			}
			String result = "";
			try {
				result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);
			} catch (InvalidIDException e) {
				System.out.println(e.getMessage());
			}

			System.out.println(result);
		} else
		{
			System.out.println("There are no available cars on this date.");
		}
		return true;
	}
	
	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking()
	{
		System.out.print("Enter Registration or Booking Date:");
		String response = console.nextLine();
		
		String result;
		// User entered a booking date
		if (response.contains("/"))
		{
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			System.out.print("Enter kilometers:");
			double kilometers = Double.parseDouble(console.nextLine());
			int day = Integer.parseInt(response.substring(0, 2));
			int month = Integer.parseInt(response.substring(3, 5));
			int year = Integer.parseInt(response.substring(6));
			DateTime dateOfBooking = new DateTime(day, month, year);
			result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
		} else
		{
			
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			if(application.getBookingByName(firstName, lastName, response))
			{
				System.out.print("Enter kilometers:");
				double kilometers = Double.parseDouble(console.nextLine());
				result = application.completeBooking(firstName, lastName, response, kilometers);
				System.out.println(result);
			}
			else
			{
				System.out.println("Error: Booking not found.");
			}
		}
		
	}

	/*
	 * Prompt user for registration number and validate it is in the correct form.
	 * Boolean value for indicating test mode allows by passing validation to test
	 * program without user input validation.
	 */
	private String promptUserForRegNo()
	{
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation)
		{
			return console.nextLine();
		} 
		else
		{
			while (!validRegistrationNumber)
			{
				regNo = console.nextLine();
				boolean exists = application.checkIfCarExists(regNo);
				if(exists)
				{
					// Empty string means the menu will not try to process
					// the registration number
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0)
				{
					break;
				}

				String validId = application.isValidId(regNo);
				if (validId.contains("Error:"))
				{
					System.out.println(validId);
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				} else
				{
					validRegistrationNumber = true;
				}
			}
			return regNo;
		}
	}
	
	/*
	 * Prints the menu.
	 */
	private void printMenu()
	{
		System.out.printf("\n********** MiRide System Menu **********\n\n");

		System.out.printf("%-30s %s\n", "Create Car", "CC");
		System.out.printf("%-30s %s\n", "Book Car", "BC");
		System.out.printf("%-30s %s\n", "Complete Booking", "CB");
		System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
		System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
		System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
		System.out.printf("%-30s %s\n", "Seed Data", "SD");
		System.out.printf("%-30s %s\n", "Exit Program", "EX");
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
}
