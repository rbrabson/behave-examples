package behave.examples;

import java.util.Arrays;

// Behavior Tree imports
import behave.Action;
import behave.BehaviorTree;
import behave.Condition;
import behave.Node;
import behave.Parallel;
import behave.RepeatN;
import behave.Sequence;
import behave.Status;
import behave.WhileFailure;

// Simulated subsystem imports
import behave.examples.subsystems.DriveTrain;
import behave.examples.subsystems.Flywheel;
import behave.examples.subsystems.Intake;
import behave.examples.subsystems.Pose;
import behave.examples.subsystems.Shooter;
import behave.examples.subsystems.Turret;

// Example of a robot application using a behavior tree to coordinate subsystems for a scoring task.
public class App {

    private DriveTrain driveTrain;
    private Intake intake;
    private Shooter shooter;

    // In a real application, subsystems would be initialized with hardware
    // interfaces and configuration. For this example, we are simulating their
    // behavior with simple method stubs.
    public App() {
        driveTrain = new DriveTrain();
        intake = new Intake();
        shooter = new Shooter(new Turret(), new Flywheel());
    }

    // This method defines and runs the behavior tree for the scoring task.
    public void runBehaviorTree() {
        Node driveToTarget = new Action(driveTrain::moveToTarget);
        Node scoreArtifact = new Action(shooter::shoot);
        Node pickUpArtifact = new Action(intake::intakeArtifact);
        Node readyToShoot = new Condition(shooter::isReadyToShoot);

        Node pickupThreeArtifacts = new RepeatN(pickUpArtifact, 3);
        Node prepareToShoot = new WhileFailure(readyToShoot);
        Node prepareToScore = new Parallel(Arrays.asList(driveToTarget, prepareToShoot));

        // Action node to transfer artifact from Intake to Shooter. This can be defined
        // in-line since it's a simple sequence of operations, or could be extracted
        // into its own method for clarity.
        Node transferArtifact = new Action(() -> {
            if (!intake.isEmpty()) {
                Status removed = intake.removeArtifact();
                if (removed == Status.SUCCESS) {
                    Status added = shooter.addArtifact();
                    if (added == Status.SUCCESS) {
                        return Status.SUCCESS;
                    }
                }
            }
            return Status.FAILURE;
        });

        Node scoreSequence = new Sequence(
                Arrays.asList(pickupThreeArtifacts, prepareToScore, transferArtifact, scoreArtifact));

        // Set target position for the subsystems to achieve before starting the
        // behavior tree. In a real application, these would be based on sensor inputs
        // or a predefined strategy.
        driveTrain.driveTo(5, 5, 0);
        shooter.rotateTo(45);
        shooter.spinUp(3000);

        BehaviorTree bt = new BehaviorTree(scoreSequence);
        System.out.println("Preparing to start, starting starting status: " + bt.status());
        while (bt.tick() == Status.RUNNING) {
            // Simulate sensor updates. In a real application, these would come from
            // hardware sensors and would likely be handled in a separate thread or through
            // event listeners.
            driveTrain.updateCurrentPose(new Pose(5, 5, 0));
            shooter.turret.updateCurrentAngle(45);
            shooter.flywheel.updateCurrentSpeed(3000);
        }
        System.out.println("Scoring complete! Final status: " + bt.status());
    }

    // Main method to run the application
    public static void main(String[] args) {
        App app = new App();
        app.runBehaviorTree();
    }
}
