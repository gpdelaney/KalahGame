package com.backbase.kalah.model;

public enum PlayerIdentifier {
	
	TOP("TOP"),
	BOTTOM("BOTTOM");
	
	
	PlayerIdentifier(String player) {
		this.player = player;
	}

	private String player;
	
	public String getPlayer() {
		return player;
	};
	
	public static PlayerIdentifier getNextPlayer(PlayerIdentifier player) {
		if(player.equals(TOP)) {
			return BOTTOM;
		}else {
			return TOP;
		}
	}

}
