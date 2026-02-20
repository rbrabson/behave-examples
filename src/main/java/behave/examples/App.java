package behave.examples;

import java.util.Arrays;

// Behavior Tree imports
import behave.Action;
import behave.AlwaysSuccess;
import behave.BehaviorTree;
import behave.Condition;
import behave.Node;
import behave.Parallel;
import behave.RepeatN;
import behave.Sequence;
import behave.Status;
import behave.WhileFailure;
import behave.WhileSuccess;

// Simulated subsystem imports
import behave.examples.subsystems.DriveTrain;
import behave.examples.subsystems.Flywheel;
import behave.examples.subsystems.Intake;
import behave.examples.subsystems.Pose;
import behave.examples.subsystems.Shooter;
import behave.examples.subsystems.Turret;

// Simulated mediator import
import behave.mediators.ScoreMediator;

// Example of a robot application using a behavior tree to coordinate subsystems for a scoring task.
public class App {

    private DriveTrain driveTrain;
    private Intake intake;
    private Shooter shooter;
    private ScoreMediator scoreMediator;

    // In a real application, subsystems would be initialized with hardware
    // interfaces and configuration. For this example, we are simulating their
    // behavior with simple method stubs.
    public App() {
        driveTrain = new DriveTrain();
        intake = new Intake();
        shooter = new Shooter(new Turret(), new Flywheel());
        scoreMediator = new ScoreMediator(intake, shooter);
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
        Node transferArtifact = new Action(scoreMediator::transferArtifact);

        // Score all artifacts. This transfers artifacts from the intake to the shooter
        // and shoots them until there are no more artifacts to shoot. This uses the
        // AlwaysSuccess decorator to ensure that the behavior continues even if there
        // are no artifacts left to shoot, and the WhileSuccess decorator to keep trying
        // to score as long as there are artifacts in the intake.
        Node scoreAllArtifacts = new AlwaysSuccess(
                new WhileSuccess(new Sequence(Arrays.asList(transferArtifact, scoreArtifact))));

        // The main sequence of the behavior tree. It first picks up three artifacts,
        // then prepares to shoot, and finally scores all artifacts.
        Node scoreSequence = new Sequence(Arrays.asList(pickupThreeArtifacts, prepareToScore, scoreAllArtifacts));

        // Set target position for the subsystems to achieve before starting the
        // behavior tree. In a real application, these would be based on sensor inputs
        // or a predefined strategy.
        driveTrain.driveTo(5, 5, 0);
        shooter.rotateTo(45);
        shooter.spinUp(3000);

        BehaviorTree bt = new BehaviorTree(scoreSequence);
        System.out.println("Preparing to start, starting starting status: " + bt.status() + "\n");
        boolean movedToTarget = false;
        while (bt.tick() == Status.RUNNING) {
            // Simulate sensor updates. In a real application, these would come from
            // hardware sensors and would likely be handled in a separate thread or through
            // event listeners.
            if (!movedToTarget && pickupThreeArtifacts.status() != Status.RUNNING) {
                driveTrain.updateCurrentPose(new Pose(5, 5, 0));
                shooter.turret.updateCurrentAngle(45);
                shooter.flywheel.updateCurrentSpeed(3000);
                movedToTarget = true;
            }
        }
        System.out.println("\nScoring complete! Final status: " + bt.status());
    }

    // Main method to run the application
    public static void main(String[] args) {
        App app = new App();
        app.runBehaviorTree();
    }
}
