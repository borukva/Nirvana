package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Experiment: disguised as {@code Items.POTION} itself (not {@code Items.BOW}) to test whether an
 * unmodified client will accept it in a brewing stand's potion slot, since the slot's insertion
 * check is hardcoded to a handful of vanilla items including {@code Items.POTION} - purely a
 * throwaway test item, not wired into any real mechanic.
 */
public class TestPotionBongItem extends Item implements PolymerItem {
    public TestPotionBongItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item." + Nirvana.MOD_ID + ".test_potion_bong");
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Nirvana.MOD_ID, "bong");
    }
}
