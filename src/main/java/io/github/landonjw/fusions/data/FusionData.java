package io.github.landonjw.fusions.data;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class FusionData {

    private Pokemon base;
    private Pokemon sacrifice;

    public Pokemon getBase() {
        return base;
    }

    public void setBase(Pokemon base) {
        this.base = base;
    }

    public Pokemon getSacrifice() {
        return sacrifice;
    }

    public void setSacrifice(Pokemon sacrifice) {
        this.sacrifice = sacrifice;
    }
} 