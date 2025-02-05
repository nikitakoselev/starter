package org.eclipse.starter.mavengenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * General Maven parameters for any command
 */
public class MavenParameters {

    protected Optional<Boolean> interactiveMode = Optional.empty();
    protected Optional<Integer> numberOfThreads = Optional.empty();
    protected List<String> goals = new ArrayList<>();
    protected boolean guessNumberOfThreads = false;

    public MavenParameters interactiveMode(final Boolean value) {
        this.interactiveMode = Optional.ofNullable(value);
        return this;
    }

    public MavenParameters numberOfThreads(final Integer i) {
        this.numberOfThreads = Optional.ofNullable(i);
        return this;
    }
    
    public MavenParameters guessNumberOfThreads() {
        this.guessNumberOfThreads = true;
        return this;
    }

    public List<String> getGoals() {
        return goals;
    }

    public MavenParameters addGoal(String goal) {
        goals.add(goal);
        return this;
    }

    protected Properties getProperties() {
        Properties properties = new Properties();
        interactiveMode
                .map(bool -> bool ? "true" : "false")
                .ifPresent(v -> properties.put("interactiveMode", v));
        return properties;
    }

    protected List<String> getOptions() {
        List<String> options = new ArrayList<>();
        Optional<Integer> finalNumberOfThreads = numberOfThreads
                .or(() -> {
                    if (guessNumberOfThreads) {
                        return Optional.of(Runtime.getRuntime().availableProcessors() / 2 + 1);
                    } else {
                        return Optional.empty();
                    }
                });
        finalNumberOfThreads.ifPresent( numberOfThreads -> {
            options.add("-T" + numberOfThreads);
        });
        return options;
    }

    public <MVN_CTX extends MavenContext> MVN_CTX updateMavenContext(MavenContext<MVN_CTX> context) {
        context.addGoals(goals);
        context.addProperties(getProperties());
        context.addOptions(getOptions());
        return (MVN_CTX) context;
    }

}
