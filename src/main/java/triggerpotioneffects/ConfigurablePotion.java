package triggerpotioneffects;

import net.minecraft.potion.Potion;

public class ConfigurablePotion extends Potion {

    public ConfigurablePotion(String name) {
        super(false, 0x888888); // neutral color
        this.setRegistryName(TriggerPotionEffectsMod.MODID, name);
        this.setPotionName("effect." + name);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }
}