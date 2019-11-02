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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot:Extendomatic TeleOp", group="Pushbot")
public class ExtendomaticsBot_TeleOp extends OpMode{

    /* Declare OpMode members. */
     ExtendomaticsHardware robot       = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */
    static final double INCREMENT   = 0.02;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    static final double COUNTS_PER_MOTOR_REV_CORE_HEX    = 288 ;    // Rev core hex motor counts per rev
    static final double DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV_CORE_HEX * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679);
    static final double LIFT_SPEED = 0.6;
    static final int Closed_Position = 0;
    static final int Open_Position = 600;
    static final int Grabbing_Position = 400;
    static final double Grabber_Power = 1;
    static final double LIFT_MAX_EXTENSION_LIMIT = 10000;
    static final boolean isDriveEnabled = true;
    static final boolean isLiftEnabled = true;
    static final boolean isGrabberEnabled = true;

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap, isDriveEnabled, isGrabberEnabled, isLiftEnabled);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello, Good Luck!");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop( ) {
        // define motor class variables
        double Y;
        double X;
        double Z;

        // LIFT MOTOR controls section
        double liftInput = gamepad2.right_stick_y;
        // do not allow movement beyond limits
        if(isLiftEnabled) {
            if ((robot.lift.getCurrentPosition() > LIFT_MAX_EXTENSION_LIMIT && liftInput > 0) ||
                    (robot.lift.getCurrentPosition() <= 0 && liftInput < 0)
            ) {
                robot.lift.setPower(0);
                telemetry.addData("You have reached the max or min position", "%.2f");
            } else {
                robot.lift.setPower(liftInput * LIFT_SPEED);
            }

            telemetry.addData("lift input value (Gamepad 2 right stick)",
                    "%.2f",
                    gamepad2.right_stick_y);
            telemetry.addData("lift encoder value",
                    "%.2f",
                    robot.lift.getCurrentPosition());
            telemetry.update();
        }
        // DRIVE MOTOR contols section
        // collect user input from left and right gamepad controls and set internal variable X & Y
        Y = -gamepad1.left_stick_y;
        X = gamepad1.left_stick_x;
        Z = gamepad1.right_stick_x;

        // use X, Y, & Z to set power for each of the motors
        if(isDriveEnabled) {
            robot.leftFrontDrive.setPower(Range.clip(Y + X + Z, -1, 1));
            robot.rightFrontDrive.setPower(Range.clip(X - Y + Z, -1, 1));
            robot.leftRearDrive.setPower(Range.clip(Y - X + Z, -1, 1));
            robot.rightRearDrive.setPower(Range.clip(-X - Y + Z, -1, 1));

            // Send telemetry message to signify robot running;
            telemetry.addData("leftpad Y", "%.2f", Y);
            telemetry.addData("leftpad X", "%.2f", X);
            telemetry.addData("rightpad Z", "%.2f", Z);
        }
        // GRABBER controls section
        if(isGrabberEnabled) {
            telemetry.addData("grabber right trigger", "%.2f", gamepad2.right_trigger);
            telemetry.addData("grabber left trigger", "%.2f", gamepad2.left_trigger);

            if (robot.grabber.getCurrentPosition() == robot.grabber.getTargetPosition()) {
                robot.grabber.setPower(0);
            } else {
                telemetry.addData("Path1", "Not yet at your target");
            }
            // GRABBER controls section
            if (!robot.grabber.isBusy()) {
                boolean isButtonPressed = false;

                // x is open.
                if (gamepad2.x) {
                    robot.grabber.setTargetPosition(Open_Position);
                    isButtonPressed = true;
                }
                // y is grab.
                else if (gamepad2.y) {
                    robot.grabber.setTargetPosition(Grabbing_Position);
                    isButtonPressed = true;
                }
                // b is closed.
                else if (gamepad2.b) {
                    robot.grabber.setTargetPosition(Closed_Position);
                    isButtonPressed = true;
                }
                if (isButtonPressed) {
                    robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.grabber.setPower(Grabber_Power);
                }

            } else {
                // Display it for the driver.);
                telemetry.addData("Path2", "Running at %7d, moving towards %7d", robot.grabber.getCurrentPosition(), robot.grabber.getTargetPosition());
                telemetry.update();
            }
        }
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
