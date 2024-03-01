package hexaround.game;

import hexaround.required.*;

public class Piece {
    private CreatureName creature;
    private PlayerName player;

    public Piece(CreatureName creature, PlayerName player) {
        this.creature = creature;
        this.player = player;
    }

    public CreatureName getCreature() {
        return creature;
    }

    public PlayerName getPlayer() {
        return player;
    }
}
