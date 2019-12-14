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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ExtendomaticsHardware
{
    Telemetry telemetry;

    /* Public OpMode members. */
    public DcMotor  leftFrontDrive   = null;
    public DcMotor  rightFrontDrive  = null;
    public DcMotor  leftRearDrive = null;
    public DcMotor  rightRearDrive = null;
    public DcMotor  liftleft = null;
    public DcMotor  liftright = null;
    public DcMotor  grabber = null;
    public Servo    foundation_hook = null;
    public Servo    grabberServo_1 = null;
    public static double INITIAL_SERVO_POSITION = 0;
    public static double INITIAL_FOUNDATION_SERVO_POSITION = 1;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public ExtendomaticsHardware(Telemetry telemetry){
        this.telemetry = telemetry;
    }
    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        init(ahwMap, true, true, true, true, true);
    }

    public void init(HardwareMap ahwMap, boolean initDriveMotors, boolean initGrabber, boolean initLift, boolean initfoundation_hook, boolean initGrabberServos) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        telemetry.addData("Init()",
                "initDriveMotors=%b; initGrabber=%b; initLift=%b",
                initDriveMotors,
                initGrabber,
                initLift);

        if (initDriveMotors){
            leftFrontDrive  = hwMap.get(DcMotor.class, "left_front_drive");
            rightFrontDrive = hwMap.get(DcMotor.class, "right_front_drive");
            leftFrontDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
            leftRearDrive  = hwMap.get(DcMotor.class, "left_rear_drive");
            rightRearDrive = hwMap.get(DcMotor.class, "right_rear_drive");
            leftRearDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            rightRearDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

            // Set all motors to zero power
            leftFrontDrive.setPower(0);
            rightFrontDrive.setPower(0);
            leftRearDrive.setPower(0);
            rightRearDrive.setPower(0);

            // Set all motors to run without encoders.
            // May want to use RUN_USING_ENCODERS if encoders are installed.
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            telemetry.addData("Init()", "Drive motors initialized.");
        }

        if (initGrabber){
            grabber = hwMap.get(DcMotor.class, "grabber");
            grabber.setDirection(DcMotor.Direction.REVERSE);
            grabber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            telemetry.addData("Init()", "Grabber motor initialized.");
        }

        if (initLift) {
            liftleft = hwMap.get(DcMotor.class, "liftleft");
            liftright = hwMap.get(DcMotor.class, "liftright");
            liftleft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            liftright.setDirection(DcMotor.Direction.REVERSE) ;
            liftleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftleft.setPower(0);
            liftright.setPower(0);
            telemetry.addData("Init()", "Lift left motor initialized.");
            telemetry.addData("Init()", "Lift right motor initialized.");
        }
        if (initfoundation_hook) {
            foundation_hook = hwMap.get(Servo.class, "foundation_hook");
            foundation_hook.setPosition(INITIAL_FOUNDATION_SERVO_POSITION);
        }
        if (initGrabberServos) {
            grabberServo_1 = hwMap.get(Servo.class, "grabberServo_1");
            //grabberServo_1.setPosition(INITIAL_SERVO_POSITION);
        }

    }
    public  void  setPowerForward(double speed)
    {
        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(-speed);
        leftRearDrive.setPower(speed);
        rightRearDrive.setPower(-speed);
    }
    public  void  setPowerRight(double speed)
    {
        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftRearDrive.setPower(-speed);
        rightRearDrive.setPower(-speed);
    }
    public  void setPowerTurnRight (double speed)
    {
        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftRearDrive.setPower(speed);
        rightRearDrive.setPower(speed);
    }
 }

