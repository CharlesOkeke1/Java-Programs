/*
This exercise can be conceptually difficult. 

Read the instructions carefully.

Given an array of directions, left or right, write a method 
that calculates how many rotations of 360 degrees were made. 

Assume each left or right direction represents a 90 degree spin
in that direction.


For example:

["right", "right", "right", "left", "right", "right"]

We spin right three times, giving us a 270 rotation. 

Then, we spin left once, meaning we've actually only
moved 180 degrees to the right and 90 degrees to the
left.

Finally, we spin right twice more, completing our full rotation
to the right.

At the end, we have fully rotated once.

Thus, our method returns 1.

You should assume the directions will be executed in order.

There is no set size of the array. 

We want both left and right full rotations.

For example, assume the following direction list:

["right", "right", "right", "right", "left", "right", "left",
"left", "left", "left", "right", "right", "right", "right"]

The method should return 3 - we complete 2 full rotations to the 
right, and one to the left.

Note that this stipulation means there are some interesting cases you need to
consider.

For example:
["right", "right", "right", "left", "left", "left", "left","right", "right"]

This should return '1' - as we complete 1 full rotation left, but never 
fully turn right. This is because our left turns completely undo our 
progress right.

You may find it helpful to visualise this process by drawing 
a pair of axis and plotting out the turns. 

We may start turning right while facing North.
But we start turning Left facing East.

We move from North to East before we move from East to North.

Once a full turn has been complete, we can treat ourselves as facing
North again.

*/

import java.util.*;

public class Spin
{
	//Main solving method
	public static void solve(List<String> directions){
		int angle = 0; //Turn angle
		int rights = 0; //Number of full right revolutions
		int lefts = 0; //Number of full left revolutions
		int size = directions.size();

		/*Compute the directions*/
		for (int i = 0; i < size; i++) {
			/*Add or subtract 90 per turn*/
			if (directions.get(i).equals("right")) {
				angle += 90;
			} else {
				angle -= 90;
			}
			
			/*Count the full revolutions after four consecutive turns in one direction*/
			if (angle == 360) {
				angle = 0;
				rights++;
			} 
			if (angle == -360) {
				angle = 0;
				lefts++;
			} 

			
		}

		/*Display the results banner with a special delayed display method and then display the results*/
		String report = "===== THE SPIN GAME PRESENTS YOUR SPIN REPORT =====";
		SteppedPrinting(report, 200);

		System.out.println("The total number of turns was " + (rights + lefts) + " turns with " 
		+ lefts + " left turns and " + rights + " right turns."); 
	}

	/* Function to turn cardinal numbers into their ordinal counterparts */
	public static String Ordinalize(int number) {
		if ((number % 100 >= 11) && (number % 100 <= 13)) return number + "th";

		switch (number % 10) {
			case 1: return number + "st";
			case 2: return number + "nd";
			case 3: return number + "rd";
			default: return number + "th";
		}	
	}

	public static void SteppedPrinting(String str, int time) {
		String[] list = str.split("");
		int lastIdx = list.length - 1;

		/*Display the message with a delay using thread.sleep() */
		for (int i = 0; i < list.length; i++) {
			if (list[i] == list[lastIdx]) {
				System.out.println(list[i]);
			} else {
				System.out.print(list[i]);
				try {
					// Pause for specified time
					Thread.sleep(time); 
				} catch (InterruptedException e) {
					// Handle the interruption if needed
					Thread.currentThread().interrupt();
					System.err.println("Thread was interrupted.");
				}
			}
		}

	}

	/*Function to initialize the game and retrive necessary information from the user*/	
	public static void initializer(List<String> directions) {
		Scanner sc = new Scanner(System.in);
		StringBuilder sb = new StringBuilder("");

		/*Ask the number if they want a random set of turns else they input theirs */
		System.out.println("Do you want a random list of directions? "); 
		System.out.print("(If you don't, you will have to input your own directions) (y/n): "); 
		String choice = sc.nextLine();

		System.out.print("How many directions do you want? "); 
		int choiceNumber = sc.nextInt();
		sc.nextLine();
		int size = choiceNumber;
		int count = 0;

		/*Branch applies if the user doesn't want random turns */
		if (choice.equals("n")) {
			/*Collect the users inputed turns and add them to the list */
			while (count < choiceNumber) {
				System.out.print("Enter the " + Ordinalize(count + 1) + " direction: ");
				String input = sc.nextLine();
				directions.add(input);
				count++;
			}
		/*Branch if the user wants random turns */
		} else {
			/*Randomly generate turns and adds them to the list */
			while (count < choiceNumber) {
				if (Math.random() > 0.5) {
					directions.add("right");
				} else {
					directions.add("left");
				}
				count++;
			}
			
		}

		/*Compute the final list and displays it to the user */
		sb.append("{");
		for (int i = 0; i < size; i++) {
			sb.append(directions.get(i));
			if (i < directions.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");

		/*If the list number of turns is less than 50, show the full list */
		if (size < 50) { 
			System.out.println(sb);
		/*If the user doesn't mind the size then show the full thing */
		} else {
			System.out.println("List is too long to print well but the list exists.");
			System.out.println("If you dont mind the format and want to see the full list, type y");
			String c = sc.nextLine();

			if (c.equals("y")) System.out.println(sb);
		}

	}
	

	public static void main(String[] args) {
		/*Create the directions list */
		ArrayList<String> directions = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		boolean confirmed = false;

		String wlcmMssg = "===== WELCOME TO THE SPIN GAME =====";

		SteppedPrinting(wlcmMssg, 200); //Run the welcome function
		
		/*If the user is happy with their array, then the solve function is called and results displayed
		else the initialization process starts again */
		while (!confirmed) {
			initializer(directions);		

			System.out.println("Is this list ok with you? (y/n): "); 
			String choiceN = sc.nextLine();

			if (choiceN.equals("y")) confirmed = true;
			else directions.clear(); 
		}
		
		solve(directions);

	}


}
