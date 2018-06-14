public class Main {
    public static final int CHAIR_NUM = 1000;
    public static final int THREAD_NUM = 1000;

    public static void main(String[] args) {
        ChairPickSystem system = new ChairPickSystem();
        system.init();
        system.initChairs(CHAIR_NUM);

        Thread[] threads = new Thread[THREAD_NUM];
        for(int i = 0; i < THREAD_NUM ; i++){
            Thread t = new Thread(new PickChairPlayer(system));
            threads[i] = t;
        }
        for(int i = 0; i < THREAD_NUM ; i++){
            threads[i].start();
        }
    }
}
