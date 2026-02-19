package behave.examples.subsystems;

import behave.Status;

public class Turret {
    private double currentAngle;
    private double targetAngle;

    public Turret() {
        this.currentAngle = 0;
    }

    public void rotateTo(double angle) {
        this.targetAngle = angle;
    }

    public void updateCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public boolean isAtTarget() {
        boolean atTarget = this.currentAngle == targetAngle;
        if (atTarget) {
            System.out.println("Turret at target angle!");
        }
        return atTarget;
    }

    public Status moveToTarget() {
        if (isAtTarget()) {
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }
}
