package org.firstinspires.ftc.teamcode.Drive;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous(name = "BlueHighBasket", group = "Autonomous")
public class BlueHighBasket extends LinearOpMode {

    private MecanumDrive drive;
    private Intake intake;
    private IntakeController intakeController;
    private LiftsController liftsController;
    private Outtake outtake;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(-33, -65, Math.toRadians(90)));
        intake = new Intake(hardwareMap);
        intakeController = new IntakeController(this);
        liftsController = new LiftsController(this);
        outtake = new Outtake(hardwareMap);

        Action openIntake = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                intake.setOpenState();
                intakeController.moveToPosition(IntakeController.MEDIUM);
                return false;
            }
        };

        Action grabIntake = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                intake.setClosedState();
                intakeController.moveToPosition(IntakeController.ZERO);
                return false;
            }
        };

        Action moveLiftToHighBar = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                liftsController.moveToPosition(LiftsController.HIGH_BAR);
                return false;
            }
        };

        Action moveLiftToHighBarPut = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                liftsController.moveToPosition(LiftsController.HIGH_BAR_PUT);
                return false;
            }
        };

        Action moveLiftToGround = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                liftsController.moveToPosition(LiftsController.GROUND);
                return false;
            }
        };

        Action moveLiftToHighBasket = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                liftsController.moveToPosition(LiftsController.HIGHEST_BASKET);
                return false;
            }
        };

        Action setClipsTakeState = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.setClipsTakeState();
                return false;
            }
        };

        Action setClipsPutState = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.setClipsPutState();
                return false;
            }
        };

        Action setGrabState = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.setGrabState();
                return false;
            }
        };

        Action openDropper = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.dropper.setPosition(Outtake.DROPPER_OPEN);
                return false;
            }
        };

        Action closeDropper = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.dropper.setPosition(Outtake.DROPPER_CLOSE);
                return false;
            }
        };

        Action setScoreState = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                outtake.setScoreState();
                return false;
            }
        };

        Action intakeClawOpen = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                intake.intakeGrab.setPosition(Intake.INTAKE_GRAB_OPEN);
                return false;
            }
        };

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(-33, -65, Math.toRadians(90)))
                                        .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBasket,
                                setScoreState

                        ),

                        new SequentialAction(
                                openDropper
                        ),

                        new ParallelAction(
                                moveLiftToGround,
                                setGrabState,
                                openIntake
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(10, -35, Math.toRadians(90))) //2nd specimen take
                                        .splineToLinearHeading(new Pose2d(-50, -45, Math.toRadians(85)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                grabIntake,
                                closeDropper,
                                intakeClawOpen
                        ),

                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(-50, -45, Math.toRadians(85)))
                                        .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBasket,
                                setScoreState
                        ),

                        new SequentialAction(
                               openDropper
                        ),

                        new ParallelAction(
                                moveLiftToGround,
                                setGrabState,
                                openIntake
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(-52, -55, Math.toRadians(45))) //3d specimen take
                                        .splineToLinearHeading(new Pose2d(-55, -45, Math.toRadians(100)), Math.toRadians(0))
                                        .build(),
                                grabIntake,
                                closeDropper,
                                intakeClawOpen
                        ),

                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(-55, -45, Math.toRadians(100)))
                                        .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBasket,
                                setScoreState
                        ),

                        new SequentialAction(
                                openDropper
                        ),

                        new ParallelAction(
                                moveLiftToGround,
                                setGrabState,
                                openIntake
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(-52, -55, Math.toRadians(45))) //4 specimen take
                                        .splineToLinearHeading(new Pose2d(-57, -45, Math.toRadians(115)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                grabIntake,
                                closeDropper,
                                intakeClawOpen
                        ),

                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(-57, -45, Math.toRadians(115)))
                                        .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBasket,
                                setScoreState
                        ),

                        new SequentialAction(
                                openDropper
                        ),

                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(-52, -55, Math.toRadians(45))) //parking
                                        .splineToLinearHeading(new Pose2d(-25, -10, Math.toRadians(180)), Math.toRadians(0))
                                        .build(),
                                moveLiftToGround,
                                setGrabState

                        )

                )
        );

        telemetry.addData("Status", "Autonomous Completed");
        telemetry.update();
    }
}