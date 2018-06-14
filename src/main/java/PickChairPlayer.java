import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public  class PickChairPlayer implements Runnable {
        public static AtomicInteger integer = new AtomicInteger(0);
        private ChairPickSystem system;

        public PickChairPlayer(ChairPickSystem system) {
            this.system = system;
        }

        @Override
        public void run() {
            String userName = "user" + integer.incrementAndGet();
            system.pickChair(userName, new Random().nextInt(1000));
        }
    }