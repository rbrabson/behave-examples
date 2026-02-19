package behave.examples.subsystems;

import behave.Status;

public class Intake {
    private static final int MAX_ARTIFACTS = 3;
    private int numOfArtifacts = 0;

    public Status intakeArtifact() {
        if (numOfArtifacts < MAX_ARTIFACTS) {
            numOfArtifacts++;
            System.out.println("Picked up an artifact!");
            return Status.SUCCESS;
        } else {
            return Status.FAILURE;
        }
    }

    public Status removeArtifact() {
        if (numOfArtifacts > 0) {
            numOfArtifacts--;
            return Status.SUCCESS;
        } else {
            return Status.FAILURE;
        }
    }

    public boolean isFull() {
        return numOfArtifacts >= MAX_ARTIFACTS;
    }

    public boolean isEmpty() {
        return numOfArtifacts == 0;
    }

    public int getArtifactCount() {
        return numOfArtifacts;
    }
}
