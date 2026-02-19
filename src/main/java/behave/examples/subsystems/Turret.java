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
        System.out.println("Moved turret to the target angle!");
    }

    public boolean isAtTarget() {
        return this.currentAngle == targetAngle;
    }

    public Status moveToTarget() {
        if (isAtTarget()) {
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }
}
