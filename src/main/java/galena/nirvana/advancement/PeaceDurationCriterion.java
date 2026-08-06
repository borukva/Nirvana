package galena.nirvana.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class PeaceDurationCriterion extends SimpleCriterionTrigger<PeaceDurationCriterion.Conditions> {

    public void trigger(ServerPlayer player, int duration) {
        this.trigger(player, conditions -> conditions.matches(duration));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints duration) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                MinMaxBounds.Ints.CODEC.optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(Conditions::duration)
                        )
                        .apply(instance, Conditions::new)
        );

        public boolean matches(int duration) {
            return this.duration.matches(duration);
        }
    }
}
