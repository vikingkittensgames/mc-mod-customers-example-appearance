package com.example.customers.appearance.client;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

public final class BobberModel<T extends Mob>
        extends EntityModel<T> {
    public static final float BOB_SPEED = 0.1F;
    public static final float BOB_QUARTER_PERIOD =
            (float)(Math.PI / 2.0) / BOB_SPEED;
    private static final float BOB_AMPLITUDE = 0.25F;
    private static final float TENTACLE_SPEED = 0.15F;
    private static final float TENTACLE_AMPLITUDE =
            (float)Math.toRadians(20.0);
    private static final float TENTACLE_PHASE_STEP =
            (float)(Math.PI * 2.0 / 6.0);
    private static final String BODY = "body";
    private static final String TENTACLE_PREFIX = "tentacle";
    private static final List<PerFaceCube> BODY_CUBES =
            createBodyCubes();
    private static final List<PerFaceCube> TENTACLE_CUBES =
            createTentacleCubes();

    private final ModelPart body;
    private final List<ModelPart> tentacles;
    private final Variant variant;

    public BobberModel(
            ModelPart root,
            Variant variant
    ) {
        body = root.getChild(BODY);
        tentacles = new ArrayList<>(6);
        for (int index = 0; index < 6; index++) {
            tentacles.add(body.getChild(
                    TENTACLE_PREFIX + (index + 1)
            ));
        }
        this.variant = variant;
    }

    public static LayerDefinition createBodyLayer(
            Variant variant
    ) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
                BODY,
                CubeListBuilder.create(),
                PartPose.ZERO
        );

        for (int index = 0;
                index < variant.tentacles().size();
                index++) {
            Variant.Tentacle tentacle =
                    variant.tentacles().get(index);
            body.addOrReplaceChild(
                    TENTACLE_PREFIX + (index + 1),
                    CubeListBuilder.create(),
                    PartPose.offsetAndRotation(
                            tentacle.pivotX() - 8.0F,
                            5.0F - tentacle.pivotY(),
                            tentacle.pivotZ() - 8.0F,
                            variant.baseTentacleRotation(),
                            0.0F,
                            0.0F
                    )
            );
        }

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        body.y = -bobOffset(ageInTicks) * 16.0F;
        body.yRot = lookRotation(netHeadYaw);
        for (int index = 0;
                index < tentacles.size();
                index++) {
            tentacles.get(index).xRot = tentacleRotation(
                    variant.baseTentacleRotation(),
                    ageInTicks,
                    index
            );
        }
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        poseStack.pushPose();
        body.translateAndRotate(poseStack);
        for (PerFaceCube cube : BODY_CUBES) {
            cube.render(
                    poseStack,
                    vertexConsumer,
                    packedLight,
                    packedOverlay,
                    color
            );
        }
        for (int index = 0; index < tentacles.size(); index++) {
            poseStack.pushPose();
            tentacles.get(index).translateAndRotate(poseStack);
            TENTACLE_CUBES.get(index).render(
                    poseStack,
                    vertexConsumer,
                    packedLight,
                    packedOverlay,
                    color
            );
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    static float bobOffset(float ageInTicks) {
        return (float)Math.sin(ageInTicks * BOB_SPEED)
                * BOB_AMPLITUDE;
    }

    static float tentacleRotation(
            float baseRotation,
            float ageInTicks,
            int tentacleIndex
    ) {
        float phase =
                tentacleIndex * TENTACLE_PHASE_STEP;
        return baseRotation
                + (float)Math.sin(
                        ageInTicks * TENTACLE_SPEED
                                + phase
                ) * TENTACLE_AMPLITUDE;
    }

    static float lookRotation(float netHeadYaw) {
        return (float)Math.toRadians(netHeadYaw);
    }

    static FaceUv mainCubeUv(Direction direction) {
        return BODY_CUBES.get(0).uv(direction);
    }

    static FaceUv tentacleCubeUv(
            int index,
            Direction direction
    ) {
        return TENTACLE_CUBES.get(index).uv(direction);
    }

    static float bodyCubeXRotation(int index) {
        return BODY_CUBES.get(index).xRotation();
    }

    private static List<PerFaceCube> createBodyCubes() {
        return List.of(
                cube(-4, -3, -5, 4, 3, 2,
                        f(8, 0, 16, 6), f(8, 12, 15, 18),
                        f(8, 6, 16, 12), f(0, 14, 7, 20),
                        f(8, 7, 0, 0), f(8, 7, 0, 14)),
                cube(-1, -1, -7, 1, 1, -6,
                        f(15, 22, 17, 24), f(22, 19, 23, 21),
                        f(17, 22, 19, 24), f(22, 21, 23, 23),
                        f(9, 24, 7, 23), f(11, 23, 9, 24)),
                cube(-2, -2, -6, 2, 2, -5,
                        f(7, 18, 11, 22), f(7, 14, 8, 18),
                        f(11, 18, 15, 22), f(21, 19, 22, 23),
                        f(11, 23, 7, 22), f(15, 22, 11, 23)),
                cube(-2, -4, -4, 2, -3, 1,
                        f(21, 15, 25, 16), f(21, 7, 26, 8),
                        f(21, 16, 25, 17), f(21, 12, 26, 13),
                        f(19, 17, 15, 12), f(20, 0, 16, 5))
                        .rotatedX(),
                cube(-2, 3, -4, 2, 4, 1,
                        f(21, 17, 25, 18), f(21, 13, 26, 14),
                        f(21, 18, 25, 19), f(21, 14, 26, 15),
                        f(20, 10, 16, 5), f(19, 17, 15, 22))
                        .rotatedX()
        );
    }

    private static List<PerFaceCube> createTentacleCubes() {
        return List.of(
                tentacleCube(
                        f(23, 10, 24, 11), f(16, 10, 23, 11),
                        f(11, 23, 12, 24), f(16, 11, 23, 12),
                        f(20, 19, 19, 12), f(20, 19, 19, 26)),
                tentacleCube(
                        f(23, 11, 24, 12), f(0, 20, 7, 21),
                        f(12, 23, 13, 24), f(20, 0, 27, 1),
                        f(21, 8, 20, 1), f(21, 12, 20, 19)),
                tentacleCube(
                        f(13, 23, 14, 24), f(20, 8, 27, 9),
                        f(14, 23, 15, 24), f(20, 9, 27, 10),
                        f(21, 26, 20, 19), f(1, 21, 0, 28)),
                tentacleCube(
                        f(23, 19, 24, 20), f(21, 1, 28, 2),
                        f(23, 20, 24, 21), f(21, 2, 28, 3),
                        f(2, 28, 1, 21), f(3, 21, 2, 28)),
                tentacleCube(
                        f(21, 23, 22, 24), f(21, 3, 28, 4),
                        f(23, 21, 24, 22), f(21, 4, 28, 5),
                        f(4, 28, 3, 21), f(5, 21, 4, 28)),
                tentacleCube(
                        f(22, 23, 23, 24), f(21, 5, 28, 6),
                        f(23, 22, 24, 23), f(21, 6, 28, 7),
                        f(6, 28, 5, 21), f(7, 21, 6, 28))
        );
    }

    private static PerFaceCube tentacleCube(
            FaceUv north,
            FaceUv east,
            FaceUv south,
            FaceUv west,
            FaceUv up,
            FaceUv down
    ) {
        return cube(
                0, -1, 0, 1, 0, 7,
                north, east, south, west, up, down
        );
    }

    private static PerFaceCube cube(
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            FaceUv north,
            FaceUv east,
            FaceUv south,
            FaceUv west,
            FaceUv up,
            FaceUv down
    ) {
        Map<Direction, FaceUv> uvs =
                new EnumMap<>(Direction.class);
        uvs.put(Direction.NORTH, north);
        uvs.put(Direction.EAST, east);
        uvs.put(Direction.SOUTH, south);
        uvs.put(Direction.WEST, west);
        uvs.put(Direction.UP, up);
        uvs.put(Direction.DOWN, down);
        return new PerFaceCube(
                minX, minY, minZ,
                maxX, maxY, maxZ,
                uvs
        );
    }

    private static FaceUv f(
            float u1,
            float v1,
            float u2,
            float v2
    ) {
        return new FaceUv(u1, v1, u2, v2);
    }

    public enum Variant {
        NORMAL(
                "textures/entity/bobber-customer-normal.png",
                0.0F,
                List.of(
                        tentacle(10, 6, 9),
                        tentacle(8, 6, 9),
                        tentacle(6, 6, 9),
                        tentacle(9, 4, 9),
                        tentacle(7, 4, 9),
                        tentacle(5, 4, 9)
                )
        ),
        IMPATIENT(
                "textures/entity/bobber-customer-impatient.png",
                (float)Math.toRadians(45.0),
                List.of(
                        tentacle(10, 6, 8),
                        tentacle(8, 6, 8),
                        tentacle(6, 6, 8),
                        tentacle(9, 4, 8),
                        tentacle(7, 4, 8),
                        tentacle(5, 4, 8)
                )
        ),
        CASUAL(
                "textures/entity/bobber-customer-casual.png",
                (float)Math.toRadians(-45.0),
                List.of(
                        tentacle(10, 4, 9),
                        tentacle(8, 4, 9),
                        tentacle(6, 4, 9),
                        tentacle(9, 2, 9),
                        tentacle(7, 2, 9),
                        tentacle(5, 2, 9)
                )
        );

        private final String texturePath;
        private final float baseTentacleRotation;
        private final List<Tentacle> tentacles;

        Variant(
                String texturePath,
                float baseTentacleRotation,
                List<Tentacle> tentacles
        ) {
            this.texturePath = texturePath;
            this.baseTentacleRotation =
                    baseTentacleRotation;
            this.tentacles = tentacles;
        }

        public String texturePath() {
            return texturePath;
        }

        public float baseTentacleRotation() {
            return baseTentacleRotation;
        }

        public List<Tentacle> tentacles() {
            return tentacles;
        }

        public static Variant fromType(
                CustomersVillagerType type
        ) {
            return switch (type) {
                case CUSTOMER_NORMAL -> NORMAL;
                case CUSTOMER_IMPATIENT -> IMPATIENT;
                case CUSTOMER_CASUAL -> CASUAL;
                case SUPPLIER -> throw new IllegalArgumentException(
                        "Bobbers does not define a Supplier model"
                );
            };
        }

        private static Tentacle tentacle(
                float x,
                float y,
                float z
        ) {
            return new Tentacle(
                    x,
                    y,
                    z,
                    x,
                    y,
                    z
            );
        }

        public record Tentacle(
                float x,
                float y,
                float z,
                float pivotX,
                float pivotY,
                float pivotZ
        ) {
        }
    }

    public record FaceUv(
            float u1,
            float v1,
            float u2,
            float v2
    ) {}

    private record Point(float x, float y, float z) {}

    private record Face(
            Direction direction,
            FaceUv uv,
            List<Point> vertices
    ) {}

    private static final class PerFaceCube {
        private static final float TEXTURE_SIZE = 32.0F;
        private final Map<Direction, Face> faces;
        private final float centerX;
        private final float centerY;
        private final float centerZ;
        private float xRotation;

        PerFaceCube(
                float minX,
                float minY,
                float minZ,
                float maxX,
                float maxY,
                float maxZ,
                Map<Direction, FaceUv> uvs
        ) {
            Map<Direction, Face> cubeFaces =
                    new EnumMap<>(Direction.class);
            cubeFaces.put(
                    Direction.DOWN,
                    face(
                            Direction.DOWN,
                            uvs,
                            point(maxX, minY, maxZ),
                            point(minX, minY, maxZ),
                            point(minX, minY, minZ),
                            point(maxX, minY, minZ)
                    )
            );
            cubeFaces.put(
                    Direction.UP,
                    face(
                            Direction.UP,
                            uvs,
                            point(maxX, maxY, minZ),
                            point(minX, maxY, minZ),
                            point(minX, maxY, maxZ),
                            point(maxX, maxY, maxZ)
                    )
            );
            cubeFaces.put(
                    Direction.WEST,
                    face(
                            Direction.WEST,
                            uvs,
                            point(minX, minY, minZ),
                            point(minX, minY, maxZ),
                            point(minX, maxY, maxZ),
                            point(minX, maxY, minZ)
                    )
            );
            cubeFaces.put(
                    Direction.NORTH,
                    face(
                            Direction.NORTH,
                            uvs,
                            point(maxX, minY, minZ),
                            point(minX, minY, minZ),
                            point(minX, maxY, minZ),
                            point(maxX, maxY, minZ)
                    )
            );
            cubeFaces.put(
                    Direction.EAST,
                    face(
                            Direction.EAST,
                            uvs,
                            point(maxX, minY, maxZ),
                            point(maxX, minY, minZ),
                            point(maxX, maxY, minZ),
                            point(maxX, maxY, maxZ)
                    )
            );
            cubeFaces.put(
                    Direction.SOUTH,
                    face(
                            Direction.SOUTH,
                            uvs,
                            point(minX, minY, maxZ),
                            point(maxX, minY, maxZ),
                            point(maxX, maxY, maxZ),
                            point(minX, maxY, maxZ)
                    )
            );
            faces = Map.copyOf(cubeFaces);
            centerX = (minX + maxX) / 2.0F;
            centerY = (minY + maxY) / 2.0F;
            centerZ = (minZ + maxZ) / 2.0F;
        }

        void render(
                PoseStack poseStack,
                VertexConsumer buffer,
                int packedLight,
                int packedOverlay,
                int color
        ) {
            poseStack.pushPose();
            poseStack.translate(
                    centerX / 16.0F,
                    centerY / 16.0F,
                    centerZ / 16.0F
            );
            poseStack.mulPose(Axis.XP.rotation(xRotation));
            poseStack.translate(
                    -centerX / 16.0F,
                    -centerY / 16.0F,
                    -centerZ / 16.0F
            );
            for (Face face : faces.values()) {
                renderFace(
                        poseStack.last(),
                        buffer,
                        packedLight,
                        packedOverlay,
                        color,
                        face
                );
            }
            poseStack.popPose();
        }

        FaceUv uv(Direction direction) {
            return faces.get(direction).uv();
        }

        float xRotation() {
            return xRotation;
        }

        PerFaceCube rotatedX() {
            xRotation = (float)Math.PI;
            return this;
        }

        private static void renderFace(
                PoseStack.Pose pose,
                VertexConsumer buffer,
                int packedLight,
                int packedOverlay,
                int color,
                Face face
        ) {
            Matrix4f matrix = pose.pose();
            Vector3f normal = pose.transformNormal(
                    face.direction().step(),
                    new Vector3f()
            );
            FaceUv uv = face.uv();
            vertex(
                    matrix, buffer, face.vertices().get(0),
                    uv.u2(), uv.v1(), color,
                    packedOverlay, packedLight, normal
            );
            vertex(
                    matrix, buffer, face.vertices().get(1),
                    uv.u1(), uv.v1(), color,
                    packedOverlay, packedLight, normal
            );
            vertex(
                    matrix, buffer, face.vertices().get(2),
                    uv.u1(), uv.v2(), color,
                    packedOverlay, packedLight, normal
            );
            vertex(
                    matrix, buffer, face.vertices().get(3),
                    uv.u2(), uv.v2(), color,
                    packedOverlay, packedLight, normal
            );
        }

        private static void vertex(
                Matrix4f matrix,
                VertexConsumer buffer,
                Point point,
                float u,
                float v,
                int color,
                int packedOverlay,
                int packedLight,
                Vector3f normal
        ) {
            Vector3f position = matrix.transformPosition(
                    point.x() / 16.0F,
                    point.y() / 16.0F,
                    point.z() / 16.0F,
                    new Vector3f()
            );
            buffer.addVertex(
                    position.x(),
                    position.y(),
                    position.z(),
                    color,
                    u / TEXTURE_SIZE,
                    v / TEXTURE_SIZE,
                    packedOverlay,
                    packedLight,
                    normal.x(),
                    normal.y(),
                    normal.z()
            );
        }

        private static Face face(
                Direction direction,
                Map<Direction, FaceUv> uvs,
                Point first,
                Point second,
                Point third,
                Point fourth
        ) {
            return new Face(
                    direction,
                    uvs.get(direction),
                    List.of(first, second, third, fourth)
            );
        }

        private static Point point(
                float x,
                float y,
                float z
        ) {
            return new Point(x, y, z);
        }
    }
}
