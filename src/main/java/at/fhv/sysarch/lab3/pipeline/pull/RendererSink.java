package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RendererSink {
    private final GraphicsContext gc;
    private final RenderingMode mode;
    private PullPipe<Pair<Face, Color>> source;

    public RendererSink(GraphicsContext gc, RenderingMode mode) {
        this.gc = gc;
        this.mode = mode;
    }

    public void setSource(PullPipe<Pair<Face, Color>> source) {
        this.source = source;
    }

    // called each frame to pull and render all faces
    public void render() {
        while (source.hasNext()) {
            Pair<Face, Color> data = source.pull();
            Face face = data.fst();
            Color color = data.snd();

            Vec2 s1 = face.getV1().toScreen();
            Vec2 s2 = face.getV2().toScreen();
            Vec2 s3 = face.getV3().toScreen();

            double[] xPoints = { s1.getX(), s2.getX(), s3.getX() };
            double[] yPoints = { s1.getY(), s2.getY(), s3.getY() };

            switch (mode) {
                case POINT:
                    gc.setStroke(color);
                    gc.strokeLine(s1.getX(), s1.getY(), s1.getX(), s1.getY());
                    gc.strokeLine(s2.getX(), s2.getY(), s2.getX(), s2.getY());
                    gc.strokeLine(s3.getX(), s3.getY(), s3.getX(), s3.getY());
                    break;
                case WIREFRAME:
                    gc.setStroke(color);
                    gc.strokePolygon(xPoints, yPoints, 3);
                    break;
                case FILLED:
                    gc.setFill(color);
                    gc.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }
    }
}
