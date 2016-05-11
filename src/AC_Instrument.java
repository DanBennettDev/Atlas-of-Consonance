import java.util.ArrayList;
import java.util.List;

/**
 * Instrument Class
 * Combines scale and notes
 * could be dispensed with in this version where there is 1 spectrum across scale
 * but it opens the way to have spectrum vary across scale
 * which will allow modelling cases more like real instruments
 * very minimal, assumes as little as poss about scales and notes
 * to ease future refactoring
 */

public class AC_Instrument {

    public AC_Scale scale;
    public List<AC_Note> notes;

    AC_Instrument(AC_Note rootNote, AC_Scale scale){
        this.scale = scale;
        notes = new ArrayList<>();
        notes.add(rootNote);
        double transposeAmount=0;

        for(int i=1;i<scale.size(); i++){
            transposeAmount = scale.getInterval(i).getRatio_double();
            notes.add(rootNote.getTransposition(transposeAmount));
        }
    }

    String getInterval_String(int i){
        return this.scale.getInterval(i).getRatio_string();
    }

    double getInterval_ratio(int i){
        return this.scale.getInterval(i).getRatio_double();
    }

    double getInterval_cents(int i){
        return this.scale.getInterval(i).getCents();
    }
    double getRootFreq(int i){
        return this.notes.get(i).getRootFrequency();
    }
    AC_Note getNote(int i){
        return this.notes.get(i);
    }
    int getNoteCount(){ return this.scale.size();}

}
