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
//                to work independently.
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
				currentGoalState(configureDiagnostics());				
				currentGoalName(UNDEFINED_GOAL);				
				break;
			
			case WATCH_FOR_FAULTS:
				currentGoalState(watchForFaults());
				currentGoalName(UNDEFINED_GOAL);				
				break;
				
			case DIAGNOSE_FAULTS:
				currentGoalState(diagnoseFaults());
				currentGoalName(UNDEFINED_GOAL);				
				break;				
				
			case REPORT_FAULTS:
				currentGoalState(reportFaults());
				currentGoalName(UNDEFINED_GOAL);				
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
		for (int cnt = 0; cnt < 5; cnt ++) {
			say("configuring stuff [" + cnt + "] ...\n");
			sleep(1000);
		}
		
		// Signal that the agent has completed this...or it has failed..
		//currentGoalState(Goal.States.PASSED);
		return Goal.States.PASSED;
	}
	
	// 
	//  watchForFaults()
	//  ================
	//  This goal performs the primary goal of watching the function block application and
	//  identifying when a fault symptom appears.
	// 
	//  RA_BRD dynamic diagnostic monitor launch code for later
	//  skills.runDiagnostic(applicationPath, "MyClass"); System.exit(0); 
	// 
	private Goal.States watchForFaults(){
		for (int cnt = 0; cnt < 10; cnt ++) {
			say("hunting for faults [" + cnt + "] ...\n");
			sleep(1000);
		}
			
		// Signal that the agent has completed this...or it has failed..
		currentGoalState(Goal.States.PASSED);
		return Goal.States.PASSED;
	}			
				
	// 
	//  diagnoseFaults()
	//  ================
	private Goal.States diagnoseFaults() {
		for (int cnt = 0; cnt < 10; cnt ++) {
			say("diagnosing a specific fault [" + cnt + "] ...\n");
			sleep(1000);
		}
		// Signal that the agent has completed this...or it has failed..
		//currentGoalState(Goal.States.PASSED);
		return Goal.States.PASSED;
	}

	// 
	//  reportFaults()
	//  =============
	private Goal.States reportFaults() {
		for (int cnt = 0; cnt < 2; cnt ++) {
			say("preparing diagnosis report [" + cnt + "] ...\n");
			sleep(1000);
		}
		// Signal that the agent has completed this...or it has failed..
		//currentGoalState(Goal.States.PASSED);
		return Goal.States.PASSED;
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
