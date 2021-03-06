package game.effectstatus;

import card.PokemonCard;

/**
 * Author:  Eric(Haotao) Lai
 * Date:    2017-07-22
 * E-mail:  haotao.lai@gmail.com
 * Website: http://laihaotao.me
 */


public abstract class Effect {

    protected PokemonCard target;
    // effect fields
    protected boolean canAttack = true;
    protected boolean canRetreat = true;

    public Effect(PokemonCard target) {
        this.target = target;
    }

    public abstract void apply();

    public boolean remove() {
        return true;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public boolean isCanRetreat() {
        return canRetreat;
    }
}
