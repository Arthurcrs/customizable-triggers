package potioneffects;

import init.TriggerPotionEffectsMod;
import net.minecraft.potion.Potion;

public class CustomPotionEffect extends Potion {

    private final String id;
    private CustomPotionEffectConfig.Trigger onDamageDealt;

    public CustomPotionEffect(String name) {
        super(false, 0x888888);
        this.setRegistryName(TriggerPotionEffectsMod.MODID, name);
        this.setPotionName("effect." + name);
        this.id = name;
    }

    public void setOnDamageDealt(CustomPotionEffectConfig.Trigger trigger) {
        this.onDamageDealt = trigger;
    }

    public CustomPotionEffectConfig.Trigger getOnDamageDealt() {
        return onDamageDealt;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean hasStatusIcon() {
        return true;
    }
}