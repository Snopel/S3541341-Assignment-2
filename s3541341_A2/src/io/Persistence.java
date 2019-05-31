
package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import cars.Car;
import exception.CorruptedFileException;
import cars.*;

public class Persistence {

	/*
	 * Creates and gets reference to a file via FileWriter Using BufferedWriter for
	 * efficiency of management PrintWriter is responsible for the sending of data.
	 */

	public void saveCars(Car[] cars) throws IOException {
		PrintWriter prWr = new PrintWriter(new BufferedWriter(new FileWriter("data.txt")));

		for (Car car : cars) {
			if (car != null) {
				String carString = car.toString();
				prWr.println(carString);
			}
		}
		prWr.close();
	}

	public Car[] readCarData(String fileName) throws IOException, CorruptedFileException {
		// Creating a file reading objects
		BufferedReader buff = new BufferedReader(new FileReader(fileName));

		String line = null;

		// Creating an array of cars to return at end of function
		Car[] records = new Car[15];

		int i = 0;
		while ((line = buff.readLine()) != null) {
			StringTokenizer inReader = new StringTokenizer(line, ":");
			
			
			// Check file for potential corruption
			// checkFileIntegrity(inReader);

			// Find a keyword to prove it's either a standard car or a silver car
			if (line.contains("Item")) {
				Car car = recoverSilverCar(inReader);
				// Populate the array
				records[i] = car;
			} else {
				Car car = recoverStandardCar(inReader);
				records[i] = car;
			}
			
			i++;
		}
		// Close to prevent resource leak
		buff.close();
		return records;
	}

	private void checkFileIntegrity(StringTokenizer inReader) throws IOException, CorruptedFileException {
		if (inReader.countTokens() != 2) {
			throw new CorruptedFileException("FATAL ERROR: File has been corrupted.");
		}
	}
	
	//Method to recover the saved silver service car into the records array
	private Car recoverSilverCar(StringTokenizer inReader) {
		String regNo = inReader.nextToken();
		String make = inReader.nextToken();
		String model = inReader.nextToken();
		String driverName = inReader.nextToken();
		int passengerCapacity = Integer.parseInt(inReader.nextToken());
		//Available is unnecessary. Using token to let it pass by
		String available = inReader.nextToken();
		double bookingFee = Double.parseDouble(inReader.nextToken());
		String[] refreshments = inReader.nextToken().split("/");
		
		SilverServiceCar car = new SilverServiceCar(regNo, make, model, driverName, passengerCapacity, bookingFee, refreshments);
		System.out.println("Silver Car Loaded!");
		return car;
	}
	
	//Method to recover the saved silver service car into the records array
	private Car recoverStandardCar(StringTokenizer inReader) {
		String regNo = inReader.nextToken();
		String make = inReader.nextToken();
		String model = inReader.nextToken();
		String driverName = inReader.nextToken();
		int passengerCapacity = Integer.parseInt(inReader.nextToken());
		
		Car car = new Car(regNo, make, model, driverName, passengerCapacity);
		System.out.println("Standard Car Loaded!");
		return car;
	 }

}
