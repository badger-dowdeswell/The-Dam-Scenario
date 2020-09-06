//
// DIAGNOSTIC TEAM
// ===============
// This class allows the behavior of the individual diagnostic
// agents to be modeled as a team.
//
// AUT University - 2019-2020
//
// Revision History
// ================
// 23.05.2019 BRD Original version. 
//
package TheDamScenario;

import com.intendico.gorite.*;
import com.intendico.gorite.addon.Perceptor;
import TheDamScenario.ExitCodes;
import static TheDamScenario.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiagnosticTeam extends Team {
	// used to turn off and on console messages during development.
	private boolean isSilent = false;

	public static final String FIND_FAULTS = "FIND_FAULTS";
	
	DiagnosticAgent marvin = new DiagnosticAgent("Marvin");
	DiagnosticAgent dennis = new DiagnosticAgent("Dennis");
	
	// Constructor
	public TaskTeam diagnosticTeam = new TaskTeam() {{
		// A diagnoser is a team member of the diagnosticTeam Team who had the
		// roles of configuring the diagnostic system, identifying, diagnosing
		// and reporting faults.
		say("Setting up TaskTeam diagnosticTeam.\nAdding roles for a Diagnoser agent.");
		addRole(new Role(DIAGNOSER, new String[] {
				CONFIGURE_DIAGNOSTICS, WATCH_FOR_FAULTS, DIAGNOSE_FAULTS, REPORT_FAULTS
		}));
	}};

	//
	// DiagnosticTeam()
	// ================
	public DiagnosticTeam(String teamName) {
		super(teamName);
		say("Creating diagnostic agents:");
		
		new Thread(marvin).start();
		say("Created Marvin.");
		
//		
//		new Thread(dennis).start();
//		say ("Created Dennis.");
		
		say("Agents operating..\n");
	
		setTaskTeam("diagnose faults", new TaskTeam() {{
			addRole(new Role(DIAGNOSER, new String[] {FIND_FAULTS}));
		}});
		addGoal(configureDiagnostics());     
		addGoal(watchForFaults());
		addGoal(diagnoseFault());
		addGoal(reportFault());
		
		say("\nTeam created and ready\n");
	}
		
	//
	// findFaults()
	// ============
	// Performs a set of goals sequentially to configure the system, find faults,
	// diagnose them and then report them.
	//
	public boolean findFaults() { 
		boolean goalStatus = false;
		Data data = new Data();
		say("Start the team and instruct them to perform the goals for findFaults().");
		
		// Experimenting with the data environment 
		data.create("count");
		data.setValue("count", 0);
		data.create("testSequence");
		data.setValue("testSequence", 0);
		
		// dhj: new LoopGoal(FIND_LEAK) creates an empty loop goal called
		// find leak which, when performed, does nothing. Either pass in
		// a goal instance (ie findLeak()) or better, pass in a BDIGoal instance
		// as below. The BDIGoal is executed by looking up the name provided
		// in the team's default capability. It is found, as it has been added
		// via an addGoal() invocation in the constructor.
		//
		performGoal(new BDIGoal(CONFIGURE_DIAGNOSTICS), "CONFIGURE_DIAGNOSTICS", data);
		performGoal(new BDIGoal(WATCH_FOR_FAULTS), "WATCH_FOR_FAULTS", data);
		performGoal(new BDIGoal(DIAGNOSE_FAULTS), "DIAGNOSE_FAULTS", data);
		performGoal(new BDIGoal(REPORT_FAULTS), "REPORT_FAULTS", data);
		return true;
	}
	
	//
	// GOAL EXECUTION
	// ==============
	// configureDiagnostics()
	// ======================
	// This goal configures the function block application for diagnosis. If any part of
	// that process fails, the agent abandons this goal. All remaining goals are terminated
	// except the final reporting goal.
	//
	Goal configureDiagnostics() {
		say("Setting up goal configureDiagnostics().");
		return new Goal (CONFIGURE_DIAGNOSTICS) {
			//
			// execute()
			// =========
			public Goal.States execute(Data data) {
				say("Executing goal configureDiagnostics().");
				
				say(" >" + marvin.currentGoalState());
				
				if (marvin.currentGoalState() == Goal.States.EXECUTING) {
					// Let him get on with it. Ask GORITE to come back and check later. 
					sleep(1000);
					return Goal.States.STOPPED;
				} else if (marvin.currentGoalName() == UNDEFINED_GOAL) {
					// Agent is awaiting a new goal to execute. 
					// RA_BRD - trialing a new Goal State for asynchronous agent operation.
					// Start marvin configuring stuff and then leave him to it..
					marvin.currentGoalName(CONFIGURE_DIAGNOSTICS);					
					marvin.currentGoalState(Goal.States.EXECUTING);
				} else {
					// The goal has been completed or has failed.
					return marvin.currentGoalState();
				}
				return Goal.States.STOPPED;
			}
		};
	}

	//
	// watchForFaults()
	// ================
	// This goal performs the primary goal of watching the function block application and
	// recognising when a fault symptom appears.
	//
	// RA_BRD dynamic diagnostic monitor launch code for later
	// skills.runDiagnostic(applicationPath, "MyClass"); System.exit(0); 
	//
	Goal watchForFaults() {
		say("Setting up goal watchForFaults().");
		return new Goal (WATCH_FOR_FAULTS) {
			//
			// execute()
			// =========
			public Goal.States execute(Data data) {	
				say("Executing goal watchForFaults().");
				return Goal.States.PASSED;
		//		delay(500);
		//		Thread.currentThread().yield();
		//		return Goal.States.STOPPED;
			}	
		};		
	}			
				
	//
	// diagnoseFaults()
	// ================
	Goal diagnoseFault() {
		say("Setting up goal diagnoseFaults().");
		return new Goal (DIAGNOSE_FAULTS) {
			//
			// execute()
			// =========
			public Goal.States execute(Data data) {
				say("Executing goal diagnoseFaults().");
				boolean status = false;
				return Goal.States.PASSED;
			}
		};
	}

	//
	// reportFaults()
	// ==============
	Goal reportFault() {
		say("Setting up goal reportFaults()");
		return new Goal (REPORT_FAULTS) {
			//
			// execute()
			// =========
			public Goal.States execute(Data data) {
				say("Executing goal reportFaults().");
				return Goal.States.PASSED;
			}
		};
	}
	
	// 
	// pause()
	// =======
	// Outputs a console message and waits for the user to press any key
	// before continuing. 
	//
	@SuppressWarnings("unused")
	private void pause(String prompt) {
		String userInput = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		System.out.println(prompt + " press a key to continue.");
		try {
			userInput = stdIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 
	// sleep()
	// =======
	// Put this thread to sleep for a specified number of milliseconds.
	//
	private void sleep(int sleepTime) {
		try {			
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
		}
	}
	
	//
	// say()
	// =====
	// Output a console message for use during debugging. This
	// can be turned off by setting the private variable isSilent.
	//
	private void say(String whatToSay){
		if(!isSilent) {
			System.out.println(whatToSay);
		}
	}	
}
