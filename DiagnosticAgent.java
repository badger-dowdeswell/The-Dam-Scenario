// 
// DIAGNOSTIC AGENT
// ================
// Implements the DiagnosticAgent that operates under the instruction of the Diagnostic
// Team. It receives instructions to configure a function block application for diagnosis,
// watches for problems and diagnoses them.
// 
// AUT University - 2017-2020
// 
// Revision History
// ================
// 23.05.2019 BRD Original version based on the agents from the Dam Scenario and comments
//                from Dennis Jarvis.
// 01.09.2020 BRD Create pseudo goal execute() methods in this class to allow this agent
//                work independently.
// 
// Documentation
// =============
// The diagnostic agent is analogous to the Cell class that extends the Team class from Chapter
// Five of the GORITE book. The agent extends the GORITE Performer class,
// 
// The execute() method of a Goal definition is key to an agent's ability to perform a the tasks
// required to achieve that goal. It is where the real work for a goal gets done. 
// 
// When GORITE calls this method, it performs steps to try and fulfill the goal and then exits
// with a Goal state. This is used to determine if the goal has been completed or should be
// re-tried later. If it needs to be retried, GORITE will make sure it is re-scheduled.
// 
package TheDamScenario;

import com.intendico.gorite.*;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.IOException;

import TheDamScenario.ExitCodes;
import static TheDamScenario.Constants.*;

//public class DiagnosticAgent extends Performer implements Runnable {
public class DiagnosticAgent implements Runnable {
	//  Used to turn off and on console messages during development.
	private boolean isSilent = false;
	private int cycleCount = 0;
	
	private String currentGoalName = UNDEFINED_GOAL;
	private Goal.States currentGoalState = Goal.States.STOPPED;
	
	private String agentName = "";

	//  Setup the application information. <RA_BRD - this will need to come
	//  from higher up the chain of command later...
	private String homeDirectory = System.getProperty("user.home");
	
	//  RA_BRD - should this information come from the Team?
	String applicationPath = homeDirectory + "/Development/4diac/HVAC/";
	private String applicationName = "HVAC";

//	Action diagnose = new Action(DiagnosticTeam.FIND_FAULTS) {
//		public Goal.States execute(
//			boolean reentry, Data.Element[] ins, Data.Element[] outs) {
//			return Goal.States.PASSED;
//		}
//	};

	// 
	//  run()
	//  =====
	@Override
	public void run() {
		int sleepTime = 0;
		
		while (true) {
			cycleCount++;
			say(agentName + " " + currentGoalName + " [" + cycleCount + "]");
			
			switch (currentGoalName) {
			case CONFIGURE_DIAGNOSTICS:
				configureDiagnostics();
				currentGoalState(Goal.States.PASSED);
				break;
			
			case WATCH_FOR_FAULTS:
				break;
				
			case DIAGNOSE_FAULTS:
				break;
				
			case REPORT_FAULTS:
				break;
				
			case UNDEFINED_GOAL:
				// The agent is idling with no assigned goal at the moment, so sleep ..
				sleep(1000);
				break;
			}
		}
	}
	
	public DiagnosticAgent(String agentName) {
		//super(agentName);
		this.agentName = agentName;
		say("Created DiagnosticAgent " + agentName);
		
		//  Create and add team goals to this agent. The method create() takes as its
		//  arguments the names of the elements in the data context that
		//  are to be used as the inputs and outputs for the goal execution.
		//  Execution will not proceed until all inputs have been assigned values.
		// 
	//	addGoal(diagnose.create(new String[] {DiagnosticTeam.FIND_FAULTS}, null));
	//	say("Assigned role FIND_FAULTS to " + this.agentName + "\n");
	}

	// 
	//  GOAL EXECUTION
	//  ==============
	//  configureDiagnostics()
	//  ======================
	//  This goal configures the function block application for diagnosis. If any part of
	//  that process fails, the agent abandons this goal. All remaining goals are terminated
	//  except the final reporting goal.
	// 
	private Goal.States configureDiagnostics() {
		//say(agentName + " " + currentGoalName());
		for (int cnt = 0; cnt < 5; cnt ++) {
			say("configuring stuff [" + cnt + "] ...\n");
			sleep(1000);
		}
		
		// Signal that marvin has completed this...
		currentGoalState(Goal.States.PASSED);
		return Goal.States.PASSED;
	}
	

	
	
	//	say("Setting up goal configureDiagnostics()");
	//	return new Goal (CONFIGURE_DIAGNOSTICS) {
	//		// 
	///			//  execute()
	//		//  =========
	//		public Goal.States execute(Data data) {
	//					
	//				//return Goal.States.PASSED;
	//				return Goal.States.FAILED;
	//		}
	//	};
	//}

	// 
	//  identifyFault()
	//  ===============
	//  This goal performs the primary goal of watching the function block application and
	//  identifying when a fault symptom appears.
	// 
	//  RA_BRD dynamic diagnostic monitor launch code for later
	//  skills.runDiagnostic(applicationPath, "MyClass"); System.exit(0); 
	// 
	Goal identifyFault() {
		say("Setting up goal identifyFault()");
		return new Goal (WATCH_FOR_FAULTS) {
			// 
			//  execute()
			//  =========
			@SuppressWarnings("static-access")
			public Goal.States execute(Data data) {	
				
				say("\n" + agentName + ": ready in identifyFault()\n");
				return Goal.States.PASSED;
				//} else {
				//	delay(500);
				//	Thread.currentThread().yield();
				//	return Goal.States.STOPPED;
				//}	
			}	
		};		
	}			
				
	// 
	//  diagnoseFault()
	//  ===============
	Goal diagnoseFault() {
		say("Setting up goal diagnoseFault()");
		return new Goal (DIAGNOSE_FAULTS) {
			// 
			//  execute()
			//  =========
			public Goal.States execute(Data data) {
				boolean status = false;
				say("\n" + agentName + ": ready in diagnoseFault()\n");
				
				return Goal.States.PASSED;
			}
		};
	}

	// 
	//  reportFault()
	//  =============
	Goal reportFault() {
		say("Setting up goal reportFault()");
		return new Goal (REPORT_FAULTS) {
			// 
			//  execute()
			//  =========
			public Goal.States execute(Data data) {
				say("\n" + agentName + ": ready in reportFault()\n");
				
				//  Present a diagnosis

				return Goal.States.PASSED;
			}
		};
	}
	
	//
	// get currentGoalName()
	// =====================
	public String currentGoalName() {
		return this.currentGoalName;
	}
	
	// set currentGoalName()
	// =====================
	public void currentGoalName(String goalName) {
		this.currentGoalName = goalName;
	}
	
	//
	// get currentGoalState()
	// ======================
	public Goal.States currentGoalState() {
		return this.currentGoalState;
	}
	
	// set currentGoalState()
	// ======================
	public void currentGoalState(Goal.States goalState) {
		this.currentGoalState = goalState;
	}
	
	// 
	//  say()
	//  =====
	//  Output a console message for use during debugging. This
	//  can be turned off by setting the private variable isSilent
	//  true.
	// 
	private void say(String whatToSay){
		if(!isSilent) {
			System.out.println(whatToSay);
		}
	}

	// 
	// pause()
	// =======
	@SuppressWarnings("unused")
	private void pause(String prompt) {
		String userInput = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		System.out.println(prompt);
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
	@SuppressWarnings("unused")
	private void sleep(int sleepTime) {
		try {			
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
		}
	}
	
	// 
	// delay()
	// =======
	@SuppressWarnings("unused")
	private void delay(int milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep((long) milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
