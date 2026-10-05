package galena.nirvana.polydex;

import eu.pb4.polydex.api.v1.recipe.PageBuilder;
import eu.pb4.polydex.api.v1.recipe.PolydexCategory;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexIngredient;
import eu.pb4.polydex.api.v1.recipe.PolydexPage;
import eu.pb4.polydex.api.v1.recipe.PolydexStack;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/** Uses the ordinary Polydex grid without adding a custom resource-pack UI. */
record NirvanaPolydexPage(Identifier identifier, List<ItemStack> input, ItemStack output,
                          boolean brewing) implements PolydexPage {
    @Override
    public List<PolydexIngredient<?>> ingredients() {
        return input.stream().filter(stack -> !stack.isEmpty())
                .<PolydexIngredient<?>>map(RecipeIngredient::new).toList();
    }

    @Override
    public List<PolydexCategory> categories() {
        return List.of(brewing ? PolydexCategory.BREWING : PolydexCategory.CRAFTING);
    }

    @Override
    public ItemStack typeIcon(ServerPlayer player) {
        return new ItemStack(brewing ? Items.BREWING_STAND : Items.CRAFTING_TABLE);
    }

    @Override
    public ItemStack entryIcon(@Nullable PolydexEntry entry, ServerPlayer player) {
        return output.copy();
    }

    @Override
    public boolean isOwner(MinecraftServer server, PolydexEntry entry) {
        return entry.stack().getBacking() instanceof ItemStack stack && sameVariant(output, stack);
    }

    /** Match recipe-relevant components, ignoring durability and custom names. */
    static boolean sameVariant(ItemStack expected, ItemStack actual) {
        return expected.is(actual.getItem())
                && Objects.equals(expected.get(DataComponents.POTION_CONTENTS), actual.get(DataComponents.POTION_CONTENTS))
                && Objects.equals(expected.get(DataComponents.SUSPICIOUS_STEW_EFFECTS), actual.get(DataComponents.SUSPICIOUS_STEW_EFFECTS));
    }

    /** Polydex's ordinary weak stack matching ignores potion/effect components. */
    private record RecipeIngredient(ItemStack expected) implements PolydexIngredient<ItemStack> {
        @Override
        public List<PolydexStack<ItemStack>> asStacks() {
            return List.of(PolydexStack.of(expected));
        }

        @Override
        public float chance() {
            return 1;
        }

        @Override
        public long amount() {
            return expected.getCount();
        }

        @Override
        public boolean matchesDirect(PolydexStack<ItemStack> stack, boolean strict) {
            return sameVariant(expected, stack.getBacking());
        }

        @Override
        public boolean isEmpty() {
            return expected.isEmpty();
        }

        @Override
        public Class<ItemStack> getBackingClass() {
            return ItemStack.class;
        }
    }

    @Override
    public boolean syncWithClient(ServerPlayer player) {
        return false;
    }

    @Override
    public void createPage(@Nullable PolydexEntry entry, ServerPlayer player, PageBuilder builder) {
        if (brewing) {
            createBrewingPage(builder);
        } else {
            for (int i = 0; i < input.size(); i++) {
                if (!input.get(i).isEmpty()) {
                    builder.setIngredient(2 + i % 3, 1 + i / 3, input.get(i));
                }
            }
            builder.setOutput(6, 2, output);
        }
    }

    private void createBrewingPage(PageBuilder builder) {
        var stage = input.getFirst().is(Items.POTION) ? "base"
                : input.getFirst().is(NirvanaItems.BONG) ? "prepare" : "effect";
        builder.set(8, 0, new GuiElementBuilder(Items.BOOK)
                .setName(Component.translatable("text.nirvana.polydex.brewing.stage." + stage))
                .addLoreLine(description("intro"))
                .addLoreLine(description("step.base"))
                .addLoreLine(description("step.prepare"))
                .addLoreLine(description("step.effect"))
                .addLoreLine(description("navigation")));

        setLabelledStack(builder, 3, 1, input.get(1), "reagent", "slot.reagent");
        builder.set(3, 2, new GuiElementBuilder(Items.BREWING_STAND)
                .setName(Component.translatable("text.nirvana.polydex.brewing.stand"))
                .addLoreLine(description("intro")));
        setLabelledStack(builder, 3, 3, input.getFirst(), "input", "slot.input");
        builder.set(4, 2, new GuiElementBuilder(Items.ARROW)
                .setName(Component.translatable("text.nirvana.polydex.brewing.brew")));
        // Polydex uses the same clickable display for outputs and ingredients. Decorating via
        // its consumer keeps the original stack for recipe lookup, unlike renaming the stack.
        setLabelledStack(builder, 5, 2, output, "output", "slot.output");
        builder.set(1, 2, new GuiElementBuilder(Items.BLAZE_POWDER)
                .setName(Component.translatable("text.nirvana.polydex.brewing.fuel"))
                .addLoreLine(description("slot.fuel")));

        // A complete, explicitly labelled example remains visible on every brewing page.
        // These navigation shortcuts do not become ingredients of the current recipe.
        var water = new ItemStack(Items.POTION);
        water.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        var awkward = new ItemStack(NirvanaItems.POTION_BONG);
        awkward.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD));
        var swiftness = new ItemStack(NirvanaItems.POTION_BONG);
        swiftness.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.SWIFTNESS));

        builder.set(0, 4, new GuiElementBuilder(water)
                .setName(Component.translatable("text.nirvana.polydex.brewing.example", water.getHoverName()))
                .addLoreLine(description("step.water"))
                .addLoreLine(description("step.base")));
        setStepArrow(builder, 1, "base");
        setLabelledStack(builder, 2, 4, new ItemStack(NirvanaItems.BONG), "example", "step.prepare");
        setStepArrow(builder, 3, "prepare");
        setLabelledStack(builder, 4, 4, awkward, "example", "step.effect");
        setStepArrow(builder, 5, "example");
        setLabelledStack(builder, 6, 4, swiftness, "example", "step.example");
        builder.set(8, 4, new GuiElementBuilder(Items.PAPER)
                .setName(Component.translatable("text.nirvana.polydex.brewing.example.title"))
                .addLoreLine(description("step.example"))
                .addLoreLine(description("example.note")));
    }

    private static void setLabelledStack(PageBuilder builder, int x, int y, ItemStack stack,
                                          String label, String hint) {
        builder.setIngredient(x, y, new RecipeIngredient(stack), element -> element
                .setName(Component.translatable("text.nirvana.polydex.brewing." + label, stack.getHoverName()))
                .addLoreLine(description(hint)));
    }

    private static void setStepArrow(PageBuilder builder, int x, String step) {
        builder.set(x, 4, new GuiElementBuilder(Items.ARROW)
                .setName(Component.translatable("text.nirvana.polydex.brewing.step." + step)));
    }

    private static Component description(String key) {
        return Component.translatable("text.nirvana.polydex.brewing." + key)
                .withStyle(ChatFormatting.GRAY).withStyle(style -> style.withItalic(false));
    }
}
