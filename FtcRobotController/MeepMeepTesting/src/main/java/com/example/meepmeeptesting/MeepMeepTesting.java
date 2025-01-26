package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity BlueSpecimen = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .build();

        BlueSpecimen.runAction(BlueSpecimen.getDrive().actionBuilder(new Pose2d(9, -65, Math.toRadians(90)))

                        .strafeTo(new Vector2d(10, -35))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(30, -35), Math.toRadians(20))
                .splineToConstantHeading(new Vector2d(45, -10), Math.toRadians(12))
                                .strafeTo(new Vector2d(45, -50))
                .setTangent(Math.toRadians(90)) // Устанавливаем плавный угол входа
                .splineToConstantHeading(new Vector2d(55, -10), Math.toRadians(30))
                .strafeTo(new Vector2d(55, -50))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(61, -10), Math.toRadians(53))
                .strafeTo(new Vector2d(61, -55))
                .setReversed(false)
                                .strafeTo(new Vector2d(10, -35)) //2 specimen
                        .waitSeconds(0.5)
                .setReversed(true)
                .strafeTo(new Vector2d(45, -55))
                        .waitSeconds(0.5)
                .strafeTo(new Vector2d(5, -35))
                        .waitSeconds(0.5)
                .setReversed(true)
                .strafeTo(new Vector2d(45, -55))
                        .waitSeconds(0.5)
                .strafeTo(new Vector2d(4, -35))
                        .waitSeconds(0.5)
                .setReversed(true)
                .strafeTo(new Vector2d(45, -55))
                        .waitSeconds(0.5)
                .strafeTo(new Vector2d(0, -35))
                        .waitSeconds(0.5)
                .setReversed(true)
                .strafeTo(new Vector2d(50, -55))
                .build());  //первый робот



        RoadRunnerBotEntity BlueBasket = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .build();

        BlueBasket.runAction(BlueBasket.getDrive().actionBuilder(new Pose2d(-33, -65, Math.toRadians(90)))

                .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-50, -45, Math.toRadians(85)), Math.toRadians(0))//1 one
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55, -45, Math.toRadians(100)), Math.toRadians(0))//2nd one
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-57, -45, Math.toRadians(115)), Math.toRadians(0))//3d one
                        .waitSeconds(0.5)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-52, -55, Math.toRadians(45)), Math.toRadians(0))
                        .waitSeconds(0.5)
                .splineToLinearHeading(new Pose2d(-25, -10, Math.toRadians(180)), Math.toRadians(0))
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(BlueSpecimen)
                .addEntity(BlueBasket)
                .start();
    }
}