/**
 * Testing for AC_SoundPresenter class
 */
public class Test_AC_SoundPresenter {

    public static void run() {

        Tester t = new Tester();

        System.out.println("AC_SoundPresenter Internal Testing");
        AC_SoundPresenter.test(t);

        t.results();
    }

    // no interface tests - designed to be used extended

    public static void main(String[] args) {
        run();
    }

}
