//
// THE DAM SCENARIO
// ================
// This is an example of a Multi-Agent system that is described in the 
// documentation for "The Dam Project". It implements a team of agents
// who manage leaks in a dam wall.
//
// This is an extension of the original version two which implements a
// better example after Dennis' feedback. The original was based on the 
// GORITE SpaceShip demonstration. 
//
// The application is used primarily as a test platform for trying out
// agent concepts that are used in the AUT Fault Diagnostic Engine. It
// is where Marvin goes to play...
//
// AUT University
//
// Revision History
// ~~~~~~~~~~~~~~~~
// 10.12.2017 BRD Original version
// 29.01.2019 BRD Brought up-to-date with Dennis Jarvis's latest recommendations. 
// 22.08.2020 BRD Imported many of the core modules from the Fault Diagnostic
//                Engine to make this a more realistic test platform.

package TheDamScenario;

public class DamMain {	
	public static String appVersion = "2.3";
    private static boolean isSilent = false;

    //
    // main()
    // ======
    public static void main(String[] args) throws Throwable {
    	say("\nTHE DAM SCENARIO version " + appVersion + "\n");
   		
    	// Start building our teams in GORITE
    	
    	DiagnosticTeam diagnosticTeam = new DiagnosticTeam("diagnosticTeam");

    	// Start the team of agents hunting for and diagnosing faults.
    	diagnosticTeam.findFaults();
    	say("\nExiting The Dam Scenario.");
    	System.exit(0); 
    }
		
    //
    // say()
    // =====
    // Output a console message for use during debugging. This
    // can be turned off by setting the private variable silence
    //
    private static void say(String whatToSay){
    	if(!isSilent) {
    		System.out.println(whatToSay);
    	}
    }	
}	
