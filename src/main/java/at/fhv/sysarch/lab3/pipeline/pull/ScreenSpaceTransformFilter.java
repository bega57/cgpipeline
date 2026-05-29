package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ScreenSpaceTransformFilter extends PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 viewportTransform;

    public ScreenSpaceTransformFilter(Mat4 viewportTransform) {
        this.viewportTransform = viewportTransform;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> data = predecessor.pull();
        Face face = data.fst();

        Vec4 sv1 = viewportTransform.multiply(perspDiv(face.getV1()));
        Vec4 sv2 = viewportTransform.multiply(perspDiv(face.getV2()));
        Vec4 sv3 = viewportTransform.multiply(perspDiv(face.getV3()));

        return new Pair<>(new Face(sv1, sv2, sv3, face), data.snd());
    }

    private Vec4 perspDiv(Vec4 v) {
        float w = v.getW();
        return new Vec4(v.getX() / w, v.getY() / w, v.getZ() / w, 1.0f);
    }
}
