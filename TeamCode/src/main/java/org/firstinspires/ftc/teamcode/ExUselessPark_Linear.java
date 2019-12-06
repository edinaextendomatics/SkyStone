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
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Linear USELESS Park", group="Autonomous Park")
public class ExUselessPark_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    ExtendomaticsHardware robot = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    // Setting speed constants for turning and moving sideways or forward
    static final double FORWARD_SPEED = 1;

    static boolean isRed = false;
    static boolean isFoundationSide = false;
    static boolean parkCenter = true;
    static double WaitTime = 23.0;
    static double uselessdrivetime = 2.5;
    static double backwardscenterime = 1.5;
    static double backwardssideime = 2.25;
    static double sideDriveTime = 0.8;
    static boolean finalChoices = false;
    
    @Override
    public void runOpMode() {

        robot.init(hardwareMap, true, false, false, false, false);

        while (!finalChoices)
        {
            telemetry.addData("bot", "we got to the while loop! yay!");
            userInput();
            idle();
        }
        String teamMessage = (isRed ? "Red " : "Blue ") + "Team selected";
        String positionMessage = (isFoundationSide ? "Foundation " : "Block ") + "Side selected";
        String parkMessage = (parkCenter ? "Center " : "Side ") + "park selected";
        telemetry.addData("Say", "Final Choices are in");
        telemetry.addData("Say", teamMessage);
        telemetry.addData("Say", positionMessage);
        telemetry.addData("Say", parkMessage);
        telemetry.update();
        waitForStart();
        executeDriving();
    }

    public void executeDriving()
    {
        // driving forward for a number of seconds defined by forwardDrive time
        robot.setPowerForward(FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < uselessdrivetime) {
            telemetry.addData("Path", " Drive: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        robot.setPowerForward(0);
        // WAIT in the middle for the wait time
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            telemetry.addData("timer", "Countdown: %2.5f", (WaitTime - runtime.seconds()));
            telemetry.update();
        }
        // back up to center or side
        robot.setPowerForward(-FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < (parkCenter ? backwardscenterime : backwardssideime)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // stop the robot
        robot.setPowerForward(0);
        telemetry.addData("Say", "Robot stopped");
        telemetry.update();


        double colorDirection = isRed ? 1 : -1;
        double sidePosition = isFoundationSide ? -1 : 1;
        robot.setPowerRight(colorDirection * sidePosition);
        runtime.reset();
        while (runtime.seconds() < sideDriveTime) {
            telemetry.addData("Path", "Side Drive: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

        }
        robot.setPowerForward(0);
        telemetry.addData("Say", "Robot stopped");
        telemetry.update();
        finalChoices = false;
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

}