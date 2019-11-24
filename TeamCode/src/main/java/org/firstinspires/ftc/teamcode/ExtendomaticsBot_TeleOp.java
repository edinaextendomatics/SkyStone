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

    static final double LIFT_SPEED = 1;
    static final int Open_Position = 7600;
    static final double Grabber_Power = 1;
    static final double LIFT_MAX_EXTENSION_LIMIT = 460;
    static final double LIFT_DOWN_POWER_FACTOR = 0.125;
    static double INCREMENT   = 0.02;
    static double MAX_POS     =  1.0; // Initial Position
    static double MIN_POS     =  0.0; // Closed/Hooked Position

    static final boolean isDriveEnabled = true;
    static final boolean isLiftEnabled = true;
    static final boolean isGrabberEnabled = true;
    static final boolean isFoundationHookEnabled = false;

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(super.hardwareMap, isDriveEnabled, isGrabberEnabled, isLiftEnabled, isFoundationHookEnabled);
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
        RunLift();
        RunDrive();
        RunGrabber();
        RunFoundationHook();

        telemetry.update();
    }

    private void RunFoundationHook() {
        double position = MAX_POS;
        if(gamepad2.dpad_down) {
            position -= INCREMENT;
        }
        else if(gamepad2.dpad_up) {
            position += INCREMENT;
        }
        position = Range.clip(position, MIN_POS, MAX_POS);
        robot.foundation_hook.setPosition(position);

        telemetry.addData("foundation hook position : ", "%.2f", position);
    }

    private void RunLift() {
        double liftInput = -gamepad2.left_stick_y;
        if(isLiftEnabled) {
            // do not allow movement beyond limits
            if ((robot.liftleft.getCurrentPosition() < -LIFT_MAX_EXTENSION_LIMIT
                || robot.liftright.getCurrentPosition() > LIFT_MAX_EXTENSION_LIMIT)
                && liftInput > 0) {
                telemetry.addData("Lift", "You have reached the Max position, please stop moving");
                robot.liftleft.setPower(0);
                robot.liftright.setPower(0);
            }
            else if ((robot.liftleft.getCurrentPosition() > 0 || robot.liftright.getCurrentPosition() < 0) && liftInput < 0) {
                telemetry.addData("Lift", "You have reached the Minimum position, please stop moving");
                robot.liftleft.setPower(0);
                robot.liftright.setPower(0);
            }
            else {
                // use less power if we are going down
                double liftPower = liftInput > 0
                        ? LIFT_SPEED * liftInput
                        : LIFT_SPEED * LIFT_DOWN_POWER_FACTOR * liftInput;

                robot.liftleft.setPower(-liftPower);
                robot.liftright.setPower(liftPower);
            }

            telemetry.addData("Lift",
                    "lift input %.2f",
                    liftInput);
            telemetry.addData("Lift",
                    "left/right lift values %7d %7d",
                    robot.liftleft.getCurrentPosition(), robot.liftright.getCurrentPosition());
        }
    }

    private void RunGrabber() {
        if(isGrabberEnabled) {
                double grabberInput = -gamepad2.right_stick_y;

                int invertedPosition = -robot.grabber.getCurrentPosition();
                // do not allow movement beyond limits
                if (invertedPosition > Open_Position && grabberInput > 0) {
                    telemetry.addData("grabber", "You have reached the Maximum position, please stop moving");
                    robot.grabber.setPower(0);
                }
                else if (invertedPosition < 0 && grabberInput < 0) {
                    telemetry.addData("grabber", "You have reached the Minimum position, please stop moving");
                    robot.grabber.setPower(0);
                }
                else {
                    robot.grabber.setPower(-grabberInput * Grabber_Power);
                }

                telemetry.addData("grabber",
                        "grabber input %.2f",
                        grabberInput);
                telemetry.addData("grabber",
                        "inverted encoder value %7d",
                        invertedPosition);
        }
    }

    private void RunDrive() {
        // define motor class variables
        double X;
        double Y;
        double Z;
        double RightTriggerInput;

        // collect user input from left and right gamepad controls and set internal variable X & Y & Z
        X = gamepad1.left_stick_x;
        Y = -gamepad1.left_stick_y;
        Z = -gamepad1.right_stick_x;
        if(gamepad1.left_trigger == 1 && gamepad1.right_trigger == 1) {
            RightTriggerInput = 1;
        } else if(gamepad1.right_trigger == 1){
            RightTriggerInput = 0.25;
        } else if(gamepad1.left_trigger == 1){
            RightTriggerInput = 0.5;
        }  else{
            RightTriggerInput = 0.75;
        }

        // use X, Y, & Z to set power for each of the motors
        if(isDriveEnabled) {
            robot.leftFrontDrive.setPower((Range.clip(Y + X + Z, -RightTriggerInput, RightTriggerInput)));
            robot.rightFrontDrive.setPower(Range.clip(X - Y + Z, -RightTriggerInput, RightTriggerInput));
            robot.leftRearDrive.setPower(Range.clip(Y - X + Z, -RightTriggerInput, RightTriggerInput));
            robot.rightRearDrive.setPower(Range.clip(-X - Y + Z, -RightTriggerInput, RightTriggerInput));
            // Send telemetry message to signify robot running;
            telemetry.addData("left front position", "%7d", robot.leftFrontDrive.getCurrentPosition());
            telemetry.addData("right front position", "%7d", robot.rightFrontDrive.getCurrentPosition());
            telemetry.addData("left rear position", "%7d", robot.leftRearDrive.getCurrentPosition());
            telemetry.addData("right rear position", "%7d", robot.rightRearDrive.getCurrentPosition());
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}

