package tirnak.persistence.common;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 *
 */
public class Repeat {
    private final static int MILLISECONDS_IN_SECOND = 1000;
    private Runnable toRun;
    private Duration duration;
    private List<BooleanSupplier> conditions = new ArrayList<>();

    private Repeat(Runnable toRun) {
        this.toRun = toRun;
    }

    public static Repeat procedure(Runnable toRun) {
        return new Repeat(toRun);
    }

    public Repeat during(Duration duration) {
        this.duration = duration;
        return this;
    }

    public Repeat orUntil(BooleanSupplier condition) {
        conditions.add(condition);
        return this;
    }

    public void run() {
        long timeoutMs = System.currentTimeMillis() + duration.toMillis();
        while (System.currentTimeMillis() < timeoutMs && conditions.stream().noneMatch(BooleanSupplier::getAsBoolean)) {
            toRun.run();
        }
    }
}
