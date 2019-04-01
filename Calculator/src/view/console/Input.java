package view.console;

import java.util.*;
import methods.Calculate;

public class Input {

	public void doTheThing() {
		Scanner input = new Scanner(System.in);
		Calculate calc = new Calculate();
		int op;
		double x;
		double y;

		System.out.println("Welcome to Noocy.Calculate!");
		System.out.print("Please enter your first number: ");
		x = input.nextDouble();
		System.out.print("Please enter your second number: ");
		y = input.nextDouble();

		System.out.print("Please choose an operator (1 = +, 2 = -, 3 = *, 4 = %): ");
		op = input.nextInt();

		for (;;) {
			if (op == 1) {
				System.out.println("The answer is " + calc.add(x, y));
				break;
			} else if (op == 2) {
				System.out.println("The answer is " + calc.subtract(x, y));
				break;
			} else if (op == 3) {
				System.out.println("The answer is " + calc.multi(x, y));
				break;
			} else if (op == 4) {
				System.out.println("The answer is " + calc.divis(x, y));
				break;
			} else {
				System.out.println("Sorry, that's incorrect.");
				System.out.print("Please choose an operator (1 = +, 2 = -, 3 = *, 4 = %): ");
				op = input.nextInt();
			}
		}

		/*
		 * switch(op){ case 1: System.out.println("The answer is " + calc.add(x, y));
		 * break; case 2: System.out.println("The answer is " + calc.subtract(x, y));
		 * break; case 3: System.out.println("The answer is " + calc.multi(x, y));
		 * break; case 4: System.out.println("The answer is " + calc.divis(x, y));
		 * break; default: System.out.println("Sorry, that is incorrect."); break; }
		 */
		input.close();

	}

}
