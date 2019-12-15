package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="ExtendoBot: Turn Test", group="Autonomous")
public class ExTurnTest extends LinearOpMode {
    ExtendomaticsHardware robot = new ExtendomaticsHardware(telemetry);
    private ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_INCH = 98.3606557;
    static final double COUNTS_PER_DEGREE = -19;
    static final double TURN_SPEED = 0.65;

    public void runOpMode() {

        robot.init(hardwareMap, true, false, false, false, true);
        waitForStart();
        turnRight(TURN_SPEED,90,4);
    }

    public void driveForward(double speed,
                             double inches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;

        // Ensure that the opmode is still active
        // Determine new target position, and pass to motor controller
        newLeftFrontTarget = robot.leftFrontDrive.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
        newRightFrontTarget = robot.rightFrontDrive.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);
        newLeftRearTarget = robot.leftRearDrive.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
        newRightRearTarget = robot.rightRearDrive.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);

        robot.leftFrontDrive.setTargetPosition(newLeftFrontTarget);
        robot.rightFrontDrive.setTargetPosition(newRightFrontTarget);
        robot.leftRearDrive.setTargetPosition(newLeftRearTarget);
        robot.rightRearDrive.setTargetPosition(newRightRearTarget);

        // Turn On RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // reset the timeout time and start motion.
        runtime.reset();
        robot.leftFrontDrive.setPower(Math.abs(speed));
        robot.rightFrontDrive.setPower(Math.abs(speed));
        robot.leftRearDrive.setPower(Math.abs(speed));
        robot.rightRearDrive.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits its target position, the motion will stop.  This is "safer" in the event that the robot will always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues onto the next step, use (isBusy() || isBusy()) in the loop test.
        while ((runtime.seconds() < timeoutS) &&
                (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy() && robot.leftRearDrive.isBusy() && robot.rightRearDrive.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget, newLeftRearTarget, newRightRearTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.leftFrontDrive.getCurrentPosition(),
                    robot.rightFrontDrive.getCurrentPosition(),
                    robot.leftRearDrive.getCurrentPosition(),
                    robot.rightRearDrive.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftFrontDrive.setPower(0);
        robot.rightFrontDrive.setPower(0);
        robot.leftRearDrive.setPower(0);
        robot.rightRearDrive.setPower(0);
        // Turn off RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveRight(double speed,
                           double inches,
                           double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;

        // Ensure that the opmode is still active
        // Determine new target position, and pass to motor controller
        newLeftFrontTarget = robot.leftFrontDrive.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
        newRightFrontTarget = robot.rightFrontDrive.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
        newLeftRearTarget = robot.leftRearDrive.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);
        newRightRearTarget = robot.rightRearDrive.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);

        robot.leftFrontDrive.setTargetPosition(newLeftFrontTarget);
        robot.rightFrontDrive.setTargetPosition(newRightFrontTarget);
        robot.leftRearDrive.setTargetPosition(newLeftRearTarget);
        robot.rightRearDrive.setTargetPosition(newRightRearTarget);

        // Turn On RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // reset the timeout time and start motion.
        runtime.reset();
        robot.leftFrontDrive.setPower(Math.abs(speed));
        robot.rightFrontDrive.setPower(Math.abs(speed));
        robot.leftRearDrive.setPower(Math.abs(speed));
        robot.rightRearDrive.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits its target position, the motion will stop.  This is "safer" in the event that the robot will always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues onto the next step, use (isBusy() || isBusy()) in the loop test.

        while ((runtime.seconds() < timeoutS) &&
                (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy() && robot.leftRearDrive.isBusy() && robot.rightRearDrive.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget, newLeftRearTarget, newRightRearTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.leftFrontDrive.getCurrentPosition(),
                    robot.rightFrontDrive.getCurrentPosition(),
                    robot.leftRearDrive.getCurrentPosition(),
                    robot.rightRearDrive.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftFrontDrive.setPower(0);
        robot.rightFrontDrive.setPower(0);
        robot.leftRearDrive.setPower(0);
        robot.rightRearDrive.setPower(0);
        // Turn off RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }

    public void turnRight(double speed,
                          double degrees,
                          double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;

        // Ensure that the opmode is still active
        // Determine new target position, and pass to motor controller
        newLeftFrontTarget = robot.leftFrontDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_DEGREE);
        newRightFrontTarget = robot.rightFrontDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_DEGREE);
        newLeftRearTarget = robot.leftRearDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_DEGREE);
        newRightRearTarget = robot.rightRearDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_DEGREE);

        robot.leftFrontDrive.setTargetPosition(newLeftFrontTarget);
        robot.rightFrontDrive.setTargetPosition(newRightFrontTarget);
        robot.leftRearDrive.setTargetPosition(newLeftRearTarget);
        robot.rightRearDrive.setTargetPosition(newRightRearTarget);

        // Turn On RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // reset the timeout time and start motion.
        runtime.reset();
        robot.leftFrontDrive.setPower(Math.abs(speed));
        robot.rightFrontDrive.setPower(Math.abs(speed));
        robot.leftRearDrive.setPower(Math.abs(speed));
        robot.rightRearDrive.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while ((runtime.seconds() < timeoutS) &&
                (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy() && robot.leftRearDrive.isBusy() && robot.rightRearDrive.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget, newLeftRearTarget, newRightRearTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.leftFrontDrive.getCurrentPosition(),
                    robot.rightFrontDrive.getCurrentPosition(),
                    robot.leftRearDrive.getCurrentPosition(),
                    robot.rightRearDrive.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftFrontDrive.setPower(0);
        robot.rightFrontDrive.setPower(0);
        robot.leftRearDrive.setPower(0);
        robot.rightRearDrive.setPower(0);
        // Turn off RUN_TO_POSITION
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }
}
