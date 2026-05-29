package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionTransformFilter extends PushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 projTransform;

    public ProjectionTransformFilter(Mat4 projTransform) {
        this.projTransform = projTransform;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();

        Vec4 pv1 = projTransform.multiply(face.getV1());
        Vec4 pv2 = projTransform.multiply(face.getV2());
        Vec4 pv3 = projTransform.multiply(face.getV3());

        Face projected = new Face(pv1, pv2, pv3, face);
        successor.push(new Pair<>(projected, data.snd()));
    }
}
