import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



/**
 * Gui elements for interaction with AC_Map
 * Adapted from example here http://docs.oracle.com/javafx/2/get_started/animation.htm
 */
public class AC_MapView extends AC_MapPresenter {


    private static final int mapPaneDimensions = 720;
    private static final int mapPanePad = 10;
    private static final int infoPaneWidth = mapPaneDimensions;
    private static final int infoPaneHeight = 30;

    private static final int TotalPaneHeight = mapPaneDimensions + infoPaneHeight;
    private static final int TotalPaneWidth
                            = mapPaneDimensions > infoPaneWidth ?
                                    mapPaneDimensions : infoPaneWidth;

    private double drawMapDimensions;
    private Group mapDisplay;
    private Circle cursor;
    private Text info;

    public AC_MapView(AC_Map model) {
        super(model);
        this.mapDisplay = new Group();

        this.drawMapDimensions = mapPaneDimensions -(2* mapPanePad);
        this.cursor = new Circle(this.points.get(0).size* drawMapDimensions,
                            Color.web("DEEPSKYBLUE",1));
        this.info = getIntervalInfo();

    }

    public Group get(){

        Group colouredPoints = getChordPoints(0.5);
        colouredPoints = applyColourGradient(colouredPoints);

        // seems inefficient but cloning is verbose
        // and after applying colour gradient click method is lost
        Group clickableOverlay = getChordPoints(0);

        Group guidelines = getGuidelines();
        mapDisplay.getChildren().add(colouredPoints);
        mapDisplay.getChildren().add(guidelines);
        mapDisplay.getChildren().add(clickableOverlay);
        mapDisplay.getChildren().add(this.cursor);
        this.info.setY(TotalPaneHeight - (infoPaneHeight/2) );
        mapDisplay.getChildren().add(this.info);
        moveCursor();
        return mapDisplay;
    }

    @Override
    public void draw(){
        updateInfo();
        moveCursor();
    }


    private Group getChordPoints(double opacity){
        // create points
        Group consonances = new Group();

        for (int i = 0; i < (this.pitchDrawCount*this.pitchDrawCount); i++) {
            indexedCircle circle =
                    new indexedCircle(this.points.get(i).size* drawMapDimensions,
                        Color.web("white", opacity), i);
            circle.setCenterX(mapPanePad + (points.get(i).posX* drawMapDimensions));
            circle.setCenterY(mapPanePad + (points.get(i).posY* drawMapDimensions));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", opacity*1.3));
            circle.setStrokeWidth(1);
            circle.setOnMouseClicked(new EventHandler<MouseEvent>()
                    {
                        @Override
                        public void handle(MouseEvent t) {
                            int posX = points.get(circle.index).idX;
                            int posY = points.get(circle.index).idY;
                            model.setCursor(posX, posY);
                        }
                    });

            consonances.getChildren().add(circle);
        }
        return consonances;
    }

    private void moveCursor(){
        this.cursor.setCenterX(mapPanePad +
                this.getCursorAxisPosition(AC_Map.Cursor.X) * drawMapDimensions);
        this.cursor.setCenterY(mapPanePad + drawMapDimensions -
                (this.getCursorAxisPosition(AC_Map.Cursor.Y) * drawMapDimensions));


    }

    private Rectangle getColourGradient(){
        LinearGradient grad = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new
                Stop[]{
                new Stop(0, Color.DARKRED),
                new Stop(0.7, Color.ORANGE),
                new Stop(1, Color.YELLOW),});
        Rectangle colours = new Rectangle(mapPaneDimensions, mapPaneDimensions,grad);
//        colours.widthProperty().bind(scene.widthProperty());
//        colours.heightProperty().bind(scene.heightProperty());

        return colours;
    }

    private Group applyColourGradient(Group groupToColour){
        Rectangle colours = getColourGradient();
        Rectangle blackBG =
                new Rectangle(mapPaneDimensions, mapPaneDimensions, Color.BLACK);
        Group stencil = new Group(blackBG, groupToColour);
        Group blendedGroup = new Group(stencil, colours);
        // set colors group to be blended, via overlay, with the other group at the same level
        colours.setBlendMode(BlendMode.OVERLAY);

        return blendedGroup;
    }

    private Group getGuidelines(){
        // create guidelines
        Group guidelines = new Group();
        for(int i=0; i<this.guidelines.length; i++) {
            double pos = (this.guidelines[i] * drawMapDimensions);
            //vert
            Line line = new Line(pos+ mapPanePad, mapPanePad, pos+ mapPanePad,
                                    drawMapDimensions + mapPanePad);
            setGuidelineWidth(i,line);
            line.setStroke(Color.web("DEEPSKYBLUE", 0.4));
            guidelines.getChildren().add(line);
            //horiz
            pos = (drawMapDimensions -pos);
            line = new Line(mapPanePad, pos+ mapPanePad,
                                drawMapDimensions + mapPanePad, pos+ mapPanePad);
            setGuidelineWidth(i,line);
            line.setStroke(Color.web("DEEPSKYBLUE", 0.4));
            guidelines.getChildren().add(line);
        }
        guidelines.setBlendMode(BlendMode.ADD);
        return guidelines;
    }

    private void setGuidelineWidth(int interval, Line line){
        for(int i=0; i<majorIntervals.length; i++){
            if(interval==majorIntervals[i]){
                line.setStrokeWidth(4.0);
                return;
            }
        }
        for(int i=0; i<noLineIntervals.length; i++){
            if(interval==noLineIntervals[i]){
                line.setStrokeWidth(0.0);
                return;
            }
        }
        line.setStrokeWidth(1.0);
    }


    private Text getIntervalInfo(){
        Text info = new Text(this.ReportIntervalsString);
        info.setFont(Font.font("Tahoma", FontWeight.EXTRA_LIGHT, 12));
        info.setStroke(Color.DEEPSKYBLUE);
        info.setX(mapPanePad);
        info.setY(mapPaneDimensions);
        info.setWrappingWidth(mapPaneDimensions);
        return info;
    }

    private void updateInfo(){
        info.setText(this.ReportIntervalsString);
    }


    private class indexedCircle extends Circle {
        int index;
        indexedCircle(double radius, Paint fill, int index){
            super(radius,  fill);
            this.index = index;
        }
    }


}
