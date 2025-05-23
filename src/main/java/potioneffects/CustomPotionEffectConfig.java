package potioneffects;

import java.util.List;

public class CustomPotionEffectConfig {
    public List<EffectEntry> effects;
    
    public static class EffectEntry {
        public String name;
        public Triggers triggers;
    }

    public static class Triggers {
        public Trigger onDamageDealt;
    }

    public static class Trigger {
        public float chance;
    }
}