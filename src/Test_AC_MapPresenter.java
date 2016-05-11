/**
 * Testing for AC_MapPresenter class
 */
public class Test_AC_MapPresenter {

    public static void run() {

        Tester t = new Tester();

        System.out.println("AC_MapPresenter Internal Testing");
        AC_MapPresenter.test(t);

        t.results();
    }

    // no interface tests - designed to be used extended

    public static void main(String[] args) {
        run();
    }

}
