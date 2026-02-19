package behave.examples.subsystems;

import behave.Status;

public class Shooter {
    public Turret turret;
    public Flywheel flywheel;
    private int artifactCount = 0;

    public Shooter(Turret turret, Flywheel flywheel) {
        this.turret = turret;
        this.flywheel = flywheel;
    }

    public void rotateTo(double angle) {
        turret.rotateTo(angle);
    }

    public void spinUp(double targetSpeed) {
        flywheel.setTargetSpeed(targetSpeed);
    }

    public Status addArtifact() {
        if (artifactCount > 0) {
            return Status.FAILURE;
        } else {
            artifactCount++;
            return Status.SUCCESS;
        }
    }

    public boolean hasArtifact() {
        return artifactCount > 0;
    }

    public boolean isReadyToShoot() {
        return hasArtifact() && turret.isAtTarget() && flywheel.isAtTargetSpeed();
    }

    public Status shoot() {
        if (isReadyToShoot()) {
            System.out.println("Shooting artifact!");
            artifactCount--;
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }
}
