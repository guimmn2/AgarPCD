#Table with additional info for commits
##main branch
- 1
	- added 'createThreads' in src/game/Game.java which will create #NUM_SLAYERS 'PlayerThreads';
	
	- added class 'PlayerThread' which will create a 'slayer' (homage to doom, a PhoneyHumanPlayer) and then call the 'addPlayerToGame';
	
	- altered the method 'addPlayerToGame' so it utilizes Conditions Variables. 