package RLearning;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EpsilonGreedyActionSelectionStrategy implements ActionSelectionStrategy {
    protected Map<String, String> attributes = new HashMap();
    double epsilon;

    public EpsilonGreedyActionSelectionStrategy() {
        this.epsilon(0.1D);
    }

    private void epsilon(double v) {
        this.attributes.put("epsilon", "" + v);
    }

    @Override
    public IndexValue selectAction(int var1, setQ var2, Set<Integer> var3) {
        return null;
    }

    @Override
    public String getPrototype() {
        return null;
    }

    @Override
    public Map<String, String> getAttributes() {
        return null;
    }
}
