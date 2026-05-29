package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class LightingFilter extends PushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Vec3 lightPos;

    public LightingFilter(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();
        Color color = data.snd();

        // direction from vertex to light, normalized
        Vec3 vertexPos = face.getV1().toVec3();
        Vec3 lightDir = lightPos.subtract(vertexPos).getUnitVector();

        // face normal, normalized
        Vec3 normal = face.getN1().toVec3().getUnitVector();

        // diffuse factor: cos of angle between normal and light direction
        float diffuse = Math.max(0, normal.dot(lightDir));

        Color shadedColor = new Color(
            color.getRed() * diffuse,
            color.getGreen() * diffuse,
            color.getBlue() * diffuse,
            color.getOpacity()
        );

        successor.push(new Pair<>(face, shadedColor));
    }
}
