package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    // Servo position constants
    public static final double INTAKE_ARM_LEFT_OPEN = 0.0;
    public static final double INTAKE_ARM_RIGHT_OPEN = 0.0;
    public static final double INTAKE_ROTATE_OPEN = 0.5;
    public static final double INTAKE_GRAB_OPEN = 0.3;

    public static final double INTAKE_ARM_LEFT_CLOSED = 1.0;
    public static final double INTAKE_ARM_RIGHT_CLOSED = 1.0;
    public static final double INTAKE_ROTATE_CLOSED = 0.0;
    public static final double INTAKE_GRAB_CLOSED = 0.8;

    public static final double INTAKE_TURN_POSITION_1 = 0.2;  // Позиция 1
    public static final double INTAKE_TURN_POSITION_2 = 0.8;  // Позиция 2
    public static final double INTAKE_TURN_DEFAULT = 0.5;     // Дефолтная позиция

    // Servo objects
    private final Servo intakeArmLeft;
    private final Servo intakeArmRight;
    private final Servo intakeRotate;
    private final Servo intakeTurn;
    public Servo intakeGrab;

    private boolean grabToggled = false;

    // FSM States
    private enum State {
        OPEN,
        CLOSED,
        IDLE
    }

    private State currentState = State.IDLE;
    private ElapsedTime timer = new ElapsedTime();

    public Intake(HardwareMap hardwareMap) {
        intakeArmLeft = hardwareMap.get(Servo.class, "intake_arm_left");
        intakeArmRight = hardwareMap.get(Servo.class, "intake_arm_right");
        intakeRotate = hardwareMap.get(Servo.class, "intake_rotate");
        intakeTurn = hardwareMap.get(Servo.class, "intake_turn");
        intakeGrab = hardwareMap.get(Servo.class, "intake_grab");

        setClosedPositions(); // Изначально закрыто
    }

    public void update() {
        switch (currentState) {
            case OPEN:
                executeOpen();
                break;
            case CLOSED:
                executeClosed();
                break;
            case IDLE:
                break;
        }
    }

    private void executeOpen() {
        if (timer.seconds() < 0.3) {
            intakeRotate.setPosition(INTAKE_ROTATE_OPEN);
            intakeTurn.setPosition(INTAKE_TURN_DEFAULT);
            intakeGrab.setPosition(INTAKE_GRAB_OPEN);
        } else {
            currentState = State.IDLE;
            timer.reset();
        }
    }

    private void executeClosed() {
        if (timer.seconds() < 0.3) {
            intakeArmLeft.setPosition(INTAKE_ARM_RIGHT_OPEN);
            intakeArmRight.setPosition(INTAKE_ARM_RIGHT_OPEN);
            intakeGrab.setPosition(INTAKE_GRAB_CLOSED);
        } else if (timer.seconds() < 0.8) {
            intakeArmLeft.setPosition(INTAKE_ARM_LEFT_CLOSED);
            intakeArmRight.setPosition(INTAKE_ARM_RIGHT_CLOSED);
            intakeRotate.setPosition(INTAKE_ROTATE_CLOSED);
            intakeTurn.setPosition(INTAKE_TURN_DEFAULT);
        }
        else {
            currentState = State.IDLE;
            timer.reset();
        }
    }

    public void setOpenState() {
        currentState = State.OPEN;
        timer.reset();
    }

    public void setClosedState() {
        currentState = State.CLOSED;
        timer.reset();
    }

    private void setClosedPositions() {
        intakeGrab.setPosition(INTAKE_GRAB_CLOSED);
        intakeArmLeft.setPosition(INTAKE_ARM_LEFT_CLOSED);
        intakeArmRight.setPosition(INTAKE_ARM_RIGHT_CLOSED);
        intakeRotate.setPosition(INTAKE_ROTATE_CLOSED);
    }

    // Управление intakeTurn
    public void setTurnPosition1() {
        intakeTurn.setPosition(INTAKE_TURN_POSITION_1);
    }

    public void setTurnPosition2() {
        intakeTurn.setPosition(INTAKE_TURN_POSITION_2);
    }

    public void setTurnDefault() {
        intakeTurn.setPosition(INTAKE_TURN_DEFAULT);
    }

    public void toggleGrab() {
        grabToggled = !grabToggled;
        if (grabToggled) {
            intakeGrab.setPosition(INTAKE_GRAB_OPEN);
        } else {
            intakeGrab.setPosition(INTAKE_GRAB_CLOSED);
        }
    }
}