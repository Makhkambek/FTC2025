package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


public class DriveTrain extends OpMode {
    private Intake intake;
    private Outtake outtake;
    private LiftsController liftMotors;
    private IntakeController intakeMotor;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private ElapsedTime timer = new ElapsedTime();
    private enum LeftTriggerState {
        LIFT_TO_HIGHEST, DROPPER_OPEN_AND_RESET
    }

    // Состояния для left_bumper
    private enum LeftBumperState {
        CLIPS_TAKE, CLIPS_PUT_AND_HIGH_BAR, HIGH_BAR_PUT_AND_RESET
    }
    private LeftTriggerState leftTriggerState = LeftTriggerState.LIFT_TO_HIGHEST;
    private LeftBumperState leftBumperState = LeftBumperState.CLIPS_TAKE;

    private boolean wasLeftTriggerPressed = false;
    private boolean wasLeftBumperPressed = false;
    private boolean wasRightTriggerPressed = false;

    @Override
    public void init() {
        // Инициализация подсистем
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        liftMotors = new LiftsController(this);
        intakeMotor = new IntakeController(this);

        // Инициализация Mecanum Drive
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Установка направления вращения моторов
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
    }


    @Override
    public void loop() {
        drive();
        codeForLift();
        codeForIntake();
    }



    private void drive() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower),
                Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            backLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backRightPower /= maxPower;
        }

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }


    private void codeForLift() {
        if (gamepad2.left_trigger > 0 && !wasLeftTriggerPressed) {
            wasLeftTriggerPressed = true;

            switch (leftTriggerState) {
                case LIFT_TO_HIGHEST:
                    liftMotors.moveToPosition(LiftsController.HIGHEST_BASKET);
                    leftTriggerState = LeftTriggerState.DROPPER_OPEN_AND_RESET;
                    break;

                case DROPPER_OPEN_AND_RESET:
                    outtake.dropper.setPosition(Outtake.DROPPER_OPEN);
                    liftMotors.moveToPosition(LiftsController.GROUND);
                    outtake.setGrabState();
                    leftTriggerState = LeftTriggerState.LIFT_TO_HIGHEST;
                    break;
            }
        }

        if (gamepad2.left_trigger == 0) {
            wasLeftTriggerPressed = false;
        }

        if (gamepad2.left_bumper && !wasLeftBumperPressed) {
            wasLeftBumperPressed = true;

            switch (leftBumperState) {
                case CLIPS_TAKE:
                    outtake.setClipsTakeState();
                    leftBumperState = LeftBumperState.CLIPS_PUT_AND_HIGH_BAR;
                    break;

                case CLIPS_PUT_AND_HIGH_BAR:
                    outtake.setClipsPutState();
                    liftMotors.moveToPosition(LiftsController.HIGH_BAR);
                    leftBumperState = LeftBumperState.HIGH_BAR_PUT_AND_RESET;
                    timer.reset();
                    break;

                case HIGH_BAR_PUT_AND_RESET:
                    if (timer.seconds() < 1.0) {
                        liftMotors.moveToPosition(LiftsController.HIGH_BAR_PUT);
                    } else if (timer.seconds() < 1.5) {
                        outtake.setClipsTakeState();
                        liftMotors.moveToPosition(LiftsController.GROUND);
                        leftBumperState = LeftBumperState.CLIPS_TAKE;
                        timer.reset();
                    }
                    break;
            }
        }

        if (!gamepad2.left_bumper) {
            wasLeftBumperPressed = false;
        }

        outtake.update();
    }


    private void codeForIntake() {

    if (gamepad2.right_trigger > 0 && !wasRightTriggerPressed) {
        wasRightTriggerPressed = true;

        if (gamepad2.right_trigger <= 0.5) {
            intakeMotor.moveToPosition(IntakeController.MEDIUM);
        } else {
            intakeMotor.moveToPosition(IntakeController.LONG);
        }
        intake.setOpenState();
        outtake.setGrabState();
    }
        if (gamepad2.right_trigger == 0) {
            wasRightTriggerPressed = false;
        }

        if (gamepad2.right_bumper) {
            if (timer.seconds() < 0.8) {
                intake.setClosedState();
                intakeMotor.moveToPosition(IntakeController.ZERO); //проверить работает ли
            } else if (timer.seconds() < 1.0) {
                outtake.dropper.setPosition(Outtake.DROPPER_CLOSE);
            } else if (timer.seconds() < 1.3) {
                intake.intakeGrab.setPosition(Intake.INTAKE_GRAB_OPEN);
            } else if (timer.seconds() < 1.8) {
                outtake.setScoreState();
            }
            else {
                timer.reset();
            }
        } else if (gamepad2.dpad_left) {
            intake.setTurnPosition1();
        } else if (gamepad2.dpad_right) {
            intake.setTurnPosition2();
        } else if (gamepad1.right_bumper) {
            outtake.dropper.setPosition(Outtake.DROPPER_OPEN);
        }

        intake.update();
        outtake.update();
    }
}