package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;

public class BackfaceCullingFilter extends PushFilter<Face, Face> {
    @Override
    public void push(Face face) {
        // in view space, camera is at origin
        // if vertex dot normal > 0, the face points away from us
        float dot = face.getV1().dot(face.getN1());
        if (dot <= 0) {
            successor.push(face);
        }
    }
}
