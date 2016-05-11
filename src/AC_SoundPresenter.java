import java.util.Observable;
import java.util.Observer;

/**
 * Presenter for audio MVP architecture
 * Observer of AC_Map
 *
 * defaults on gain and envelope as I intend to add GUIs later
 *
 */

public class AC_SoundPresenter implements Observer {

    private static final float defaultAttack = 20;
    private static final float defaultDecay = 60;
    private static final float defaultSustainLvl = 0.5f;
    private static final float defaultSustainTime = 100;
    private static final float defaultRelease = 7000;
    private static final float defaultGain = 0.4f;


    protected AC_Map model;
    protected float[][] noteX;
    protected float[][] noteY;
    protected float[][] note0;
    protected float[] envelope;
    protected float gain;


    public AC_SoundPresenter(AC_Map model){
        if(model==null){
            throw new NullPointerException("Presenter must have a model");
        }
        this.model = model;
        this.envelope = new float[]{defaultAttack,
                defaultDecay,
                defaultSustainTime,
                defaultSustainLvl,
                defaultRelease };
        this.gain = defaultGain;

        this.noteX =
                noteToSpectrum(model.getNote(model.getCursor(AC_Map.Cursor.X)));
        this.noteY =
                noteToSpectrum(model.getNote(model.getCursor(AC_Map.Cursor.Y)));
        this.note0 =
                noteToSpectrum(model.getNote(0));
        this.model.addObserver(this);
    }

    // update notes and play (any change to model results in sound change,
    // //so note is played
    @Override
    public void update(Observable o, Object size) {
        this.noteX =
                noteToSpectrum(model.getNote(model.getCursor(AC_Map.Cursor.X)));
        this.noteY =
                noteToSpectrum(model.getNote(model.getCursor(AC_Map.Cursor.Y)));
        play();
    }

    public void play(){ }

    public void setCursor(int x, int y){
        model.setCursor(x, y);
    }

    private float[][] noteToSpectrum(AC_Note note){
        float[][] spectrum = new float[note.size()][2];
        for(int i=0; i<note.size(); i++){
            spectrum[i][0]=(float)note.getFrequency(i);
            spectrum[i][1]=(float)note.getAmplitude(i);
        }
        return spectrum;
    }



    public static void test(Tester t){
        Boolean check = false;
        // sawtooth wave  approximation
        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0, 1.0/6.0};
        AC_Spectrum spec = new AC_Spectrum(amp);
        AC_Note note1 = new AC_Note(spec, 256, 0.8);
        AC_Scale scale = new AC_Scale(9);
        AC_Instrument instr = new AC_Instrument(note1, scale);
        AC_Map model = new AC_Map(instr);

        AC_SoundPresenter presenter = new AC_SoundPresenter(model);

        t.is(presenter.noteX[0][0], 256.0f, "freq of first partial note x correct");
        t.is(presenter.noteX[0][1], 0.8f, "amp of first partial note x correct");
        t.is(presenter.noteX[5][0], 1536.0f, "freq of 6th partial note ycorrect");
        t.is(presenter.noteX[5][1], 0.8f/6.0f, "freq of 6th partial note y correct");

        presenter.setCursor(4,4);
        t.is(presenter.noteX[0][0], 307.2f, "first freq note x correct after cursor change");
        t.is(presenter.noteX[0][1], 0.8f, "amp of first partial note x correct after cursor change");
        t.is(presenter.noteX[5][0], 1843.2f, "freq of 6th partial note y correct after cursor change");
        t.is(presenter.noteX[5][1], 0.8f/6.0f, "freq of 6th partial note y correct after cursor change");

        for(int i=0; i< presenter.noteX.length; i++){
            System.out.println(presenter.noteX[i][0]);
        }
        for(int i=0; i< presenter.noteX.length; i++){
            System.out.println(presenter.noteX[i][1]);
        }
        presenter.setCursor(4,4);
        for(int i=0; i< presenter.noteY.length; i++){
            System.out.println(presenter.noteX[i][0]);
        }
        for(int i=0; i< presenter.noteY.length; i++){
            System.out.println(presenter.noteX[i][1]);
        }


    }




}
