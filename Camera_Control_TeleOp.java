package org.firstinspires.ftc.teamcode.Season_Robots.Tests.AGagne_Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Camera_Control", group="TeleOp")
public class Camera_Control_TeleOp extends LinearOpMode {

    DcMotor FrontLeft;
    DcMotor FrontRight;
    DcMotor BackLeft;
    DcMotor BackRight;
    Servo Pan;
    Servo Tilt;

    double setpower = 1.0;
    double leftMotorPower = 1.0;
    double rightMotorPower = 1.0;
    double TiltActive = 1;

    ElapsedTime ResetTime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        //Initializes the hardware

        FrontLeft = hardwareMap.dcMotor.get("FL");
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);

        FrontRight = hardwareMap.dcMotor.get("FR");
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        BackLeft = hardwareMap.dcMotor.get("BL");
        BackLeft.setDirection(DcMotor.Direction.FORWARD);

        BackRight = hardwareMap.dcMotor.get("BR");
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        Pan = hardwareMap.servo.get("Pan"); //Continuous Rotation Servo
        Tilt = hardwareMap.servo.get("Tilt"); //180 Degree Servo

        Tilt.setPosition(0.5); //Sets servo into "Zero" position
        TiltActive = 1;

        //End of Initialization

        waitForStart();

            while (opModeIsActive()) {

                //Speed Change
                if (gamepad1.a) {
                    setpower = 1.0;
                }

                if (gamepad1.b) {
                    setpower = 0.75;
                }

                if (gamepad1.x) {
                    setpower = 0.5;
                }

                if (gamepad1.y) {
                    setpower = 0.25;
                }

                //Driving Forward
                leftMotorPower = Range.clip(Math.pow(gamepad1.left_stick_y, 3), -setpower, setpower);
                rightMotorPower = Range.clip(Math.pow(gamepad1.left_stick_y, 3), -setpower, setpower);

                //Turning Left
                    //Not Done
                //Turning Right
                    //Not Done
                //Setting motor power to respective joystick input
                FrontLeft.setPower(leftMotorPower);
                FrontRight.setPower(rightMotorPower);
                BackLeft.setPower(leftMotorPower);
                BackRight.setPower(rightMotorPower);

                //Camera Control
                    //Pan
                    if (gamepad1.right_stick_x > .3) {
                        Pan.setPosition(1); //Tells servo to go forwards
                    }

                    if (gamepad1.right_stick_x < -.3) {
                        Pan.setPosition(0); //Tells servo to go backwards
                    }

                    if (gamepad1.right_stick_x < .3 && gamepad1.right_stick_x > -.3) {
                        Pan.setPosition(.5); //Tells Servo to sit still
                    }
                    //End of Pan

                    //Tilt THIS IS THE PROBLEM AREA!!!

                    double joy = gamepad1.right_stick_y; //-1 to 1
                    double TiltPos = joy / 2 + 0.5; //-0.5 to 0.5 then 0 to 1 for 180 degree servo

                    if (TiltActive > 0.5){ //Tilt Control Loop
                        /*if (gamepad1.left_bumper) {
                            Tilt.setPosition(TiltPos); //Sets servo position to right_stick_y, left_bumper also breaks loop, hence holding tilt pos
                        }*/

                        if (gamepad1.left_bumper) {
                            TiltActive = 0;
                        }

                        else {
                            Tilt.setPosition(TiltPos); //Sets servo position to right_stick_y
                        }
                    }//End of Tilt Control Loop

                    if (TiltActive < 0.5){
                        if (gamepad1.left_bumper) {
                            TiltActive = 1;
                        }
                    }
                    //End of Tilt
                //End of Camera Control

                //Telemetry Data
                telemetry.addData("Current Speed", setpower); //Tells Current Speed
                telemetry.addData("Current Angle", TiltPos-0.5); //Says Current Angle of TiltServo (-0.5 Says exact position relative to the camera)
                telemetry.addData("TiltActive? 1=Yes, 0=No", TiltActive); //says whether tilt is active
                telemetry.update();

            }
    }
}
