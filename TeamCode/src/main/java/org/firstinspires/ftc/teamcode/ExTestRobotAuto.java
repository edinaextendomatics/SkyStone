package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autonomous Robot Test", group="Holonomic bot")
public class ExTestRobotAuto extends OpMode {

        /* Declare OpMode members. */
        ExtendomaticsHardware robot       = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_INCH = 200;
    static final double COUNTS_PER_DEGREE = 100;
    static final int LIFT_TOP_POSITION = 800;
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static final int OPEN_POSITION = 600;
    static final int GRABBING_POSITION = 400;
    int TestNumber;
    @Override
    public void init() {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d :%7d :%7d",
                robot.leftFrontDrive.getCurrentPosition(),
                robot.leftRearDrive.getCurrentPosition(),
                robot.rightFrontDrive.getCurrentPosition(),
                robot.rightRearDrive.getCurrentPosition());

        telemetry.update();
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Say", "Test 1 - Drive Forward 24 inches - Press X");
        telemetry.addData("Say", "Test 2 - Drive Right 24 inches - Press Y");
        telemetry.addData("Say", "Test 3 - Turn Right 90 degrees - Press A");
        TestNumber = 1;

    }

    @Override
    public void init_loop() {

        if (gamepad1.x) {
            //TestNumber = ?????
            telemetry.addData("Say", "Test 1 Selected");
        }
        else if (gamepad1.a) {
            //TestNumber = ?????
        }
        else if (gamepad1.y) {
            //TestNumber = ?????
        }
        telemetry.addData("Say", "Test 1 - Drive Forward 24 inches - Press X");
        telemetry.addData("Say", "Test 2 - Drive Right 24 inches - Press Y");
        telemetry.addData("Say", "Test 3 - Turn Right 90 degrees - Press A");
    }

    @Override
    public void loop() {
        super.internalPostInitLoop();
    }

    public void driveForward(double speed,
                             double inches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;


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
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (
                (runtime.seconds() < timeoutS) &&
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
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeoutS) ||
                    (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy() && robot.leftRearDrive.isBusy() && robot.rightRearDrive.isBusy()))
        {

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

    public void turnRight(double speed,
                          double degrees,
                          double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;



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
            while (
                    (runtime.seconds() < timeoutS) &&
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


        public void lowerLift(double timeout) {
        // Ensure that the opmode is still active
        {

            robot.lift.setTargetPosition(0);
            // Turn On RUN_TO_POSITION
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.lift.setPower(0.7);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeout) &&
                            (robot.lift.isBusy()))
                // Display it for the driver.

                telemetry.addData("Path2", "lift position: %7d target position: %7d",
                        robot.lift.getCurrentPosition(),
                        0);
            telemetry.update();
        }

        // Stop all motion;
        robot.lift.setPower(0);


        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void raiseLift(double timeout) {
        // Ensure that the opmode is still active
        {

            robot.lift.setTargetPosition(LIFT_TOP_POSITION);
            // Turn On RUN_TO_POSITION
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.lift.setPower(0.7);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeout) &&
                    (robot.lift.isBusy()))
                // Display it for the driver.

                telemetry.addData("Path2", "lift position: %7d target position: %7d",
                        robot.lift.getCurrentPosition(),
                        LIFT_TOP_POSITION);
            telemetry.update();
        }

        // Stop all motion;
        robot.lift.setPower(0);


        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }

    public void grabBlock(double timeout) {
        // Ensure that the opmode is still active
        {

            robot.grabber.setTargetPosition(GRABBING_POSITION);
            // Turn On RUN_TO_POSITION
            robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.grabber.setPower(0.7);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeout) &&
                    (robot.grabber.isBusy()))
                // Display it for the driver.

                telemetry.addData("Path2", "grabber position: %7d target position: %7d",
                        robot.grabber.getCurrentPosition(),
                        GRABBING_POSITION);
            telemetry.update();
        }

        // Stop all motion;
        robot.grabber.setPower(0);


        robot.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }

    public void openGrabber(double timeout) {
        // Ensure that the opmode is still active
        {

            robot.grabber.setTargetPosition(OPEN_POSITION);
            // Turn On RUN_TO_POSITION
            robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.grabber.setPower(0.7);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeout) &&
                    (robot.grabber.isBusy()))
                // Display it for the driver.

                telemetry.addData("Path2", "grabber position: %7d target position: %7d",
                        robot.grabber.getCurrentPosition(),
                        OPEN_POSITION);
            telemetry.update();
        }

        // Stop all motion;
        robot.grabber.setPower(0);


        robot.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move

    }

    public void closeGrabber(double timeout) {
        // Ensure that the opmode is still active
        {

            robot.grabber.setTargetPosition(0);
            // Turn On RUN_TO_POSITION
            robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.grabber.setPower(0.7);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (
                    (runtime.seconds() < timeout) &&
                    (robot.grabber.isBusy()))
                // Display it for the driver.

                telemetry.addData("Path2", "grabber position: %7d target position: %7d",
                        robot.grabber.getCurrentPosition(),
                        0);
            telemetry.update();
        }

        // Stop all motion;
        robot.grabber.setPower(0);


        robot.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move


    }
}

