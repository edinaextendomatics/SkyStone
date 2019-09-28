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
import com.qualcomm.robotcore.hardware.Servo;
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

@TeleOp(name="Pushbot:TeleOpHolonomic", group="Pushbot")
public class ExHolonomicDrive_TeleOp extends OpMode{

    /* Declare OpMode members. */
     ExHardwareHolonomicBot robot       = new ExHardwareHolonomicBot(); // use the class created to define a Pushbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */
    static final double INCREMENT   = 0.02;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    double Ztracker = 0;
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.grabber.setPosition((MAX_POS+MIN_POS)/2);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello, Good Luck!");    //
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
        Ztracker = 0;
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // define motor class variables
        double Y;
        double X;
        double Z;
        double motorZ;
        // Define grabber variables
        double  position = robot.grabber.getPosition(); // Start at halfway position

        // MOTOR contols section
        // collect user input from left and right gamepad controls and set internal variable X & Y
        Y = -gamepad1.left_stick_y;
        X = gamepad1.left_stick_x;
        Z = gamepad1.right_stick_x;

        if (Z == 0 && Ztracker != 0)
        {
            telemetry.addData("We are going the opposite way!","&.2f", Ztracker);
            motorZ = -Ztracker;
        }
        else
        {
            motorZ = Z;
        }
        // use X, Y, & Z to set power for each of the motors
        robot.leftFrontDrive.setPower(Range.clip(Y+X+motorZ,-1, 1));
        robot.rightFrontDrive.setPower(Range.clip(X-Y+motorZ,-1, 1));
        robot.leftRearDrive.setPower(Range.clip(Y-X+motorZ,-1, 1));
        robot.rightRearDrive.setPower(Range.clip(-X-Y+motorZ,-1, 1));
        Ztracker = Z;
        // Send telemetry message to signify robot running;
        telemetry.addData("leftpad Y",  "%.2f", Y);
        telemetry.addData("leftpad X", "%.2f", X);
        telemetry.addData("rightpad Z", "%.2f", Z);
        // GRABBER controls section
        if(gamepad1.right_trigger > 0)
        {
            position+=INCREMENT;
            if(position >= MAX_POS){
                position = MAX_POS;
            }
        }
        if(gamepad1.left_trigger > 0){
            position-=INCREMENT;
            if(position <= MIN_POS){
                position = MIN_POS;
            }
        }
        robot.grabber.setPosition(position);
        telemetry.addData("grabber position", "%.2f", position);
        telemetry.addData("grabber right trigger", "%.2f", gamepad1.right_trigger);
        telemetry.addData("grabber left trigger", "%.2f", gamepad1.left_trigger);
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
