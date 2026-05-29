package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import java.util.Iterator;
import java.util.List;

public class ModelSource implements PullPipe<Face> {
    private Iterator<Face> iterator;

    // called each frame to reset the iterator
    public void setFaces(List<Face> faces) {
        this.iterator = faces.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    @Override
    public Face pull() {
        return iterator.next();
    }
}
