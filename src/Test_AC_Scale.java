/**
 * Created by dan on 22/03/16.
 */
public class Test_AC_Scale {

    public static void run() {

        Tester t = new Tester();
        System.out.println("Testing: AC_Scale - interface");
        Boolean check = false;

        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0};
        AC_Scale scale = new AC_Scale(2);

        t.is(scale.getInterval(0).getRatio_double(),1.0, "first interval at 1");
        t.is(scale.getInterval(1).getRatio_double(),2.0, "2nd interval of 2 limit scale ==2");
        t.is(scale.size(),2,"2 limit scale has exactly 2 members");

        // non octaving
        scale = new AC_Scale(9);
        t.is(scale.size(),22,"9 limit scale has exactly 23 members");
        t.is(scale.getInterval(1).getRatioTop(),9, "interval 1 top correct");
        t.is(scale.getInterval(1).getRatioBottom(),8, "interval 1 bottom correct");
        t.is(scale.getInterval(2).getRatioTop(),8, "interval 2 top correct");
        t.is(scale.getInterval(2).getRatioBottom(),7, "interval 2 bottom correct");
        t.is(scale.getInterval(5).getRatioTop(),5, "interval 5 top correct");
        t.is(scale.getInterval(5).getRatioBottom(),4, "interval 5 bottom correct");
        t.is(scale.getInterval(6).getRatioTop(),9, "interval 6 top correct");
        t.is(scale.getInterval(6).getRatioBottom(),7, "interval 6 bottom correct");
        t.is(scale.getInterval(10).getRatioTop(),8, "interval 10 top correct");
        t.is(scale.getInterval(10).getRatioBottom(),5, "interval 10 bottom correct");
        t.is(scale.getInterval(18).getRatioTop(),8, "interval 18 top correct");
        t.is(scale.getInterval(18).getRatioBottom(),3, "interval 18 bottom correct");
        System.out.println("full 9 limit scale: "+scale.size()+" members");
        scale.printScale();

        //octaving
        boolean octaving=true;
        scale = new AC_Scale(9,octaving);
        t.is(scale.size(),29,"9 limit octaving scale has exactly 29 members");
        t.is(scale.getInterval(1).getRatioTop(),9, "interval 1 top correct");
        t.is(scale.getInterval(1).getRatioBottom(),8, "interval 1 bottom correct");
        t.is(scale.getInterval(2).getRatioTop(),8, "interval 2 top correct");
        t.is(scale.getInterval(2).getRatioBottom(),7, "interval 2 bottom correct");
        t.is(scale.getInterval(5).getRatioTop(),5, "interval 5 top correct");
        t.is(scale.getInterval(5).getRatioBottom(),4, "interval 5 bottom correct");
        t.is(scale.getInterval(6).getRatioTop(),9, "interval 6 top correct");
        t.is(scale.getInterval(6).getRatioBottom(),7, "interval 6 bottom correct");
        t.is(scale.getInterval(10).getRatioTop(),8, "interval 10 top correct");
        t.is(scale.getInterval(10).getRatioBottom(),5, "interval 10 bottom correct");
        t.is(scale.getInterval(18).getRatioTop(),12, "interval 18 top correct");
        t.is(scale.getInterval(18).getRatioBottom(),5, "interval 18 bottom correct");


        System.out.println("full 9 limit octaving scale: "+scale.size()+" members");
        scale.printScale();




        t.results();
    }


    public static void main(String[] args) {
        run();
    }

}
