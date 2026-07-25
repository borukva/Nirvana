package galena.nirvana.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class PeaceDurationCriterion extends AbstractCriterion<PeaceDurationCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, int duration) {
        this.trigger(player, conditions -> conditions.matches(duration));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, NumberRange.IntRange duration) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                NumberRange.IntRange.CODEC.optionalFieldOf("duration", NumberRange.IntRange.ANY).forGetter(Conditions::duration)
                        )
                        .apply(instance, Conditions::new)
        );

        public boolean matches(int duration) {
            return this.duration.test(duration);
        }
    }
}
