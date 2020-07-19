package sample;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

public class Pyramidz extends Application {

    private PerspectiveCamera camera;
    private final double sceneWidth = 600;
    private double sceneHeight = 600;
    private double scenex, scenery = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Group pyramid1 = buildPyramid(100, 200, Color.BLUEVIOLET, false, false);
        Group pyramidGroup = new Group(pyramid1);

        Group mainGrp = new Group();
        Scene scene = new Scene(mainGrp, sceneWidth, sceneHeight);
        scene.setFill(Color.BLACK);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        scene.setCamera(camera);

        mainGrp.getChildren().addAll(pyramidGroup);

        primaryStage.setTitle("TriangleMeshes");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private Group buildPyramid(float height, float hypotenuse, Color color, boolean ambient, boolean fill) {
        final TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll(
                0, 0, 0,                        // Point 0: Top of Pyramid
                0, height, - hypotenuse/2,             // Point 1: closest base point to camera
                -hypotenuse/2, height, 0,                // Point 2: leftmost base point to camera
                hypotenuse/2, height, 0,                 // Point 3: farthest base point to camera
                0, height, hypotenuse/2                 // Point 4: rightmost base point to camera
        );

        mesh.getTexCoords().addAll(0,0);

        mesh.getFaces().addAll(
                0,0,2,0,1,0,
                0,0,1,0,3,0,
                0,0,3,0,4,0,
                0,0,4,0,2,0,
                4,0,1,0,2,0,
                4,0,3,0,1,0
        );

        MeshView meshView = new MeshView(mesh);
        meshView.setDrawMode(DrawMode.LINE);
        meshView.setCullFace(CullFace.BACK);

        Group pyraGroup = new Group();
        pyraGroup.getChildren().add(meshView);

        if(null != color)
        {
            PhongMaterial material = new PhongMaterial(color);
            meshView.setMaterial(material);
        }
        if(ambient) {
            AmbientLight light = new AmbientLight(Color.WHITE);
            light.getScope().add(meshView);
            pyraGroup.getChildren().add(light);
        }
        if(fill) {
            meshView.setDrawMode(DrawMode.FILL);
        }
        return pyraGroup;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
