/*
===========================================================
ORBITAL DRIFT - FORMAL SPECIFICATION
===========================================================

OVERVIEW:
You are managing a satellite attempting to complete full
360-degree orbital rotations around a planet.

A sequence of commands is processed in order.
Each command affects altitude, stability, and fuel.

The goal is to complete as many full orbits as possible
before fuel depletion or instability.

-----------------------------------------------------------
INITIAL STATE:

altitude = 0
stability = 100
fuel = 300
orbits = 0
crashes = 0
gameOver = false

-----------------------------------------------------------
VALID COMMANDS:

"boost"
"drift"
"correct"
"drag"

-----------------------------------------------------------
COMMAND EFFECTS:

boost:
    altitude += 60
    stability -= 10
    fuel -= 40

drift:
    altitude += 30
    stability -= 20

correct:
    stability += 25
    fuel -= 20

drag:
    altitude -= 40
    stability -= 15

-----------------------------------------------------------
PROCESSING ORDER (PER COMMAND):

1. Apply command effects.
2. Clamp altitude >= 0.
3. Clamp stability <= 100.
4. If fuel <= 0:
       gameOver = true
       stop processing.
5. If stability <= 0:
       crashes++
       stability = 100
       altitude = 0
6. While altitude >= 360:
       altitude -= 360
       orbits++

-----------------------------------------------------------
TERMINATION:

Simulation ends when:
- All commands are processed, OR
- fuel <= 0

-----------------------------------------------------------
FINAL REPORT MUST INCLUDE:

- Total orbits completed
- Total crashes
- Final altitude
- Final stability
- Remaining fuel
- Whether simulation ended due to fuel depletion
===========================================================
*/

import java.util.*;

public class OrbitalDrift {

    // ===== Result Type =====
    public static class OrbitResult {
        int orbits;
        int crashes;
        int altitude;
        int stability;
        int fuel;
        boolean gameOver;

        public OrbitResult(int orbits, int crashes, int altitude,
                           int stability, int fuel, boolean gameOver) {
            this.orbits = orbits;
            this.crashes = crashes;
            this.altitude = altitude;
            this.stability = stability;
            this.fuel = fuel;
            this.gameOver = gameOver;
        }
    }

    public static OrbitResult simulate(List<String> commands) {

        int altitude = 0;
        int stability = 100;
        int fuel = 300;

        int orbits = 0;
        int crashes = 0;
        boolean gameOver = false;

        for (String c : commands) {

            switch (c) {
                case "boost":
                    altitude += 60;
                    fuel -= 40;
                    stability -= 10;
                    break;

                case "drift":
                    altitude += 30;
                    stability -= 20;
                    break;

                case "correct":
                    stability += 25;
                    fuel -= 20;
                    break;

                case "drag":
                    altitude -= 40;
                    stability -= 15;
                    break;

                default:
                    System.out.println("ERROR - Invalid command found!");
            }

            // Clamp to prevent unexpected values
            if (stability > 100) stability = 100;
            if (altitude < 0) altitude = 0;

            // Fuel check
            if (fuel <= 0) {
                gameOver = true;
                break;
            }

            // Crash check
            if (stability <= 0) {
                crashes++;
                stability = 100;
                altitude = 0;
            }

            // Orbit check (carryover)
            while (altitude >= 360) {
                altitude -= 360;
                orbits++;
            }
        }

        return new OrbitResult(orbits, crashes, altitude, stability, fuel, gameOver);
    }

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

    public static void initializer(List<String> command) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder("");

        System.out.println("Do you want a random list of commands? ");
        System.out.print("(If you don't, you will have to input your own commands) (y/n): ");
        String choice = sc.nextLine();

        System.out.print("How many commands do you want? ");
        int choiceNumber = sc.nextInt();
        sc.nextLine();

        int count = 0;

        if (choice.equals("n")) {
            while (count < choiceNumber) {
                System.out.print("Enter the " + Ordinalize(count + 1) + " command: ");
                String input = sc.nextLine();
                command.add(input);
                count++;
            }
        } else {
            Random rand = new Random();
            while (count < choiceNumber) {
                int r = rand.nextInt(4);
                switch (r) {
                    case 0: command.add("correct"); break;
                    case 1: command.add("boost"); break;
                    case 2: command.add("drift"); break;
                    case 3: command.add("drag"); break;
                }
                count++;
            }
        }

        sb.append("{");
        for (int i = 0; i < command.size(); i++) {
            sb.append(command.get(i));
            if (i < command.size() - 1) sb.append(", ");
        }
        sb.append("}");

        System.out.println(sb);
    }

    public static void main(String[] args) {

        ArrayList<String> commands = new ArrayList<>();
        initializer(commands);

        OrbitResult result = simulate(commands);

        String Report = "===== ORBITAL DRIFT REPORT =====";
        SteppedPrinting(Report, 200);
        System.out.println("Orbits: " + result.orbits);
        System.out.println("Crashes: " + result.crashes);
        System.out.println("Final Altitude: " + result.altitude);
        System.out.println("Final Stability: " + result.stability);
        System.out.println("Remaining Fuel: " + result.fuel);
        System.out.println("Game Over: " + result.gameOver);
    }
}
