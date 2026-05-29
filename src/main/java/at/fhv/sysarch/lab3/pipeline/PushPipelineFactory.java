package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.push.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        // 1. model-view transformation
        ModelViewTransformFilter modelViewFilter = new ModelViewTransformFilter();

        // 2. backface culling in view space
        BackfaceCullingFilter backfaceCullingFilter = new BackfaceCullingFilter();

        // 3. depth sorting in view space (only possible in push pipeline)
        DepthSortingFilter depthSortingFilter = new DepthSortingFilter();

        // 4. coloring
        ColoringFilter coloringFilter = new ColoringFilter(pd.getModelColor());

        // 5. projection transformation
        ProjectionTransformFilter projectionFilter = new ProjectionTransformFilter(pd.getProjTransform());

        // 6. perspective division + viewport
        ScreenSpaceTransformFilter screenSpaceFilter = new ScreenSpaceTransformFilter(pd.getViewportTransform());

        // 7. renderer sink
        RendererSink renderer = new RendererSink(pd.getGraphicsContext(), pd.getRenderingMode());

        // wire up the pipeline
        modelViewFilter.setSuccessor(backfaceCullingFilter);
        backfaceCullingFilter.setSuccessor(depthSortingFilter);

        if (pd.isPerformLighting()) {
            // with lighting: coloring → lighting → projection
            LightingFilter lightingFilter = new LightingFilter(pd.getLightPos());
            depthSortingFilter.setSuccessor(coloringFilter);
            coloringFilter.setSuccessor(lightingFilter);
            lightingFilter.setSuccessor(projectionFilter);
        } else {
            // without lighting: coloring → projection
            depthSortingFilter.setSuccessor(coloringFilter);
            coloringFilter.setSuccessor(projectionFilter);
        }

        projectionFilter.setSuccessor(screenSpaceFilter);
        screenSpaceFilter.setSuccessor(renderer);

        return new AnimationRenderer(pd) {
            private float rotationAngle = 0;

            @Override
            protected void render(float fraction, Model model) {
                // accumulate rotation (radians), frame-independent
                rotationAngle += fraction;

                // create rotation matrix around Y axis
                Mat4 rotationMatrix = Matrices.rotate(rotationAngle, pd.getModelRotAxis());

                // model matrix = translation * rotation (order matters!)
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotationMatrix);

                // model-view = view * model
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                // update the filter with the new matrix
                modelViewFilter.setModelViewMatrix(modelViewMatrix);

                // push all faces through the pipeline
                for (Face face : model.getFaces()) {
                    modelViewFilter.push(face);
                }

                // flush the depth sorting buffer (sorts and pushes downstream)
                depthSortingFilter.flush();
            }
        };
    }
}
