package Graphics;

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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Pyramidz extends Application {

    private PerspectiveCamera camera;
    private final double sceneWidth = 600;
    private final double sceneHeight = 600;
    private double sceneX, sceneY = 0;
    private double fixedXAngle, fixedYAngle = 0;
    private double anchorAngleX, anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Group pyramid1 = buildPyramid(100, 200, Color.BLUEVIOLET, false, false);
        Group pyramid2 = buildPyramid(100, 200, Color.BLUEVIOLET, true, true);
        Group pyramid3 = buildPyramid(100,200,Color.DEEPPINK,true,true);
        Group pyramid4 = buildPyramid(100,200,Color.DEEPPINK,true,false);

        pyramid1.setTranslateX(-100);
        pyramid1.setRotationAxis(Rotate.Y_AXIS);
        pyramid1.setRotate(45);

        pyramid2.setTranslateX(-100);
        pyramid2.setTranslateY(-100);
        pyramid2.setRotationAxis(Rotate.Z_AXIS);
        pyramid2.setRotate(180);

        pyramid3.setTranslateX(100);

        pyramid4.setTranslateX(100);
        pyramid4.setTranslateY(-100);
        pyramid4.setRotationAxis(Rotate.Z_AXIS);
        pyramid4.setRotate(180);

        Group pyramidGroup = new Group(pyramid1, pyramid2, pyramid3, pyramid4);

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        pyramidGroup.getTransforms().addAll(xRotate,yRotate);

        Group mainGrp = new Group();
        Scene scene = new Scene(mainGrp, sceneWidth, sceneHeight);
        scene.setFill(Color.BLACK);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        scene.setCamera(camera);

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            sceneX = event.getSceneX();
            sceneY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        } );

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (sceneX - event.getSceneY()));
            angleY.set(anchorAngleY + sceneY - event.getSceneX());
        });

        PointLight light = new PointLight(Color.WHITE);

        mainGrp.getChildren().add(light);
        light.setTranslateZ(-sceneWidth);
        light.setTranslateY(-sceneHeight);

        mainGrp.getChildren().addAll(pyramidGroup);

        primaryStage.setTitle("Pyramidz");
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
