import java.util.*;

public class EmergencyGrid {
    public static int noComms;
    public static class DistrictState {
        int stress;
        int resources;
        int incidents;
        int collapses;

        public DistrictState(int stress, int resources, int incidents, int collapses) {
            this.stress = stress;
            this.resources = resources;
            this.incidents = incidents;
            this.collapses = collapses;       
        }
    }

    public static class SimulationResult {
        Map<String, DistrictState> districtMap;
        int totalCollapses;
        int executedCommands;
        boolean globalFailure;

        public SimulationResult(Map<String, DistrictState> districtMap, int totalCollapses, int executedCommands, boolean globalFailure) {
            this.districtMap = districtMap;
            this.totalCollapses = totalCollapses;
            this.executedCommands = executedCommands;
            this.globalFailure = globalFailure;
        }
    }

    public static SimulationResult simulate(List<String> commands, Map<String, DistrictState> districts) {
        boolean globalFailure = false;
        String effect = "";
        String target = "";
        int totalCollapses = 0;
        int executedCommands = 0;

        while(!globalFailure) {    
            for (int i = 0; i < commands.size(); i++) {
                String[] arr = commands.get(i).split(" ");
                effect = arr[0];
                target = arr[1];
                DistrictState district = districts.get(target);

                if (effect.equals("policy") && target.equals("all")) {
                    for (DistrictState d : districts.values()) {
                        d.stress -= 5;
                        d.resources -= 5;
                    }
                }
                
                switch(effect) {
                    case "incident": 
                        district.incidents++;
                        district.stress += 20;
                        district.resources -= 10;
                        break;
                    
                    case "reinforce":
                        district.resources -= 20;
                        district.stress -= 15;
                        break;

                    case "supply":
                        district.resources += 30;
                        break;

                    case "blackout":
                        district.stress += 30;
                        district.resources -=30;
                        break;

                    default:
                        System.out.println("ERROR - Invalid System Command Detected!");
                        break;
                }

                for (DistrictState d : districts.values()) {
                    d.stress += d.incidents*2;
                    if (d.resources < 20) d.stress += 10;
                    if (d.stress > 70) d.incidents++;
                }
                

                /*Collapse */
                for (DistrictState d : districts.values()) {
                    if ((d.stress >= 100) || (d.resources <= 0)) {
                        d.collapses++;
                        d.stress = 50;
                        d.resources = 50;
                        d.incidents = 0;
                    }
                }

                /*System Shutdown */
                for (DistrictState d : districts.values()) {
                    totalCollapses += d.collapses;
                } 
                if (totalCollapses >= 10) {
                    globalFailure = true;
                    break;
                }
            }
        }     
        return new SimulationResult(districts, totalCollapses, executedCommands, globalFailure);
    }

    /*Function to initialize and retrive necessary information from the user*/	
	public static void initializer(List<String> command) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder("");
        String[] effects = {"incident", "reinforce", "supply", "blackout"};
        String[] districts = {"Birmingham", "Coventry", "Dudley", "Sandwell", "Walsall"};
        
        /*Ask the number if they want a random set of commands else they input theirs */
        MyUtils.SteppedPrinting("Do you want a random list of commands? ", 25);
        MyUtils.SteppedPrinting("Example of a command is supply North, that is (effect district)", 25);
        MyUtils.SteppedPrinting("(If you don't, you will have to input your own commands) (y/n): ", 25);
        String choice = sc.nextLine();

        MyUtils.SteppedPrinting("How many commands do you want? ", 25);
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
                String effect = effects[rand.nextInt(4)];
                String target = districts[rand.nextInt(4)];
                command.add(effect + " " + target);
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

    public static void printReport(Map<String, DistrictState> districts, String countyName) {
        DistrictState county = districts.get(countyName);
    
        MyUtils.SteppedPrinting("===== YOUR " + countyName + " COUNTY REPORT =====", 30);
        MyUtils.SteppedPrinting(countyName + " County Final Stress: " + county.stress, 25);
        MyUtils.SteppedPrinting(countyName + " County Final Resources: " + county.resources, 25);
        MyUtils.SteppedPrinting(countyName + " County Final Incidents: " + county.incidents, 25);
        MyUtils.SteppedPrinting(countyName + " County Final Number of Collapses: " + county.collapses, 25);  
        System.out.println(" ");  
    }

    public static void main(String[] args) {
        /*Create a lsit for the commands */
        ArrayList<String> Commands = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean confirmed = false;

        Map<String, DistrictState> districts = new HashMap<>();

        districts.put("Birmingham", new DistrictState(35, 130, 0, 0));
        districts.put("Coventry", new DistrictState(30, 125, 0, 0));
        districts.put("Dudley", new DistrictState(25, 115, 0, 0));
        districts.put("Sandwell", new DistrictState(15, 110, 0, 0));
        districts.put("Walsall", new DistrictState(20, 100, 0, 0));

        String Welcome = "===== WELCOME TO WEST MIDLAND'S EMERGENCY SERVICES CONTROL GRID =====";
        String managedDistricts = "===== The counties you will be managing include - Birmingham, Coventry, Dudley, Sandwell and Walsall";
        String task = "Your task is to manage the grid by putting in the right commands where necessary and analyzing the generated report! You are also to transmit the report of each county to the West Midlands Combined Health Authority";
        String report = "===== YOUR COUNTY REPORT =====";
        String gridStatus = " ";

        /*Use my custom stepped printing function to animate the messages */
        MyUtils.SteppedPrinting(Welcome, 75);
        MyUtils.SteppedPrinting(managedDistricts, 50);
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
     
        SimulationResult results = simulate(Commands, districts); //fetch the results

        /*Convert the reactor status to online and offline */
        if (results.globalFailure == true) gridStatus = "Offline";
        else gridStatus = "Online";
    
        /*Print the results */
        printReport(districts, "Birmingham");
        printReport(districts, "Coventry");
        printReport(districts, "Dudley");
        printReport(districts, "Sandwell");
        printReport(districts, "Walsall");

        System.out.println("");
        MyUtils.SteppedPrinting(report, 50); /*Use my custom stepped printing function to animate the report title */
        MyUtils.SteppedPrinting("Total Collapses: " + results.totalCollapses, 50);
        MyUtils.SteppedPrinting("Number of Commands Executed before Grid Failure: " + results.executedCommands, 50);
        MyUtils.SteppedPrinting("City Grid Status: " + gridStatus, 50);


    }
}
