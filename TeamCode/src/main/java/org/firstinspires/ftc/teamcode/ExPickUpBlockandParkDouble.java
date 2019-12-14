package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


    @Autonomous(name="ExtendoBot: Block Double", group="Autonomous")

    public class ExPickUpBlockandParkDouble extends LinearOpMode {
        /* Declare OpMode members. */
        ExtendomaticsHardware robot = new ExtendomaticsHardware(telemetry);
        private ElapsedTime runtime = new ElapsedTime();
        static final double COUNTS_PER_INCH = 98.3606557;
        double COUNTS_PER_90_DEGREE = -19;
        static final double DRIVE_SPEED = 0.9;
        static final double down   = 1.25;
        static final double up = 0.75;
        double run_1_Block = 0;
        static final double up   = 0.75;
        static final double down = 1.25;
        static final double run_1_Block = 0;
        boolean finalChoices = false;
        boolean isRed = false;
        boolean isFoundationSide = true;
        boolean parkCenter = true;

        public void runOpMode(){

            robot.init(hardwareMap, true, false, false, false, true);

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
            execute_block();
        }

        public void execute_block() {
            double forwardParkCenter = parkCenter ? -4:-28;
            double colorDirection = isRed ? 1:-1;
            double sidePosition = isFoundationSide ? -1:1;
            double first_run_position = 8*run_1_Block;

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
            // Orientation is having the front of the bot face away from the drivers, set 2ft from alliance bridge

            // Sequence initiated
            // first sequence
            driveForward(DRIVE_SPEED, 29.75, 3.5);
            driveRight(DRIVE_SPEED,-colorDirection*sidePosition*first_run_position, 3);
            robot.grabberServo_1.setPosition(down);
            sleep(1000);
            driveForward(DRIVE_SPEED, forwardParkCenter, 4.0);
            turnRight(DRIVE_SPEED, colorDirection*sidePosition*90, 2);
            driveForward(DRIVE_SPEED,-colorDirection*sidePosition*(first_run_position+48), 6);
            robot.grabberServo_1.setPosition(up);
            sleep(1000);
            driveForward(DRIVE_SPEED, colorDirection*sidePosition*8, 1);
            robot.grabberServo_1.setPosition(down);
            driveForward(DRIVE_SPEED, colorDirection*sidePosition*20, 3);
            sleep(500);
            //second sequence
            //driveForward(DRIVE_SPEED, 30, 3.5);
            //driveRight(DRIVE_SPEED,-colorDirection*sidePosition*second_run_position, 5);
            //robot.grabberServo_1.setPosition(1.25);
            //sleep(1000);
            //driveForward(DRIVE_SPEED, forwardParkCenter, 4.0);
            //driveRight(DRIVE_SPEED,colorDirection*sidePosition*(second_run_position+48), 5);
            //robot.grabberServo_1.setPosition(0.75);
            //driveRight(DRIVE_SPEED, -24, 3);
            sleep(500);

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }

        public void userInput() {
            if (gamepad1.dpad_left){
                run_1_Block -= 1;
                if(run_1_Block<=0){
                    run_1_Block = 0;
                }
                telemetry.addData("1st run block number : ",run_1_Block);
            }
            if (gamepad1.dpad_right){
                run_1_Block += 1;
                if(run_1_Block>=1){
                    run_1_Block = 1;
                }
                telemetry.addData("1st run block number : ",run_1_Block);
            }
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
            newLeftFrontTarget = robot.leftFrontDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_90_DEGREE);
            newRightFrontTarget = robot.rightFrontDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_90_DEGREE);
            newLeftRearTarget = robot.leftRearDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_90_DEGREE);
            newRightRearTarget = robot.rightRearDrive.getCurrentPosition() + (int) (degrees * COUNTS_PER_90_DEGREE);

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

