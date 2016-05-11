import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Rough outer Dev wrapper for the app to allow me to trial things.
 * TODO: Organise into methods, title drawing needs to go in own class
 */
public class Test_AppRough extends Application {

    private static final int screenWidth = 1010;
    private static final int screenHeight = 900;

    AC_Map model;
    AC_MapView  mapView;
    AC_Synth synth;

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Atlas of Consonance");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text scenetitle = new Text("Atlas of Consonance");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        scenetitle.setStroke(Color.DEEPSKYBLUE);
        scenetitle.setStrokeType(StrokeType.OUTSIDE);
        scenetitle.setStrokeWidth(1);
        grid.add(scenetitle, 0, 0, 2, 1);


        //model = new AC_Map();
        model = new AC_Map(13);

        mapView = new AC_MapView (model);
        this.synth = new AC_Synth(model);

        grid.add(mapView.get(), 0,2);


        Scene scene = new Scene(grid, screenWidth, screenHeight, Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @Override
    public void stop() {
        this.synth.close();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
