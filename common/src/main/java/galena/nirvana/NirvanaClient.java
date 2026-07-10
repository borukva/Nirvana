package galena.nirvana;

// ItemColor was removed in MC 1.21.4+. Item tinting is now handled via ItemTintSource
// registered in item model JSON definitions. The potion bong overlay color is handled
// through the vanilla potion_contents tint source in the model.

public class NirvanaClient {
    // No client-side color providers needed; tint is handled via model JSON
}
