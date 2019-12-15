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

@TeleOp(name="Pushbot:Extendomatic TeleOp", group="Pushbot")
public class ExtendomaticsBot_TeleOp extends OpMode{

    /* Declare OpMode members. */
     ExtendomaticsHardware robot       = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware

    static final double LIFT_SPEED = 1;
    static final int Open_Position = 7600;
    static final double Grabber_Power = 1;
    static final double LIFT_MAX_EXTENSION_LIMIT = 460;
    static final double LIFT_DOWN_POWER_FACTOR = 0.2;
    static final double MAX_GRABBER_POS     =  1.25; // Initial Position
    static final double MIN_GRABBER_POS     =  0.0; // Closed/Hooked Position
    static final double MAX_DRAGGER_POS     =  1.0;
    static final double MIN_DRAGGER_POS     =  0;
    double target_dragger_position = MIN_DRAGGER_POS;
    double target_grabber_position = MIN_GRABBER_POS;

    static final boolean isDriveEnabled = true;
    static final boolean isLiftEnabled = true;
    static final boolean isGrabberEnabled = true;
    static final boolean isFoundationHookEnabled = true;
    static final boolean isGrabberServosEnabled = true;

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(super.hardwareMap, isDriveEnabled, isGrabberEnabled, isLiftEnabled, isFoundationHookEnabled, isGrabberServosEnabled);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello, Good Luck!");
        if (isFoundationHookEnabled)
        {
            telemetry.addData("Say","dragger position %.2f",robot.foundation_hook.getPosition());
            target_dragger_position = robot.foundation_hook.getPosition();
            //robot.foundation_hook.setPosition(MAX_DRAGGER_POS);
            //telemetry.addData("Say","dragger position %.2f",robot.foundation_hook.getPosition());
        }
        if (isGrabberServosEnabled)
        {
            telemetry.addData("Say","grabber position %.2f",robot.grabberServo_1.getPosition());
            target_grabber_position = robot.grabberServo_1.getPosition();
            //robot.grabberServo_1.setPosition(target_grabber_position);
        }
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
        RunGrabberServos();
        RunFoundationHook();

        telemetry.update();
    }

    private void RunFoundationHook() {
        double INCREMENT = 0.05;
        double current_dragger_position = robot.foundation_hook.getPosition();
//        if (Math.abs(current_dragger_position - target_dragger_position )<=INCREMENT*0.5)
//        {

            if(gamepad1.dpad_down) {
                target_dragger_position -= INCREMENT;
            }
            else if(gamepad1.dpad_up) {
                target_dragger_position += INCREMENT;
            }
 //           telemetry.addData("foundation", "dragger taking input");
            target_dragger_position = Range.clip(target_dragger_position, MIN_DRAGGER_POS, MAX_DRAGGER_POS);
            robot.foundation_hook.setPosition(target_dragger_position);

 //       }
 //       else
 //       {
 //           telemetry.addData("foundation", "dragger NOT taking input");
 //       }
        telemetry.addData("foundation", "dragger current %.2f target %.2f", current_dragger_position,target_dragger_position);
    }

    private void RunGrabberServos() {
        double INCREMENT = 0.05;
        double current_grabber_position = robot.grabberServo_1.getPosition();
//        if (Math.abs(current_grabber_position - target_grabber_position )<=INCREMENT*0.5)
//        {
            if(gamepad2.dpad_up) {
                target_grabber_position -= INCREMENT;
            }
            else if(gamepad2.dpad_down) {
                target_grabber_position += INCREMENT;
            }
//            telemetry.addData("foundation", "grabber taking input");
            target_grabber_position = Range.clip(target_grabber_position, MIN_GRABBER_POS, MAX_GRABBER_POS);
            robot.grabberServo_1.setPosition(target_grabber_position);
//        }
//        else
//        {
//            telemetry.addData("foundation", "grabber NOT taking input");
//        }
        telemetry.addData("foundation", "grabber current %.2f target %.2f", current_grabber_position,target_grabber_position);
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
        double TriggerInput;

        // collect user input from left and right gamepad controls and set internal variable X & Y & Z
        X = gamepad1.left_stick_x;
        Y = -gamepad1.left_stick_y;
        Z = -gamepad1.right_stick_x;
        if(gamepad1.left_trigger == 1 && gamepad1.right_trigger == 1) {
            TriggerInput = 1;
        } else if(gamepad1.right_trigger == 1){
            TriggerInput = 0.25;
        } else if(gamepad1.left_trigger == 1){
            TriggerInput = 0.5;
        }  else{
            TriggerInput = 0.9;
        }

        // use X, Y, & Z to set power for each of the motors
        if(isDriveEnabled) {
            robot.leftFrontDrive.setPower((Range.clip(Y + X + Z, -TriggerInput, TriggerInput)));
            robot.rightFrontDrive.setPower(Range.clip(X - Y + Z, -TriggerInput, TriggerInput));
            robot.leftRearDrive.setPower(Range.clip(Y - X + Z, -TriggerInput, TriggerInput));
            robot.rightRearDrive.setPower(Range.clip(-X - Y + Z, -TriggerInput, TriggerInput));
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

