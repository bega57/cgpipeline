package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        // source: provides faces from the model
        ModelSource source = new ModelSource();

        // 1. model-view transformation
        ModelViewTransformFilter modelViewFilter = new ModelViewTransformFilter();
        modelViewFilter.setPredecessor(source);

        // 2. backface culling in view space
        BackfaceCullingFilter backfaceCullingFilter = new BackfaceCullingFilter();
        backfaceCullingFilter.setPredecessor(modelViewFilter);

        // 3. depth sorting: NOT possible in pull pipeline (can't collect all before sorting)

        // 4. coloring
        ColoringFilter coloringFilter = new ColoringFilter(pd.getModelColor());
        coloringFilter.setPredecessor(backfaceCullingFilter);

        // 5. projection transformation (with optional lighting before it)
        ProjectionTransformFilter projectionFilter = new ProjectionTransformFilter(pd.getProjTransform());

        if (pd.isPerformLighting()) {
            LightingFilter lightingFilter = new LightingFilter(pd.getLightPos());
            lightingFilter.setPredecessor(coloringFilter);
            projectionFilter.setPredecessor(lightingFilter);
        } else {
            projectionFilter.setPredecessor(coloringFilter);
        }

        // 6. perspective division + viewport
        ScreenSpaceTransformFilter screenSpaceFilter = new ScreenSpaceTransformFilter(pd.getViewportTransform());
        screenSpaceFilter.setPredecessor(projectionFilter);

        // 7. renderer sink
        RendererSink renderer = new RendererSink(pd.getGraphicsContext(), pd.getRenderingMode());
        renderer.setSource(screenSpaceFilter);

        return new AnimationRenderer(pd) {
            private float rotationAngle = 0;

            @Override
            protected void render(float fraction, Model model) {
                rotationAngle += fraction;

                Mat4 rotationMatrix = Matrices.rotate(rotationAngle, pd.getModelRotAxis());
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotationMatrix);
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                modelViewFilter.setModelViewMatrix(modelViewMatrix);

                // reset the source with faces for this frame
                source.setFaces(model.getFaces());

                // the sink pulls everything through the pipeline
                renderer.render();
            }
        };
    }
}
