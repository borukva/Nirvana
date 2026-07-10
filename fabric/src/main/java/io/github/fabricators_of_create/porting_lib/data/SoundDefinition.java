package io.github.fabricators_of_create.porting_lib.data;

import net.minecraft.resources.ResourceLocation;

public class SoundDefinition {
    public enum SoundType {
        SOUND, EVENT;
    }

    public static class Sound {
        private Sound() {}

        public static Sound of() {
            return new Sound();
        }

        public Sound sound(ResourceLocation location) {
            return this;
        }

        public Sound volume(float volume) {
            return this;
        }

        public Sound pitch(float pitch) {
            return this;
        }

        public Sound weight(int weight) {
            return this;
        }

        public Sound stream(boolean stream) {
            return this;
        }

        public Sound type(SoundType type) {
            return this;
        }
    }

    private SoundDefinition() {}

    public static SoundDefinition definition() {
        return new SoundDefinition();
    }

    public SoundDefinition subtitle(String subtitle) {
        return this;
    }

    public SoundDefinition with(Sound... sounds) {
        return this;
    }
}
