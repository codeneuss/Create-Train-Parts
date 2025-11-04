package com.tiestoettoet.create_train_parts.content.decoration.trainSlide;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import com.tiestoettoet.create_train_parts.AllPartialModels;
//import com.simibubi.create.foundation.render.
import com.tiestoettoet.create_train_parts.CreateTrainParts;
import com.tiestoettoet.create_train_parts.content.decoration.encasing.EncasedCTBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.createmod.catnip.render.CachedBuffers;

public class TrainSlideRenderer extends SafeBlockEntityRenderer<TrainSlideBlockEntity> {

    public TrainSlideRenderer() {
    }

    @Override
    protected void renderSafe(TrainSlideBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        Level world = be.getLevel();
        if(world instanceof VirtualRenderWorld)
            return;

        if(!be.shouldRenderSpecial(be.getBlockState()))
            return;


        renderTrainSlide(
                be.getBlockState(),
                be.getBlockPos(),
                world,
                world,
                be.animation.getValue(partialTicks),
                ms,
                buffer,
                light,
                null
        );

        /*BlockState blockState = be.getBlockState();
        // System.out.println("Partial Ticks: " + partialTicks);
        if (!be.shouldRenderSpecial(blockState))
            return;

        BlockPos pos = be.getBlockPos();
        BlockAndTintGetter world = be.getLevel();

        ConnectedTextureBehaviour behaviour = new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING);

        Direction facing = blockState.getValue(TrainSlideBlock.FACING);
        CTType dataType = behaviour.getDataType(world, pos, blockState, facing);

        if (dataType == null) {
            // System.out.println("Data type is null");
            return;
        }
        // Direction face = Direction.UP;

        // int textureIndex = textureIndexV % 8;

        float rotationAngle = switch (facing) {
            case NORTH -> 0; // No rotation needed
            case SOUTH -> 180;
            case WEST -> 90;
            case EAST -> -90;
            default -> 0;
        };

        float value = be.animation.getValue(partialTicks);
        float exponentialValue = (float) value * value;
        float relativeValue = blockState.getValue(TrainSlideBlock.OPEN) ? exponentialValue : 1 - exponentialValue;
        float relativeAnimationValue = relativeValue;

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        if (blockState.getBlock() instanceof TrainSlideBlock) {

            ResourceLocation blockTexture = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
            String blockTexturePath = blockTexture.getPath();
            // System.out.println("Block Texture Path: " + blockTexturePath);
            TrainSlideBlock.ConnectedState connectedState = blockState.getValue(TrainSlideBlock.CONNECTED);

            float f = blockState.getValue(TrainSlideBlock.OPEN) ? -1 : 1;

            // partial_flap.translate(0, 10.5 / 16f, 0.5 / 16f);

            float movement = 5 / 16f * exponentialValue * f; // Clamp value to avoid unexpected results
            Direction movementDirection = blockState.getValue(TrainSlideBlock.OPEN) ? facing.getOpposite() : facing;
            // System.out.println("Movement Direction: " + movementDirection);
            Vec3 moveOffset = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movement);

            // Vec3 pivotOrigin = new Vec3(10.5 / 16f, 15.5 / 16f, 8 / 16f); // Define the
            // origin point from the model file

            float movementP = (float) (10.5 / 16f);
            Vec3 moveOffsetP = Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementP);

            float rotationPivot;

            if (blockState.getValue(TrainSlideBlock.OPEN)) {
                rotationPivot = 90 * f * exponentialValue;
                // Open to closed: Translate and rotate correctly
                // partial_pivot.translate(0, 15.5 / 16f, 10.5 / 16f)
                // .rotateXDegrees(90 * value * f)
                // .light(light)
                // .renderInto(ms, vb);
                // System.out.println("Rotation: " + (90 * value * f));
            } else {
                rotationPivot = 90 * f * (1 - exponentialValue) - 90;
                // Closed to open: Reverse translation and rotation
                // partial_pivot.translate(0, 15.5 / 16f, 10.5 / 16f)
                // .rotateXDegrees(90 * (1 - value) * f - 90)
                // .light(light)
                // .renderInto(ms, vb);
                // System.out.println("Rotation: " + (90 * (1 - value) * f - 90));
            }

            // System.out.println("MovementF: " + movementF.x + ", " + movementF.y + ", " +
            // movementF.z);
            // System.out.println("Value: " + value);

            // float animFirstTenth = Mth.clamp(value / 0.1f, 0, 1);
            // float animSecondTenth = Mth.clamp((value - 0.1f) / 0.1f, 0, 1);
            // float animThirdTenth = Mth.clamp((value - 0.2f) / 0.1f, 0, 1);
            // float animFourthTenth = Mth.clamp((value - 0.3f) / 0.1f, 0, 1);
            // float animFifthTenth = Mth.clamp((value - 0.4f) / 0.1f, 0, 1);
            // float animSixthTenth = Mth.clamp((value - 0.5f) / 0.1f, 0, 1);
            // float animSeventhTenth = Mth.clamp((value - 0.6f) / 0.1f, 0, 1);
            // float animEighthTenth = Mth.clamp((value - 0.7f) / 0.1f, 0, 1);
            // float animNinthTenth = Mth.clamp((value - 0.8f) / 0.1f, 0, 1);
            // float animTenthTenth = Mth.clamp((value - 0.9f) / 0.1f, 0, 1);

            float movementT;
            float movementC;
            float movementB;
            Vec3 moveOffsetT;
            Vec3 moveOffsetC;
            Vec3 moveOffsetB;

            // if (exponentialValue <= 0.25) {
            // movementMMain = (float) (0 / 16f + 0.25 / 16f * animFirstQuarter); // Main
            // movement
            // movementMUp = (float) (0 / 16f - 2 / 16f * animFirstQuarter); // Upward
            // movement
            // Vec3 moveOffsetM =
            // Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementMMain);
            // movementM = new Vec3(moveOffsetM.x, movementMUp, moveOffsetM.z);
            // } else if (exponentialValue > 0.25 && exponentialValue <= 0.5) {
            // movementMMain = (float) (0.25 / 16f + 0.75 / 16f * animSecondQuarter);
            // movementMUp = (float) (-2 / 16f - 1.5 / 16f * animSecondQuarter);
            // Vec3 moveOffsetM =
            // Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementMMain);
            // movementM = new Vec3(moveOffsetM.x, movementMUp, moveOffsetM.z);
            // } else if (exponentialValue > 0.5 && exponentialValue <= 0.75) {
            // movementMMain = (float) (1 / 16f + 1.25 / 16f * animThirdQuarter);
            // movementMUp = (float) (-3.5 / 16f - 1 / 16f * animThirdQuarter);
            // Vec3 moveOffsetM =
            // Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementMMain);
            // movementM = new Vec3(moveOffsetM.x, movementMUp, moveOffsetM.z);
            // } else {
            // movementMMain = (float) (2.25 / 16f + 2.75 / 16f * animFourthQuarter);
            // movementMUp = (float) (-4.5 / 16f - 0.5 / 16f * animFourthQuarter);
            // Vec3 moveOffsetM =
            // Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementMMain);
            // movementM = new Vec3(moveOffsetM.x, movementMUp, moveOffsetM.z);
            // }

            float valueFromThirdTenth = Mth.clamp((exponentialValue - 0.3f) / 0.7f, 0, 1);
            float valueFromSeventhTenth = Mth.clamp((exponentialValue - 0.7f) / 0.3f, 0, 1);
            movementB = 13 / 16f * exponentialValue * f;
            movementC = 9 / 16f * valueFromThirdTenth * f; // Clamp value to avoid unexpected results
            movementT = 4 / 16f * valueFromSeventhTenth * f; // Clamp value to avoid unexpected results
            moveOffsetB = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementB);
            moveOffsetC = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementC);
            moveOffsetT = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementT);

            for (Direction face : Iterate.directions) {

                ConnectedTextureBehaviour.CTContext context = behaviour.buildContext(world, pos, blockState, face,
                        dataType.getContextRequirement());

                int textureIndex = dataType.getTextureIndex(context);
                if (facing == Direction.EAST && face != Direction.UP && face != Direction.DOWN)
                    face = face.getCounterClockWise();
                if (facing == Direction.SOUTH && face != Direction.UP && face != Direction.DOWN)
                    face = face.getOpposite();
                if (facing == Direction.WEST && face != Direction.UP && face != Direction.DOWN)
                    face = face.getClockWise();

                ResourceLocation resourceLocation = CreateTrainParts.asResource(
                        blockTexturePath + "/" + facing.getSerializedName() + "_" + connectedState.getSerializedName()
                                + "_" + face.getSerializedName());

                // System.out.println("Resource Location: " + resourceLocation);

                PartialModel top = AllPartialModels.TRAIN_SLIDE_TOP.get(resourceLocation);
                PartialModel centre = AllPartialModels.TRAIN_SLIDE_CENTRE.get(resourceLocation);
                PartialModel block = AllPartialModels.TRAIN_SLIDE.get(resourceLocation);

                PartialModel bottom = AllPartialModels.TRAIN_SLIDE_BOTTOM.get(resourceLocation);
                // PartialModel pivot = AllPartialModels.TRAIN_STEP_PIVOT.get(resourceLocation);

                SuperByteBuffer partial_block = CachedBuffers.partial(block, blockState);
                CTSpriteShiftEntry spriteShift = null;
                // System.out.println("Block Texture Path: " + blockTexturePath);
                if (blockTexturePath.equals("train_slide_andesite")) {
                    spriteShift = AllSpriteShifts.ANDESITE_CASING;
                    // System.out.println("Using andesite casing texture");
                } else if (blockTexturePath.equals("train_slide_brass")) {
                    spriteShift = AllSpriteShifts.BRASS_CASING;
                    // System.out.println("Using brass casing texture");
                } else if (blockTexturePath.equals("train_slide_copper")) {
                    spriteShift = AllSpriteShifts.COPPER_CASING;
                    // System.out.println("Using copper casing texture");
                }

                // ...existing code...
                else if (blockTexturePath.equals("train_slide_train")) {
                    if (face == Direction.UP || face == Direction.DOWN) {
                        spriteShift = com.tiestoettoet.create_train_parts.AllSpriteShifts.TRAIN_STEP_TRAIN;
                    } else {
                        spriteShift = com.tiestoettoet.create_train_parts.AllSpriteShifts.TRAIN_STEP_SIDE;
                    }
                } else {
                    // System.out.println("Unknown block texture path: " + blockTexturePath);
                    return;
                }

                if (spriteShift == null) {
                    // System.out.println("Sprite shift is null, using fallback texture.");
                    return;
                }
                // System.out.println("TextureIndex: " + textureIndex + ", face: " + face +
                // ",facing" + facing
                // + ", connectedState: " + connectedState);

                SuperByteBuffer partial_bottom = CachedBuffers.partial(bottom, blockState);
                // SuperByteBuffer partial_pivot = CachedBuffers.partial(pivot, blockState);
                SuperByteBuffer partial_top = CachedBuffers.partial(top, blockState);
                SuperByteBuffer partial_centre = CachedBuffers.partial(centre, blockState);
                float row = Math.floorDiv(textureIndex, 8);
                float column = textureIndex % 8;
                float u = (column) / 8f;
                float v = (row) / 8f;

                int blockLight = getSafeLight(world, pos, light);

                partial_block
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        // .shiftUV(connectedShift)
                        .light(blockLight)
                        .renderInto(ms, vb);

                BlockPos centrePos = pos.offset((int) Math.round(moveOffsetC.x), (int) Math.round(moveOffsetC.y),
                        (int) Math.round(moveOffsetC.z));
                int centreLight = getSafeLight(world, centrePos, light);

                partial_centre.translate(moveOffsetC.x, moveOffsetC.y, moveOffsetC.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        // .rotateXDegrees(rotation)
                        .light(centreLight)
                        .renderInto(ms, vb);

                BlockPos topPos = pos.offset((int) Math.round(moveOffsetT.x), (int) Math.round(moveOffsetT.y),
                        (int) Math.round(moveOffsetT.z));
                int topLight = getSafeLight(world, topPos, light);

                partial_top.translate(moveOffsetT.x, moveOffsetT.y, moveOffsetT.z)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .light(topLight)
                        .renderInto(ms, vb);

                // if (facing == Direction.EAST || facing == Direction.WEST)
                // if (face == Direction.UP) {
                // context = behaviour.buildContext(world, pos, blockState, facing,
                // dataType.getContextRequirement());
                //
                // textureIndex = dataType.getTextureIndex(context);
                // row = Math.floorDiv(textureIndex, 8);
                // column = textureIndex % 8;
                // u = (column) / 8f;
                // v = (row) / 8f;
                // }

                BlockPos bottomPos = pos.offset((int) Math.round(moveOffsetB.x), (int) Math.round(moveOffsetB.y),
                        (int) Math.round(moveOffsetB.z));
                int bottomLight = getSafeLight(world, bottomPos, light);

                partial_bottom.translate(moveOffsetB.x, moveOffsetB.y, moveOffsetB.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(bottomLight)
                        .renderInto(ms, vb);
                // if (facing == Direction.EAST || facing == Direction.WEST)
                // if (face.getOpposite() == facing) {
                // context = behaviour.buildContext(world, pos, blockState, facing,
                // dataType.getContextRequirement());
                //
                // textureIndex = dataType.getTextureIndex(context);
                // row = Math.floorDiv(textureIndex, 8);
                // column = textureIndex % 8;
                // u = (column) / 8f;
                // v = (row) / 8f;
                // }
                // partial_pivot.translate(moveOffsetP.x, 15.5 / 16f, moveOffsetP.z)
                // .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                // .shiftUVtoSheet(spriteShift, u, v, 8)
                // .rotateXDegrees(rotationPivot)
                // .light(light)
                // .renderInto(ms, vb);

            }
            // check if movementDirection is going north or south

        }*/
    }

    public static void renderTrainSlide(BlockState state, BlockPos pos, Level world, Level renderWorld, float animValue, PoseStack ms, MultiBufferSource buffer,
                                 int light, ContraptionMatrices matrices) {

        if (!(state.getBlock() instanceof TrainSlideBlock))
            return;

        ConnectedTextureBehaviour behaviour = new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING);

        Direction facing = state.getValue(TrainSlideBlock.FACING);
        CTType dataType = behaviour.getDataType(world, pos, state, facing);

        if (dataType == null) {
            return;
        }
        float rotationAngle = switch (facing) {
            case NORTH -> 0; // No rotation needed
            case SOUTH -> 180;
            case WEST -> 90;
            case EAST -> -90;
            default -> 0;
        };

        float exponentialValue = animValue * animValue;
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        ResourceLocation blockTexture = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String blockTexturePath = blockTexture.getPath();
        TrainSlideBlock.ConnectedState connectedState = state.getValue(TrainSlideBlock.CONNECTED);

        float f = state.getValue(TrainSlideBlock.OPEN) ? -1 : 1;
        float movement = 5 / 16f * exponentialValue * f; // Clamp value to avoid unexpected results
        Direction movementDirection = state.getValue(TrainSlideBlock.OPEN) ? facing.getOpposite() : facing;
        Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movement);
        float movementP = (float) (10.5 / 16f);
        Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(movementP);

        float movementT;
        float movementC;
        float movementB;
        Vec3 moveOffsetT;
        Vec3 moveOffsetC;
        Vec3 moveOffsetB;

        float valueFromThirdTenth = Mth.clamp((exponentialValue - 0.3f) / 0.7f, 0, 1);
        float valueFromSeventhTenth = Mth.clamp((exponentialValue - 0.7f) / 0.3f, 0, 1);
        movementB = 13 / 16f * exponentialValue * f;
        movementC = 9 / 16f * valueFromThirdTenth * f; // Clamp value to avoid unexpected results
        movementT = 4 / 16f * valueFromSeventhTenth * f; // Clamp value to avoid unexpected results
        moveOffsetB = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementB);
        moveOffsetC = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementC);
        moveOffsetT = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale(movementT);

        for (Direction face : Iterate.directions) {

            ConnectedTextureBehaviour.CTContext context = behaviour.buildContext(world, pos, state, face,
                    dataType.getContextRequirement());

            int textureIndex = dataType.getTextureIndex(context);
            if (facing == Direction.EAST && face != Direction.UP && face != Direction.DOWN)
                face = face.getCounterClockWise();
            if (facing == Direction.SOUTH && face != Direction.UP && face != Direction.DOWN)
                face = face.getOpposite();
            if (facing == Direction.WEST && face != Direction.UP && face != Direction.DOWN)
                face = face.getClockWise();

            ResourceLocation resourceLocation = CreateTrainParts.asResource(
                    blockTexturePath + "/" + facing.getSerializedName() + "_" + connectedState.getSerializedName()
                            + "_" + face.getSerializedName());


            PartialModel top = AllPartialModels.TRAIN_SLIDE_TOP.get(resourceLocation);
            PartialModel centre = AllPartialModels.TRAIN_SLIDE_CENTRE.get(resourceLocation);
            PartialModel block = AllPartialModels.TRAIN_SLIDE.get(resourceLocation);

            PartialModel bottom = AllPartialModels.TRAIN_SLIDE_BOTTOM.get(resourceLocation);

            SuperByteBuffer partial_block = CachedBuffers.partial(block, state);
            CTSpriteShiftEntry spriteShift;
            switch (blockTexturePath) {
                case "train_slide_andesite" -> spriteShift = AllSpriteShifts.ANDESITE_CASING;
                case "train_slide_brass" -> spriteShift = AllSpriteShifts.BRASS_CASING;
                case "train_slide_copper" -> spriteShift = AllSpriteShifts.COPPER_CASING;
                case "train_slide_train" -> {
                    if (face == Direction.UP || face == Direction.DOWN) {
                        spriteShift = com.tiestoettoet.create_train_parts.AllSpriteShifts.TRAIN_STEP_TRAIN;
                    } else {
                        spriteShift = com.tiestoettoet.create_train_parts.AllSpriteShifts.TRAIN_STEP_SIDE;
                    }
                }
                default -> {
                    return;
                }
            }

            if (spriteShift == null) {
                return;
            }

            SuperByteBuffer partial_bottom = CachedBuffers.partial(bottom, state);
            SuperByteBuffer partial_top = CachedBuffers.partial(top, state);
            SuperByteBuffer partial_centre = CachedBuffers.partial(centre, state);
            float row = Math.floorDiv(textureIndex, 8);
            float column = textureIndex % 8;
            float u = (column) / 8f;
            float v = (row) / 8f;

            int blockLight = getSafeLight(world, pos, light);

            if (world instanceof ContraptionWorld) {
                partial_block
                        .transform(ms)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(blockLight)
                        .useLevelLight(world, matrices.getWorld())
                        .renderInto(matrices.getViewProjection(), vb);
            } else {
                partial_block
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(blockLight)
                        .renderInto(ms, vb);
            }

            BlockPos centrePos = pos.offset((int) Math.round(moveOffsetC.x), (int) Math.round(moveOffsetC.y),
                    (int) Math.round(moveOffsetC.z));
            int centreLight = getSafeLight(world, centrePos, light);

            if (world instanceof ContraptionWorld) {
                partial_centre
                        .transform(ms)
                        .translate(moveOffsetC.x, moveOffsetC.y, moveOffsetC.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(centreLight)
                        .useLevelLight(world, matrices.getWorld())
                        .renderInto(matrices.getViewProjection(), vb);
            } else {
                partial_centre.translate(moveOffsetC.x, moveOffsetC.y, moveOffsetC.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(centreLight)
                        .renderInto(ms, vb);
            }

            BlockPos topPos = pos.offset((int) Math.round(moveOffsetT.x), (int) Math.round(moveOffsetT.y),
                    (int) Math.round(moveOffsetT.z));
            int topLight = getSafeLight(world, topPos, light);

            if (world instanceof ContraptionWorld) {
                partial_top
                        .transform(ms)
                        .translate(moveOffsetT.x, moveOffsetT.y, moveOffsetT.z)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .light(topLight)
                        .useLevelLight(world, matrices.getWorld())
                        .renderInto(matrices.getViewProjection(), vb);
            } else {
                partial_top.translate(moveOffsetT.x, moveOffsetT.y, moveOffsetT.z)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .light(topLight)
                        .renderInto(ms, vb);
            }

            BlockPos bottomPos = pos.offset((int) Math.round(moveOffsetB.x), (int) Math.round(moveOffsetB.y),
                    (int) Math.round(moveOffsetB.z));
            int bottomLight = getSafeLight(world, bottomPos, light);

            if (world instanceof ContraptionWorld) {
                partial_bottom
                        .transform(ms)
                        .translate(moveOffsetB.x, moveOffsetB.y, moveOffsetB.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(bottomLight)
                        .useLevelLight(world, matrices.getWorld())
                        .renderInto(matrices.getViewProjection(), vb);
            } else {
                partial_bottom.translate(moveOffsetB.x, moveOffsetB.y, moveOffsetB.z)
                        .rotateCentered(Mth.DEG_TO_RAD * rotationAngle, Direction.Axis.Y)
                        .shiftUVtoSheet(spriteShift, u, v, 8)
                        .light(bottomLight)
                        .renderInto(ms, vb);
            }

        }
    }

    public static int getSafeLight(BlockAndTintGetter world, BlockPos targetPos, int fallbackLight) {
        // Check if the position is valid (within world bounds)
        if (targetPos.getY() < world.getMinBuildHeight() || targetPos.getY() > world.getMaxBuildHeight()) {
            return fallbackLight;
        }

        int calculatedLight = LevelRenderer.getLightColor(world, targetPos);

        // If the calculated light is 0 (completely dark), use fallback
        if (calculatedLight == 0) {
            // Try the position above to see if that has better lighting
            BlockPos abovePos = targetPos.above();
            int aboveLight = LevelRenderer.getLightColor(world, abovePos);
            if (aboveLight > 0) {
                return aboveLight;
            }

            // If still 0, return the original fallback light
            return fallbackLight;
        }

        return calculatedLight;
    }
}
