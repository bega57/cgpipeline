package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class ColoringFilter extends PushFilter<Face, Pair<Face, Color>> {
    private final Color color;

    public ColoringFilter(Color color) {
        this.color = color;
    }

    @Override
    public void push(Face face) {
        successor.push(new Pair<>(face, color));
    }
}
