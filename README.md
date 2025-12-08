# softwareEngineeringGame

**Description**

A game which I programmed for a University course. 
The client spawns on a map, of which one half is created by my side, the other half is created by the enemy half. The server puts them together and this serves as the playing field.

**Objective**

The objective is for each client to find the treasure and the enemy castle. The treasure only spawns on the clients own half and the castle serves as the spawning point, so it only spawns on the others half.

**Rules**

- The client cannot move towards a water field. If it does so, it immediately looses.
- Moving on grass fields consumes one turn. 
- Moving onto mountain fields consumes two turns and moving from mountain fields consumes two turs.
- There is a total of 150 possible rounds
- A total of 10 minutes playing time
- The client cannot request the game state too quickly, else it looses.

**Git**

I programmed the game on a seperate account, but moved it here to make it public. Because of this, there are not past commits of the developement.
