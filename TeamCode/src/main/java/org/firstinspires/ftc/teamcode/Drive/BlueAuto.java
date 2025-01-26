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
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "BlueAuto", group = "Autonomous")
public class BlueAuto extends LinearOpMode {

    private MecanumDrive drive;
    private Intake intake;
    private IntakeController intakeController;
    private LiftsController liftsController;
    private Outtake outtake;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(10, -65, Math.toRadians(90)));
        intake = new Intake(hardwareMap);
        intakeController = new IntakeController(this);
        liftsController = new LiftsController(this);
        outtake = new Outtake(hardwareMap);

        Action moveLiftToHighBar = new Action() {
            @Override
            public boolean run(com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
                liftsController.moveToPosition(LiftsController.HIGH_BAR);
                return false; // Возвращаем false, чтобы действие завершилось
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

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                drive.actionBuilder(new Pose2d(10, -70, Math.toRadians(90)))
                                        .strafeTo(new Vector2d(10, -35))
                                        .build(),
                                moveLiftToHighBarPut
                        ),
                        new ParallelAction(
                                moveLiftToGround,
                                setClipsTakeState
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(10, -35, Math.toRadians(90)))
                                        .setReversed(true)
                                        .splineToConstantHeading(new Vector2d(30, -35), Math.toRadians(20))
                                        .build()
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(30, -35, Math.toRadians(20)))
                                        .splineToConstantHeading(new Vector2d(45, -10), Math.toRadians(12))
                                        .build()
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(45, -10, Math.toRadians(12)))
                                        .strafeTo(new Vector2d(45, -50))
                                        .build()
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(45, -50, Math.toRadians(90)))
                                        .splineToConstantHeading(new Vector2d(55, -10), Math.toRadians(35))
                                        .build()
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(55, -10, Math.toRadians(35)))
                                        .strafeTo(new Vector2d(55, -50))
                                        .build()
                        ),

                        new SequentialAction(
                                drive.actionBuilder(new Pose2d(55, -50, Math.toRadians(90)))
                                        .splineToConstantHeading(new Vector2d(61, -10), Math.toRadians(53))
                                        .strafeTo(new Vector2d(61, -55))
                                        .setReversed(false)
                                        .build()
                        ),

                        new SequentialAction(
                                setClipsPutState,
                                moveLiftToHighBar,
                                drive.actionBuilder(new Pose2d(61, -55, Math.toRadians(53)))
                                        .strafeTo(new Vector2d(10, -35))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBarPut
                        ),

                        new ParallelAction(
                                setClipsTakeState,
                                moveLiftToGround,
                                drive.actionBuilder(new Pose2d(10, -35, Math.toRadians(53)))
                                        .setReversed(true)
                                        .strafeTo(new Vector2d(45, -55))
                                        .build()
                        ),

                        new SequentialAction(
                                setClipsPutState,
                                moveLiftToHighBar,
                                drive.actionBuilder(new Pose2d(45, -55, Math.toRadians(53)))
                                        .strafeTo(new Vector2d(5, -35))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBarPut
                        ),

                        new ParallelAction(
                                setClipsTakeState,
                                moveLiftToGround,
                                drive.actionBuilder(new Pose2d(5, -35, Math.toRadians(53)))
                                        .setReversed(true)
                                        .strafeTo(new Vector2d(45, -55))
                                        .build()
                        ),

                        new SequentialAction(
                                setClipsPutState,
                                moveLiftToHighBar,
                                drive.actionBuilder(new Pose2d(45, -55, Math.toRadians(53)))
                                        .strafeTo(new Vector2d(4, -35))
                                        .setReversed(true)
                                        .build(),
                                moveLiftToHighBarPut
                        ),

                        new ParallelAction(
                                setClipsTakeState,
                                moveLiftToGround,
                                drive.actionBuilder(new Pose2d(4, -35, Math.toRadians(53)))
                                        .setReversed(true)
                                        .strafeTo(new Vector2d(45, -55))
                                        .build()
                        ),

                        new SequentialAction(
                                setClipsPutState,
                                moveLiftToHighBar,
                                drive.actionBuilder(new Pose2d(45, -55, Math.toRadians(53)))
                                        .strafeTo(new Vector2d(0, -35))
                                        .build(),
                                moveLiftToHighBarPut
                        ),

                        new ParallelAction(
                                setClipsTakeState,
                                moveLiftToGround,
                                drive.actionBuilder(new Pose2d(0, -35, Math.toRadians(53)))
                                        .setReversed(true)
                                        .strafeTo(new Vector2d(50, -55))
                                        .build()
                        )
                )
        );

        telemetry.addData("Status", "Autonomous Completed");
        telemetry.update();
    }
}