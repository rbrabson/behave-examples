package behave.examples.mediators;

import behave.examples.subsystems.Shooter;
import behave.Status;
import behave.examples.subsystems.Intake;

public class ScoreMediator {
    private final Intake intake;
    private final Shooter shooter;

    public ScoreMediator(Intake intake, Shooter shooter) {
        this.intake = intake;
        this.shooter = shooter;
    }

    public Status transferArtifact() {
        if (!intake.isEmpty()) {
            if (intake.removeArtifact() == behave.Status.SUCCESS && shooter.addArtifact() == behave.Status.SUCCESS) {
                System.out.println("Transferred artifact from intake to shooter.");
                return behave.Status.SUCCESS;
            }
        }
        return behave.Status.FAILURE;
    }
}
