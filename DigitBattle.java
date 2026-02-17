import java.util.*;

/*

The digit battle game.

Given a number, we can 'pair off' the digits and have them compete.
In this case, the highest number wins and goes into the 'winning' number. 

For example:

526273445 produces the 'winning' number of 5675.

5 v 2 -> 5
6 v 2 -> 6
7 v 3 -> 7
4 v 4 -> A tie! No number is added to the 'winning' number.
5 - > Wins by default!

So, when digits are the same, neither is added to the 'winning' number
and a lone number wins automatically.

Write a method that, given a long, returns the 'winning' number.

*/

public class DigitBattle
{
	public static long solve(long n){

		/*Create lists to store the input and the output */
		ArrayList<Integer> list = new ArrayList<>();
		ArrayList<Integer> winner = new ArrayList<>();

		/*Extract the digits from the number and add them to the list */
		while (n > 0) {
			list.add((int)(n % 10));
			n /= 10;
		}

		/*Reverse the list to achieve the correct order */
		Collections.reverse(list);

		int size = list.size();
		/*Iterate through the list, compare consecutive numbers and implement the rules of the game */
		for (int i = 0; i < size - 1; i += 2) {
			int a = list.get(i);
			int b = list.get(i+1);

				if (a > b) {
					winner.add(a);
				} else {
					winner.add(b);
				} 
		}
		/*For odd numbers, append the last number to the the winning list*/
		if (size % 2 == 1) {
			winner.add(list.get(size - 1));
		}

		/*Reverse the list to arrange the result in the correct order and concatenate them into the final number*/
		Collections.reverse(winner);
		long finalNo = 0;
		for (int j = winner.size() - 1; j >= 0; j--) {
			finalNo = finalNo * 10 + winner.get(j);
		}

		/*Display and return the number */
		System.out.println("The winning number is: " + finalNo);
		return finalNo;

	}

	public static void main(String[] args) {
		/*Display the welcome message.*/
		Scanner input = new Scanner(System.in);
		String wlcm = "===== WELCOME TO DIGIT BATTLE =====";
		String[] welcome = wlcm.split("");
		int lastIdx = welcome.length - 1;

		/*Display the welcome message with a delay using thread.sleep() */
		for (String c : welcome) {
			if (c == welcome[lastIdx]) {
				System.out.println(c);
			} else {
				System.out.print(c);
				try {
					// Pause for half a second (500 milliseconds)
					Thread.sleep(200); 
				} catch (InterruptedException e) {
					// Handle the interruption if needed
					Thread.currentThread().interrupt();
					System.err.println("Thread was interrupted.");
				}
			}
		}

		while (true) {
			/*Collect the user input and peform the solve method */
			System.out.println("Give me your number:");
			long number = input.nextLong();
			solve(number);

			//Player is given a choice to either continue playing or stop playing.
			System.out.println("Do you want to play again ? (y/n)");
			char choice = input.next().charAt(0);

			if (choice == 'n') {
				System.out.println("Thanks for playing Digit Battle :)");
				break;
			}

		}
	}

}
