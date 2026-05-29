package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionTransformFilter extends PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 projTransform;

    public ProjectionTransformFilter(Mat4 projTransform) {
        this.projTransform = projTransform;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> data = predecessor.pull();
        Face face = data.fst();

        Vec4 pv1 = projTransform.multiply(face.getV1());
        Vec4 pv2 = projTransform.multiply(face.getV2());
        Vec4 pv3 = projTransform.multiply(face.getV3());

        return new Pair<>(new Face(pv1, pv2, pv3, face), data.snd());
    }
}
