import java.util.Observable;

/**
 * calculates consonance of all pairs of notes in a scale and stores in a map
 * spectral "roughness" calculation from Vassilakis, 2005
 * http://www.acousticslab.org/learnmoresra/files/vassilakis2005sre.pdf
 *
 * Observable even though I only expect only 1 observer at present (the map presenter)
 * Observer/observable pattern ensures loose coupling to GUI which will make it
 * easier to convert this to android, and allows for multiple views altering the
 * map (eg. separate dialogues for scale building and note spectrum building.)
 *
 * No public set methods outside constructor as not needed and this allows secure
 * passing of the object
 *
 */

public class AC_Map extends Observable{

    private static final int scaleLimitDefault = 11;
    private static final int harmonicCountDefault = 10;
    private static final double rootHzDefault = 500.0;
    private static final boolean octavingDefault = true;
    private static final double noteVolDefault = 0.8;


    private double[][] map; // this will need to be scaled up to 3d at some point
    private AC_Instrument instrument;
    private int size;
    private int currNoteX;
    private int currNoteY;


    AC_Map(AC_Instrument instr){
        this.instrument = instr;
        this.size = instr.getNoteCount();
        this.currNoteX = 0;
        this.currNoteY = 0;
        map = new double[size][size];
        makeMap();
    }

    AC_Map(int scaleLimit){
        double[] harmonicsAmps = new double[harmonicCountDefault];
        //sawtooth spectrum
        for(int i=0; i<harmonicCountDefault; i++){
            harmonicsAmps[i]=1.0/(double)(i+1);
        }
        AC_Spectrum spec = new AC_Spectrum(harmonicsAmps);
        AC_Note note = new AC_Note(spec, rootHzDefault, noteVolDefault);
        AC_Scale scale = new AC_Scale(scaleLimit, octavingDefault);
        AC_Instrument instr = new AC_Instrument(note, scale);

        this.instrument = instr;
        this.size = instr.getNoteCount();
        this.currNoteX = 0;
        this.currNoteY = 0;
        map = new double[size][size];
        makeMap();
    }

    AC_Map(){
        this(scaleLimitDefault);
    }

    public void setCursor(int x, int y){
        if(x<0 || x>= this.size || y<0 || y>= this.size){
            throw new IndexOutOfBoundsException("no coordinate at "+x+","+y);
        }
        this.currNoteX = x;
        this.currNoteY = y;
        setChanged();
        notifyObservers(size());
    }

    public int getCursor(Cursor xOrY){
        if(xOrY==Cursor.X){
            return this.currNoteX;
        }
        return this.currNoteY;
    }

    public void makeMap(){
        double maxRoughness=0;
        double roughness;
        double maxInterval;

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                roughness = calcRoughness_complex(this.instrument.getNote(i),
                                                  this.instrument.getNote(j),
                                                  this.instrument.getNote(0));
                map[i][j]= roughness;

                if(roughness>maxRoughness){
                    maxRoughness=roughness;
                }
            }
        }
        convertMapRoughnessToConsonance(maxRoughness);
        setChanged();
        notifyObservers(this.size);
    }

    public double getConsonance(int note1, int note2){
        if(note1<0 ||note1>=this.size ||note2<0 ||note2>=this.size){
            throw new IndexOutOfBoundsException(
                    "only "+this.size+"notes stored");
        }
        return map[note1][note2];
    }
    public AC_Note getNote(int i){
        if(i<0 ||i>=this.size){
            throw new IndexOutOfBoundsException(
                    "only "+this.size+"notes stored");
        }
        return instrument.getNote(i);
    }

    public double getNoteRatio(int i){
        if(i<0 ||i>=this.size){
            throw new IndexOutOfBoundsException(
                            "only "+this.size+"notes stored");
        }
        return instrument.getInterval_ratio(i);
    }

    public String printNoteRatio(int i){
        if(i<0 ||i>=this.size){
            throw new IndexOutOfBoundsException(
                    "only "+this.size+"notes stored");
        }
        return instrument.getInterval_String(i);
    }

    public double getNoteCents(int i){
        if(i<0 ||i>=this.size){
            throw new IndexOutOfBoundsException(
                    "only "+this.size+"notes stored");
        }
        return instrument.getInterval_cents(i);
    }


    public int size(){
        return size;
    }

    // converts map from roughness to relative consonance 0 = least consonant
    private void convertMapRoughnessToConsonance(double maxRoughness){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                map[i][j] =
                            1.0 - (map[i][j] / maxRoughness);
            }
        }
    }

    public double calcRoughness_pair(double freq1, double amp1,
                                            double freq2, double amp2){
        // R = X^(0.1)*(0.5Y^3.11)*Z

        double fmin = Math.min(freq1,freq2);
        double fmax = Math.max(freq1,freq2);
        double amin = Math.min(amp1,amp2);
        double amax = Math.max(amp1,amp2);

        double b1 = 3.5;    double b2=5.75;
        double s1 = 0.0207; double s2 = 18.96;
        double s = 0.24/(s1*fmin + s2);

        double x = amp1*amp2;
        double y = 2 * amin / (amin+amax);
        double z = Math.exp(-b1*s*(fmax-fmin)) - Math.exp(-b2*s*(fmax-fmin));

        return Math.pow(x,0.1)*0.5*Math.pow(y,3.11)*z;
    }


    public double calcRoughness_complex(AC_Note n1, AC_Note n2){
        double roughness=0;
        for(int i=0; i<n1.size(); i++){
            for(int j=0; j<n2.size(); j++){
                //System.out.println(""+n1.getFrequency(i) +" "+ n2.getFrequency(j));
                roughness+= calcRoughness_pair(
                                n1.getFrequency(i), n1.getAmplitude(i),
                                n2.getFrequency(j), n2.getAmplitude(j)
                                );
            }
        }
        //System.out.println("done");
        return roughness;
    }

    public double calcRoughness_complex(AC_Note n1, AC_Note n2, AC_Note n0){
        double roughness=0;
        for(int h=0; h<n0.size(); h++) {
            for (int i = 0; i < n1.size(); i++) {
                for (int j = 0; j < n2.size(); j++) {
                    //System.out.println(""+n1.getFrequency(i) +" "+ n2.getFrequency(j));
                    roughness += calcRoughness_pair(
                            n1.getFrequency(i), n1.getAmplitude(i),
                            n2.getFrequency(j), n2.getAmplitude(j)
                    );
                    roughness += calcRoughness_pair(
                            n0.getFrequency(h), n0.getAmplitude(h),
                            n2.getFrequency(j), n2.getAmplitude(j)
                    );
                    roughness += calcRoughness_pair(
                            n1.getFrequency(i), n1.getAmplitude(i),
                            n0.getFrequency(h), n0.getAmplitude(h)
                    );
                }
            }
        }
        //System.out.println("done");
        return roughness;
    }



    public static enum Cursor {
        X, Y
    }

}