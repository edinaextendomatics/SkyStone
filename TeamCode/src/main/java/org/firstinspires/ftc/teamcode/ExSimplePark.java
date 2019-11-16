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

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

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

@Autonomous(name="Simple Park", group="Autonomous Park")
public class ExSimplePark extends OpMode{

    /* Declare OpMode members. */
    ExtendomaticsHardware robot       = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    // Setting speed constants for turning and moving sideways or forward
    static final double     FORWARD_SPEED = 1;

    static boolean isRed = false;
    static boolean isFoundationSide = false;
    static boolean parkCenter = false;
    static double forwardDriveTime = 1.25;
    static double sideDriveTime = 0.75;
    static double MaxTime = 3.0;


    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap,true,false,false);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Press X for Blue Team, B for Red Team");
        telemetry.addData("Say", "Press Y for Foundation Side, A for Blocks Side");
        telemetry.addData("Say", "Press Start for center parking, press back for side parking");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        if (gamepad1.start) {
            parkCenter = true;
        }
        if (gamepad1.back)
        {
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
        String teamMessage = (isRed ? "Red " : "Blue ") + "Team selected";
        String positionMessage = (isFoundationSide ? "Foundation " : "Block ") + "Side selected";
        String parkMessage = (parkCenter ? "Center " : "Side ") + "park selected";
        telemetry.addData("Say", teamMessage);
        telemetry.addData("Say", positionMessage);
        telemetry.addData("Say", parkMessage);
        telemetry.addData("Say", "Press X for Blue Team, B for Red Team");
        telemetry.addData("Say", "Press Y for Foundation Side, A for Blocks Side");
        telemetry.addData("Say", "Press Start for center parking, press back for side parking");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        int rightDirection = isRed ? -1 : 1;
        // driving forward for a number of seconds defined by forwardDrive time
        robot.setPowerForward(FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < (parkCenter ? 1 : 0.2) * forwardDriveTime) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        //this multiplies the 2 varibles foundation direction and right direction to output
        // then drives right for sideDriveTime seconds
        robot.setPowerRight(rightDirection * FORWARD_SPEED);
        runtime.reset();
        while (runtime.seconds() < sideDriveTime) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        // stop the robot
        robot.setPowerRight(0);
        robot.setPowerForward(0);
        robot.setPowerTurnRight(0);
        telemetry.addData("Say", "Robot stopped");
        telemetry.update();
    }
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

    }




    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
