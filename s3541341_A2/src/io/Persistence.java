package io;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import cars.Car;

public class Persistence {
	
	public void saveCars(Car[] cars) throws IOException {
		PrintWriter prWr = new PrintWriter(new BufferedWriter(new FileWriter("data.txt")));
		
		for(Car car: cars) {
			if(car != null) {
				String carString = car.toString();
				prWr.println(carString);
			}
		} prWr.close();
	}
}
