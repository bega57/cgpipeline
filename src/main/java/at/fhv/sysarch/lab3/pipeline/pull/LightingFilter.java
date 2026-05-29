package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class LightingFilter extends PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Vec3 lightPos;

    public LightingFilter(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> data = predecessor.pull();
        Face face = data.fst();
        Color color = data.snd();

        Vec3 vertexPos = face.getV1().toVec3();
        Vec3 lightDir = lightPos.subtract(vertexPos).getUnitVector();
        Vec3 normal = face.getN1().toVec3().getUnitVector();

        float diffuse = Math.max(0, normal.dot(lightDir));

        Color shaded = new Color(
            color.getRed() * diffuse,
            color.getGreen() * diffuse,
            color.getBlue() * diffuse,
            color.getOpacity()
        );

        return new Pair<>(face, shaded);
    }
}
