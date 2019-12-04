/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name="ExtendoBot: Foundation", group="Autonomous")
public class ExMoveFoundationandPark extends LinearOpMode {
    /* Declare OpMode members. */
    ExtendomaticsHardware robot = new ExtendomaticsHardware(telemetry);
    private ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_INCH = 98.3606557;
    static final double COUNTS_PER_DEGREE = 100;
    static final double DRIVE_SPEED = 0.8;
    static double forwardDriveTime = 1;
    static double sideDriveTime = 0.8;
    static double INCREMENT   = 0.02;
    static double MAX_POS     =  1.0; // Initial Position
    static double MIN_POS     =  0.0; // Closed/Hooked Position
    static boolean finalChoices = false;
    static boolean isRed = false;
    static boolean isFoundationSide = false;
    static boolean parkCenter = true;

    public void runOpMode(){

        robot.init(hardwareMap, true, false, false, true, false);

        while (!finalChoices)
        {
            telemetry.addData("bot", "we got to the while loop! yay!");
            userInput();
            idle();
        }
        String teamMessage = (isRed ? "Red " : "Blue ") + "Team selected";
        String positionMessage = (isFoundationSide ? "Foundation " : "Block ") + "Side selected";
        String parkMessage = (parkCenter ? "Center " : "Side ") + "park selected";
        telemetry.addData("Say", "Final Choices are in!");
        telemetry.addData("Say", "Hook Foundation");
        telemetry.addData("Say", teamMessage);
        telemetry.addData("Say", positionMessage);
        telemetry.addData("Say", parkMessage);
        telemetry.update();
        waitForStart();
        // executes foundation grabbing sequence
        execute_foundation();

        // executes parking procedure
        // drive to center or side!

        /*
        robot.setPowerForward(1);
        runtime.reset();
        while (runtime.seconds() < forwardDriveTime * (parkCenter ? 1:0.2)) {
            telemetry.addData("Path", "Forward Drive: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        robot.setPowerForward(0);

        // drive right or left
        double colorDirection = isRed ? -1:1; //flipped because robot is positioned backwards
        double sidePosition = isFoundationSide ? -1:1;
        robot.setPowerRight(colorDirection * sidePosition);
        runtime.reset();
        while (runtime.seconds() < sideDriveTime) {
            telemetry.addData("Path", "Side Drive: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        robot.setPowerForward(0);
        telemetry.addData("Say", "Robot stopped");
        telemetry.update();
        finalChoices = false; */
    }

    public void execute_foundation() {
        int    CYCLE_MS    =   50;
        double forwardParkCenter = parkCenter ? -1:-0.2;
        double colorDirection = isRed ? -1:1; // flipped because robot is positioned backwards
        double sidePosition = isFoundationSide ? -1:1;

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
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

        // Wait for the game to start (driver presses PLAY)
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Orientation is having the back of the bot face away from the drivers, set 2 feet away from alliance bridge marker

        driveRight(DRIVE_SPEED, colorDirection*27, 3.0);
        driveForward(DRIVE_SPEED, -28, 3.0);
        robot.foundation_hook.setPosition(0);
        sleep(2000);
        driveForward(DRIVE_SPEED, 28, 3.0);
        robot.foundation_hook.setPosition(1);
        sleep(2000);
        driveRight(DRIVE_SPEED, -colorDirection*27, 3.0);
        sleep(500);
        driveForward(DRIVE_SPEED,24*forwardParkCenter, 3.0);
        driveRight(DRIVE_SPEED, sidePosition*colorDirection*32, 3.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void userInput() {
        if (gamepad1.start) {
            parkCenter = true;
        }
        if (gamepad1.back) {
            parkCenter = false;
        }
        if (gamepad1.b) {
            isRed = true;
        }
        if (gamepad1.x) {
            isRed = false;
        }
        if (gamepad1.a) {
            isFoundationSide = false;
        }
        if (gamepad1.y) {
            isFoundationSide = true;
        }
        if (gamepad1.right_trigger > 0 && gamepad1.left_trigger > 0)
        {
            finalChoices = true;
        }
        String teamMessage = (isRed ? "Red " : "Blue ") + "Team selected";
        String positionMessage = (isFoundationSide ? "Foundation " : "Block ") + "Side selected";
        String parkMessage = (parkCenter ? "Center " : "Side ") + "park selected";
        telemetry.addData("Say", teamMessage+ " Press X for Blue Team, B for Red Team");
        telemetry.addData("Say", positionMessage+" Press Y for Foundation Side, A for Blocks Side");
        telemetry.addData("Say", parkMessage+"Press Start for center parking, press back for side parking");
        telemetry.addData("Say", "Final Choices: "+finalChoices+" Press Right Trigger and Left Trigger to finalize your choices");
        telemetry.update();

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