package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;

public class BackfaceCullingFilter extends PullFilter<Face, Face> {
    private Face buffered;

    @Override
    public boolean hasNext() {
        if (buffered != null) return true;

        // search for the next visible face
        while (predecessor.hasNext()) {
            Face face = predecessor.pull();
            if (face.getV1().dot(face.getN1()) <= 0) {
                buffered = face;
                return true;
            }
        }
        return false;
    }

    @Override
    public Face pull() {
        if (buffered == null) hasNext();
        Face result = buffered;
        buffered = null;
        return result;
    }
}
