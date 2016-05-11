import java.util.ArrayList;
import java.util.List;

/**
 * Scale class for "atlas of consonance"
 * A scale here is an ordered set of interval defined by some construction principle
 * Scales need not repeat exactly at the octave as in (eg) western
 * equal temperament, and may have any number of members, both absolutely and per
 * octave.
 *
 * In the first version scales are just - constructed on the basis of
 * a supplied harmonic limit and incorporating every ratio of integers beneath
 * that limit, up to a defined octave ceiling (intervals in the ceiling
 * octave are included)
 *
 * TODO:
 *  extend to handle specific case of Equal Temperament scales
 *  extend for arbitrary and subset scales (eg indonesian, well temperament etc.)
 */

public class AC_Scale {

    // CONSTANT
    private static final double defaultRatioCeiling = 4.0; // store intervals up to this octave.
    
    public final int limit;
    public final boolean isOctaving;
    private final double ratioCeiling;
    public final List<AC_Interval> scale;

    AC_Scale(int limit, boolean isOctaving){
        if(limit<2){
            throw new IllegalArgumentException("limit must be >=2");
        }
        this.isOctaving = isOctaving;
        this.limit = limit;
        if(isOctaving){
            this.ratioCeiling = 2;
        } else {
            this.ratioCeiling = defaultRatioCeiling;
        }
        this.scale = constructScale(limit);
    }

    AC_Scale(int limit) {
        this(limit, false);
    }


    private List<AC_Interval> constructScale(int limit){
        List<AC_Interval> scale = new ArrayList<>();
        scale.add(new AC_Interval(1,1));
        boolean repeat;
        int noteNo = 0;
        double newIntvl;

        for(int i=2; i<=limit; i++){
            for(int j=1; j<i; j++){
                repeat = false;
                newIntvl = (double)i/(double)j;
                // check doesn't already exist & find location
                for(noteNo=0; noteNo<scale.size() && !repeat
                        && newIntvl>=scale.get(noteNo).getRatio_double();
                            noteNo++){
                    if(scale.get(noteNo).getRatio_double()==newIntvl){
                        repeat=true;
                    }
                }
                if(!repeat && newIntvl<=ratioCeiling) {
                    scale.add(noteNo, new AC_Interval(i, j));
                }
            }
        }
        if(isOctaving){
            int size = scale.size();
            // start at 1 because we have already written the octave
            for(int i=1; i<size; i++){
                scale.add(scale.get(i).raiseByOctave(1));
            }
        }
        return scale;
    }

    public AC_Interval getInterval(int i) {
        return this.scale.get(i);
    }

    public int size(){
        return this.scale.size();
    }

    public void printScale(){
        String out = "";
        AC_Interval itvl;
        for(int i=0; i<scale.size(); i++){
            itvl = scale.get(i);
            out = out + itvl.getRatioTop() + "/" + itvl.getRatioBottom();
            out = out + " (" + itvl.getRatio_double() + "), ";
        }
        System.out.println(out);
    }

}
