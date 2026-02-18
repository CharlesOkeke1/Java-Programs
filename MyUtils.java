import java.util.*;

public class MyUtils {

    /*Function that takes a string and a number and prints each character
    one by one with a delay of the number specified which ic time in milliseconds*/
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
					// Pause for specified time in milliseconds
					Thread.sleep(time); 
				} catch (InterruptedException e) {
					// Handle the interruption if needed
					Thread.currentThread().interrupt();
					System.err.println("Thread was interrupted.");
				}
			}
		}

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
    /* ==This is an example of how to use a custom object class to store 
    variables of different types together
    public static class Example {
        type var1;
        type var2;
        type var3;

        public SpinResult(type var1, type var2, type var3) {
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }
    }
    
    return new Example(var1, var2, var3);

    == USAGE ==
    Example usage1 = function(param);
    */


    /*== ADJUST TO SUIT USAGE ==*/
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

}
