
package behave.examples.subsystems;

import behave.Status;

public class DriveTrain {
    private Pose currentPose;
    private Pose targetPose;

    public DriveTrain() {
        this.currentPose = new Pose(0, 0, 0);
    }

    public void driveTo(double x, double y, double theta) {
        driveTo(new Pose(x, y, theta));
    }

    public void driveTo(Pose targetPose) {
        this.targetPose = targetPose;
    }

    public void updateCurrentPose(Pose currentPose) {
        this.currentPose = currentPose;
    }

    public boolean isAtTarget() {
        return this.currentPose.getX() == targetPose.getX() && this.currentPose.getY() == targetPose.getY()
                && this.currentPose.getTheta() == targetPose.getTheta();
    }

    public Status atTarget() {
        if (isAtTarget()) {
            return Status.SUCCESS;
        } else {
            return Status.FAILURE;
        }
    }

    public Status moveToTarget() {
        if (currentPose == null) {
            return Status.FAILURE;
        } else if (isAtTarget()) {
            System.out.println("At target position!");
            return Status.SUCCESS;
        } else {
            return Status.RUNNING;
        }
    }
}
