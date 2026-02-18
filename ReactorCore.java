/*
===========================================================
REACTOR CORE - FORMAL SPECIFICATION
===========================================================

OVERVIEW:
You are managing a nuclear reactor core.

A sequence of operator commands is processed in order.
Each command affects heat, pressure, control rod position,
and energy production.

The system evolves per command and includes automatic
environmental effects and safety thresholds.

-----------------------------------------------------------
INITIAL STATE:

heat = 0
pressure = 50
controlRodPosition = 50
energyProduced = 0
meltDowns = 0
reactorOffline = false

-----------------------------------------------------------
VALID COMMANDS:

"insert"
"withdraw"
"cool"
"vent"
"generate"

-----------------------------------------------------------
COMMAND EFFECTS:

insert:
    controlRodPosition += stepSize
    heat -= 15

withdraw:
    controlRodPosition -= stepSize
    heat += 20

cool:
    heat -= 25
    pressure -= 10

vent:
    pressure -= 25

generate:
    energyProduced += heat / 5
    heat += 10
    pressure += 15

(stepSize may be 1 or 10 depending on configuration.)

-----------------------------------------------------------
AUTOMATIC ENVIRONMENTAL EFFECTS
(After each command)

heat += 5
pressure += heat / 20

If controlRodPosition < 30:
    heat += 10

-----------------------------------------------------------
PROCESSING ORDER (PER COMMAND):

1. Apply command effects.
2. Apply automatic environmental effects.
3. Clamp heat >= 0.
4. Clamp pressure >= 0.
5. If controlRodPosition <= 0 OR >= 100:
       reactorOffline = true
       stop processing immediately.
6. If heat >= 300 OR pressure >= 250:
       meltDowns++
       heat = 50
       pressure = 50
       controlRodPosition = 70
7. Continue to next command if not offline.

-----------------------------------------------------------
TERMINATION:

Simulation ends when:
- reactorOffline becomes true, OR
- All commands are processed.

-----------------------------------------------------------
FINAL REPORT MUST INCLUDE:

- Total commands received
- Number of commands executed before shutdown
- Final heat
- Final pressure
- Final controlRodPosition
- Total energyProduced
- Total meltDowns
- Final reactor status (Online/Offline)
===========================================================
*/

import java.util.*;

public class ReactorCore {
    public static int noComms;

    /*Create a Core State class object to store the reactor state values*/    
    public static class CoreState {
        int heat;
        int pressure;
        int controlRodPosition;
        int energyProduced;
        int meltDowns;
        int executedComms;
        boolean reactorOffline;

        public CoreState(int heat, int pressure, int controlRodPosition,
                        int energyProduced, int meltDowns, int executedComms, boolean reactorOffline) {
            
            this.heat = heat;
            this.pressure = pressure;
            this.controlRodPosition = controlRodPosition;
            this.energyProduced = energyProduced;
            this.meltDowns = meltDowns;
            this.executedComms = executedComms;
            this.reactorOffline = reactorOffline;
        }
    }

    /*Simulate the reactor operation */
    public static CoreState simulate(List<String> commands) {
        int heat = 0;
        int pressure = 50;
        int controlRodPosition = 50;
        int energyProduced = 0;
        int meltDowns = 0;
        int executedComms = 0;
        boolean reactorOffline = false;

        /*While the reactor has not shutwdown, peform the required computations on
        the reactors state values */
        while (!reactorOffline) {
            for (int i = 0; i < commands.size(); i++) {
                switch (commands.get(i)) {
                    case "insert":
                        controlRodPosition += 1;
                        heat -= 15;
                        break;

                    case "withdraw":
                        controlRodPosition -= 1;
                        heat += 20;
                        break;

                    case "cool":
                        heat -= 25;
                        pressure -= 10;
                        break;

                    case "vent":
                        pressure -= 25;
                        break;

                    case "generate":
                        energyProduced += heat/5;
                        heat += 10;
                        pressure += 15;
                        break;

                    default:
                        System.out.println("ERROR - Invalid command detected!");
                }
            
                /*Automatic Environmental Effects */
                heat += 5;
                pressure += heat/20;
                executedComms = i;

                if (controlRodPosition < 30) {
                    heat += 10;
                }

                /*Control Measures to keep the reactor running properly */
                /*Clamp rules - to prevent unexpected values */
                if (heat < 0) heat = 0;
                if (pressure < 0) pressure = 0;

                /*Shutdown Condition when the rod position is outside of 0 to 100 (inclusive)*/
                if ((controlRodPosition <= 0) || (controlRodPosition >= 100))  {
                    reactorOffline = true;
                    break;
                }  

                /*Threshold Rules to reset heat, pressure and rod position whenever a meltdown occurs*/
                if ((heat >= 300) || (pressure >= 250)) {
                    meltDowns++;
                    heat = 50;
                    pressure = 50;
                    controlRodPosition = 70;
                }         
            }
        }
        /*return the cores final state */
        return new CoreState(heat, pressure, controlRodPosition, energyProduced, meltDowns, executedComms, reactorOffline);
    }

    /*Function to initialize and retrive necessary information from the user*/	
	public static void initializer(List<String> command) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder("");
        
        /*Ask the number if they want a random set of commands else they input theirs */
        System.out.println("Do you want a random list of commands? ");
        System.out.print("(If you don't, you will have to input your own commands) (y/n): ");
        String choice = sc.nextLine();

        System.out.print("How many commands do you want? ");
        int choiceNumber = sc.nextInt();
        sc.nextLine();

        int size = choiceNumber;  
        int count = 0;  

        /*Branch applies if the user doesn't want random commands */
        if (choice.equals("n")) {
            while (count < choiceNumber) {
                /*Collect the users inputed turns and add them to the list */
                System.out.print("Enter the " + MyUtils.Ordinalize(count + 1) + " command: ");
                String input = sc.nextLine();
                command.add(input);
                count++;
            }
            /*Branch if the user wants random commands*/
        } else {
            /*Randomly generate commands and adds them to the list */
            Random rand = new Random();
            while (count < choiceNumber) {
                /*Randomly select between the 5 commands to generate the array of commands */
                int r = rand.nextInt(5);
                switch (r) {
                    case 0: command.add("insert"); break;
                    case 1: command.add("withdraw"); break;
                    case 2: command.add("cool"); break;
                    case 3: command.add("vent"); break;
                    case 4: command.add("generate"); break;
                }
                count++;
            }
        }
        
        /*Compute the final list and displays it to the user */
        sb.append("{");
        for (int i = 0; i < command.size(); i++) {
            sb.append(command.get(i));
            if (i < command.size() - 1) sb.append(", ");
        }
        sb.append("}");

        /*If the list number of commands is less than 50, show the full list */
		if (size < 50) { 
			System.out.println(sb);
		/*If the user doesn't mind the size then show the full thing */
		} else {
			System.out.println("List is too long to print well but the list exists.");
			System.out.println("If you dont mind the format and want to see the full list, type y");
			String c = sc.nextLine();

			if (c.equals("y")) System.out.println(sb);
		}
        /*Store the number of commands stored in the list of commands */
        noComms = choiceNumber;
    }

    public static void main(String[] args) {
        /*Create a lsit for the commands */
        ArrayList<String> Commands = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean confirmed = false;

        String Welcome = "===== WELCOME TO THE REACTOR CORE =====";
        String task = "Your task is to manage the reactor by putting in the right commands where necessary and analyzing the generated report!";
        String report = "===== YOUR REACTOR CORE REPORT =====";
        String reactorStatus = " ";

        /*Use my custom stepped printing function to animate the messages */
        MyUtils.SteppedPrinting(Welcome, 75);
        MyUtils.SteppedPrinting(task, 50);
        MyUtils.SteppedPrinting("Good Luck!", 50);

        /*If the user has not confirmed their array of commands, run the initializer */
        while (!confirmed) {
            initializer(Commands);

            System.out.print("Do you accept your commands? (y/n): ");
            String choice = sc.nextLine();

            if (choice.equals("y")) confirmed = true;
            else Commands.clear();
        }
     
        CoreState results = simulate(Commands); //fetch the results

        /*Convert the reactor status to online and offline */
        if (results.reactorOffline == true) reactorStatus = "Offline";
        else reactorStatus = "Online";
        
        MyUtils.SteppedPrinting(report, 50); /*Use my custom stepped printing function to animate the report title */

        /*Print the results */
        System.out.println("Number of Inputed Commands: " + noComms);
        System.out.println("Number of Executed Commands before shutdown: " + results.executedComms);
        System.out.println("Final Heat: " + results.heat);
        System.out.println("Final Pressure: " + results.pressure);
        System.out.println("Final Control Rod Position: " + results.controlRodPosition);
        System.out.println("Energy Produced: " + results.energyProduced);
        System.out.println("Final MeltDown Count: " + results.meltDowns);
        System.out.println("Final Status: " + reactorStatus);
    }
}
