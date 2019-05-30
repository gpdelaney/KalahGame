# KalahGame
KalahGame made in java with an enbedded mongoDB for tests and a remote one hosted in mLabs

## Kalah Rules
Each of the two players has ​ **​ six pits​ ** ​ in front of him/her. To the right of the six pits, each player has a larger pit, his
Kalah or house.
At the start of the game, six stones are put in each pit.
The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.
When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

## Endpoints

This is a rest app that has to principal endpoints:

<h3>http://host:port/games</h3>
<h3>POST</h3>
Creates a new game and returns its ID in order to keep and start playing. The first one to start is the top player (Cups 1 to 6).
Bottom player has cups 8 to 13. Kalah's are numbered 7 and 14 for top and bottom players respectively.

<h3>http://host:port/games/{gameId}/pits/{cupId}</h3>
<h3>PUT</h3>
Makes a move, where the selected cup moves all the stones it has to the right. The game will continue until no stones are left in 
one of the player sides.

## Instructions to run

Download Repo

./gradlew bootRun

For tests
./gradlew clean build

## Components/Frameworks
<h3>SpringBoot</h3>
<h3>JUnit</h3>
<h3>MongDB(Embedded for testing and remote one for "production")</h3>
<h3>Gradle</h3>
