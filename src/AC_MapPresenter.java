import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Presenter for AC_Map in MVP GUI architecture
 * Observer of AC_Map
 * converts map data into set instructions for drawing GUI
 * converts instructions from gui into instructions for AC_Map & AC_Synth classes
 *
 */

public class AC_MapPresenter implements Observer{

    private static final double pointScale = 0.007;
    private static final double defaultFirstPitch = 0.0;
    private static final double defaultLastPitch = 1200.0;
    private static final int defaultIntervalLinesCount = 12;
    public static final int[] majorIntervals = {2,4,5,7,9,11};
    public static final int[] noLineIntervals = {0,12};


    protected AC_Map model;
    protected List<AC_Point> points;
    protected double[] guidelines;
    protected int pitchDrawCount;
    protected double firstPitchDrawn;
    protected double lastPitchDrawn;
    protected String ReportIntervalsString;



    public AC_MapPresenter(AC_Map model){
        if(model==null){
            throw new NullPointerException("Presenter must have a model");
        }
        this.model = model;
        points = new ArrayList<>();
        this.firstPitchDrawn = defaultFirstPitch;
        this.lastPitchDrawn = defaultLastPitch;

        this.model.addObserver(this);
        this.pitchDrawCount= getPitchDrawCount();
        this.ReportIntervalsString = getIntervalsString();
        this.guidelines = setIntervalGuidelines(defaultIntervalLinesCount);
        recalcPoints();
    }

    @Override
    public void update(Observable o, Object size) {
        this.pitchDrawCount= getPitchDrawCount();
        this.ReportIntervalsString = getIntervalsString();
        recalcPoints();
        draw();
    }

    public void draw(){ }

    private void recalcPoints(){
        if(this.pitchDrawCount==0){
            return;
        }
        AC_Point thisPoint;
        points.clear();

        for(int i=0; i<this.pitchDrawCount; i++) {
            for (int j = 0; j < this.pitchDrawCount; j++) {
                thisPoint  = new AC_Point();
                thisPoint.idX = j;
                thisPoint.idY = i;
                thisPoint.posX = getNoteAxisPosition(j);
                thisPoint.posY = 1.0-getNoteAxisPosition(i);
                thisPoint.size = consToSize(model.getConsonance(i,j));
                points.add(thisPoint);
            }
        }
    }

    public double getCursorAxisPosition(AC_Map.Cursor xOrY){
        return getNoteAxisPosition(model.getCursor(xOrY));
    }


    private int getPitchDrawCount(){
        int i=0;
        while(i<this.model.size()
                && this.model.getNoteCents(i)
                                            <= this.lastPitchDrawn){
            i++;
        }
        return i;
    }

    // returns values between 0 and 1 for relative positioning on screen
    // currently only equal temperament. Refactor for other options later
    private double[] setIntervalGuidelines(int intervalCount){
        double[] lines = new double[intervalCount+1];
        for(int i=0; i< intervalCount+1; i++){
            lines[i]= (double)i * (1.0/ (intervalCount));
        }
        return lines;
    }




    private double consToSize(double consonance){
        double rawsize = consonance/this.pitchDrawCount;
        return Math.log(100.0*(rawsize)) * pointScale;
    }

    private double getNoteAxisPosition(int i){
        return (this.model.getNoteCents(i)-firstPitchDrawn) /
                                                        (lastPitchDrawn);
    }

    // print root, notes (ratios & cents) ratio between & consonance score
    private String getIntervalsString(){
        int cursorX = model.getCursor(AC_Map.Cursor.X);
        int cursorY = model.getCursor(AC_Map.Cursor.Y);
        DecimalFormat fmt = new DecimalFormat("#.#");

        String root=  fmt.format(model.getNote(0).getRootFrequency());
        String xRatio = model.printNoteRatio(cursorX);
        String yRatio = model.printNoteRatio(cursorY);
        String cons = fmt.format(model.getConsonance(cursorX, cursorY));

        String xCents = fmt.format(model.getNoteCents(cursorX));
        String yCents = fmt.format(model.getNoteCents(cursorY));
        String centsDiff = fmt.format(
                Math.abs(model.getNoteCents(cursorY)
                        - model.getNoteCents(cursorX)));

        return "Scale Root:"+root+"hz. noteX: "+xRatio+
                " ("+xCents+"cents)"+ " noteY: "+yRatio+" ("+yCents+"cents) "
                +centsDiff+" cents difference. Consonance: "+cons;
    }

    class AC_Point {
        protected int idX;
        protected int idY;
        protected double posX;
        protected double posY;
        protected double size;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public static void test(Tester t){
        Boolean check = false;
        // sawtooth wave  approximation
        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0, 1.0/6.0};
        AC_Spectrum spec = new AC_Spectrum(amp);
        AC_Note note1 = new AC_Note(spec, 256, 0.8);
        AC_Scale scale = new AC_Scale(9);
        AC_Instrument instr = new AC_Instrument(note1, scale);
        AC_Map model = new AC_Map(instr);

        AC_MapPresenter presenter = new AC_MapPresenter(model);

        t.is(presenter.getPitchDrawCount(),15, "15 pitches in 1 octave of 9 limit scale");
        t.is(presenter.points.size(), 15*15, "correct number of points to be drawn");
        t.is(presenter.getNoteAxisPosition(0),0.0, "first note draws at 0");
        t.is(presenter.getNoteAxisPosition(14),1.0, "last note draws at 1");
        t.is(t.round(presenter.getNoteAxisPosition(6),6),0.36257,
                                                    "6th note draws correctly");
        t.is(t.round(presenter.getNoteAxisPosition(12),6),0.807355,
                                                    "12th note draws correctly");

        // first point
        t.is(presenter.points.get(0).idX, 0, "first point note1= 0");
        t.is(presenter.points.get(0).idY, 0, "first point note2= 0");
        t.is(presenter.points.get(0).posX, 0.0, "first point drawn at x:0");
        t.is(presenter.points.get(0).posY, 1.0, "first point drawn at y:1");
        t.is(t.round(presenter.points.get(0).size,6),0.013093, "first size is correct");

        // a middle point
        t.is(presenter.points.get(4).idX, 4, "5th point note1= 4");
        t.is(presenter.points.get(4).idY, 0, "5th point note2= 0");
        t.is(t.round(presenter.points.get(4).posX, 6), 0.263034,
                                            "5th point drawn at correct x:");
        t.is(presenter.points.get(4).posY, 1.0, "5th point drawn at y:0");
        t.is(t.round(presenter.points.get(4).size,6),0.010086,"4th size is correct");

        // last point (15x15 notes in grid for a 9 limit scale)
        t.is(presenter.points.get(224).idX, 14, "last point note1= 14");
        t.is(presenter.points.get(224).idY, 14, "last point note2= 14");
        t.is(presenter.points.get(224).posX, 1.0, "last point drawn at x:1");
        t.is(presenter.points.get(224).posY, 0.0, "last point drawn at y:0");
        t.is(t.round(presenter.points.get(224).size,6), 0.013232,
                                                     "last size is correct");

        double[] lines = presenter.setIntervalGuidelines(12);
        t.is(lines.length,13, "13 lines for 12 note scale (scale + octave)");
        t.is(t.round(lines[0],6) ,0.0, "first line pos correct ");
        t.is(t.round(lines[9],6) ,0.75, "middle line pos correct ");
        t.is(t.round(lines[12],6) ,1.0, "last line pos correct ");

    }

}
