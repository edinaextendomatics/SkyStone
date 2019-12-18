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

@Autonomous(name="ExtendoBot: Foundation", group="Autonomous")
public class ExMoveFoundationandPark extends LinearOpMode {
    /* Declare OpMode members. */
    ExtendomaticsHardware robot = new ExtendomaticsHardware(telemetry);
    private ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_INCH = 98.3606557;
    static final double COUNTS_PER_DEGREE = 100;
    static final double DRIVE_SPEED = 0.8;
    static double up   = 1;
    static double down = 0;
    static boolean finalChoices = false;
    static boolean isRed = false;
    static boolean isFoundationSide = true;
    static boolean parkCenter = true;

    public void runOpMode(){
        finalChoices = false;
        robot.init(hardwareMap, true, false, false, true, true);

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
    }

    public void execute_foundation() {
        double forwardParkCenter = parkCenter ? -3:17.5;
        double colorDirection = isRed ? -1:1; // flipped because robot is positioned backwards
        double sidePosition = isFoundationSide ? -1:1;

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
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Orientation is having the back of the bot face away from the drivers, set as close to building site as possible

        // Sequence initiated
        double redStartAdjustment= isRed ? 2 : 0;
        driveRight(DRIVE_SPEED, colorDirection*13.5+redStartAdjustment, 3.0);
        driveForward(DRIVE_SPEED, -27.5, 5.0);
        robot.foundation_hook.setPosition(down);
        sleep(2000);

        driveForward(0.3, 32.5, 4.5);
        robot.foundation_hook.setPosition(up);
        sleep(2000);
        driveRight(DRIVE_SPEED, -colorDirection*(29.5), 5.0);
        double redCenter = isRed ? -6 : 0;
        driveForward(DRIVE_SPEED, -16.5+redCenter, 5.0);
        double redPush = isRed ? colorDirection*11 : 0;
        driveRight(DRIVE_SPEED, colorDirection*6+redPush, 6.0);
        // brings grabber servo down so robot can fit under alliance bridge
        robot.grabberServo_1.setPosition(1.25);
        sleep(2000);
        // parking sequence
        if (isRed)
        {
            forwardParkCenter = forwardParkCenter+9;
        }
        driveForward(DRIVE_SPEED,forwardParkCenter, 5.0);
        driveRight(DRIVE_SPEED, sidePosition*colorDirection*22, 5.0);

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