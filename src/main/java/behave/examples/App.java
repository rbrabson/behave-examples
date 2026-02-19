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

public class App {
    public static void main(String[] args) {
        DriveTrain driveTrain = new DriveTrain();
        Intake intake = new Intake();
        Shooter shooter = new Shooter(new Turret(), new Flywheel());

        Node driveToTarget = new Action(driveTrain::moveToTarget);
        Node scoreArtifact = new Action(shooter::shoot);
        Node pickUpArtifact = new Action(intake::intakeArtifact);
        Node readyToShoot = new Condition(shooter::isReadyToShoot);

        Node pickupThreeArtifacts = new RepeatN(pickUpArtifact, 3);
        Node prepareToShoot = new WhileFailure(readyToShoot);
        Node prepareToScore = new Parallel(Arrays.asList(driveToTarget, prepareToShoot));

        // Action node to transfer artifact from Intake to Shooter
        Node transferArtifact = new Action(() -> {
            // Remove artifact from intake, then add to shooter
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

        // Set target position
        driveTrain.driveTo(5, 5, 0);
        shooter.rotateTo(45);
        shooter.spinUp(3000);

        BehaviorTree bt = new BehaviorTree(scoreSequence);
        System.out.println("Preparing to start, starting starting status: " + bt.status());
        while (bt.tick() == Status.RUNNING) {
            // Simulate sensor updates
            driveTrain.updateCurrentPose(new Pose(5, 5, 0));
            shooter.turret.updateCurrentAngle(45);
            shooter.flywheel.updateCurrentSpeed(3000);
        }

        System.out.println("Scoring complete! Final status: " + bt.status());
    }
}
