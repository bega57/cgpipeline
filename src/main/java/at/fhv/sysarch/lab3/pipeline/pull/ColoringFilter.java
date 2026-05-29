package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class ColoringFilter extends PullFilter<Face, Pair<Face, Color>> {
    private final Color color;

    public ColoringFilter(Color color) {
        this.color = color;
    }

    @Override
    public Pair<Face, Color> pull() {
        return new Pair<>(predecessor.pull(), color);
    }
}
