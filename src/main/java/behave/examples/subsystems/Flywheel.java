package behave.examples.subsystems;

import behave.Status;

public class Flywheel {
    private double currentSpeed;
    private double targetSpeed;

    public Flywheel() {
        this.currentSpeed = 0;
    }

    public void setTargetSpeed(double speed) {
        this.targetSpeed = speed;
    }

    public void updateCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public boolean isAtTargetSpeed() {
        return this.currentSpeed == targetSpeed;
    }

    public Status spinUp() {
        if (isAtTargetSpeed()) {
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }

    public Status spinDown() {
        if (currentSpeed == 0) {
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }
}
