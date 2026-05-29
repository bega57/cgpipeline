package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ModelViewTransformFilter extends PullFilter<Face, Face> {
    private Mat4 modelViewMatrix;

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    @Override
    public Face pull() {
        Face face = predecessor.pull();

        Vec4 tv1 = modelViewMatrix.multiply(face.getV1());
        Vec4 tv2 = modelViewMatrix.multiply(face.getV2());
        Vec4 tv3 = modelViewMatrix.multiply(face.getV3());

        // normals have w=0, translation is automatically ignored
        Vec4 tn1 = modelViewMatrix.multiply(face.getN1());
        Vec4 tn2 = modelViewMatrix.multiply(face.getN2());
        Vec4 tn3 = modelViewMatrix.multiply(face.getN3());

        return new Face(tv1, tv2, tv3, tn1, tn2, tn3);
    }
}
