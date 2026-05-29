package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import java.util.ArrayList;
import java.util.List;

public class DepthSortingFilter extends PushFilter<Face, Face> {
    private final List<Face> buffer = new ArrayList<>();

    @Override
    public void push(Face face) {
        buffer.add(face);
    }

    // called after all faces have been pushed for this frame
    public void flush() {
        // sort back-to-front (highest z first = farthest from camera)
        buffer.sort((a, b) -> Float.compare(avgZ(b), avgZ(a)));

        for (Face face : buffer) {
            successor.push(face);
        }
        buffer.clear();
    }

    private float avgZ(Face f) {
        return (f.getV1().getZ() + f.getV2().getZ() + f.getV3().getZ()) / 3.0f;
    }
}
