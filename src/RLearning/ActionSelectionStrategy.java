package RLearning;

import java.util.Map;
import java.util.Set;

public interface ActionSelectionStrategy {
    IndexValue selectAction(int var1, setQ var2, Set<Integer> var3);

    //IndexValue selectAction(int var1, UtilityModel var2, Set<Integer> var3);

    String getPrototype();

    Map<String, String> getAttributes();
}
