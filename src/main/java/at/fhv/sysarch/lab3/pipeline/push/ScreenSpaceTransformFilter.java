package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ScreenSpaceTransformFilter extends PushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 viewportTransform;

    public ScreenSpaceTransformFilter(Mat4 viewportTransform) {
        this.viewportTransform = viewportTransform;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();

        // perspective division: divide by w
        Vec4 ndc1 = perspDiv(face.getV1());
        Vec4 ndc2 = perspDiv(face.getV2());
        Vec4 ndc3 = perspDiv(face.getV3());

        // viewport transform to screen coordinates
        Vec4 sv1 = viewportTransform.multiply(ndc1);
        Vec4 sv2 = viewportTransform.multiply(ndc2);
        Vec4 sv3 = viewportTransform.multiply(ndc3);

        Face screen = new Face(sv1, sv2, sv3, face);
        successor.push(new Pair<>(screen, data.snd()));
    }

    private Vec4 perspDiv(Vec4 v) {
        float w = v.getW();
        return new Vec4(v.getX() / w, v.getY() / w, v.getZ() / w, 1.0f);
    }
}
