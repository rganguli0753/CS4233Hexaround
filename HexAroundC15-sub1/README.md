# README #

This assignment requires you to implement some features that you mostl ikely will need for your final project. These features will not be exposed in the final release, but are exposed for this assignment.

You should begin to develop a package structure and move some of the files in the `required` package to other packages according to your design.

I have provided you with an extended interface, `IHexAround1`. This adds the following methods:
- `getCreatureAt` that returns the name of the creature at a specified location on the board. Just the name is returned. There is nothing to indicate which player it belongs to.
- `hasProperty` given a location on the board, and a property, this method returns true or false depending upon whether the creature at that location on the board has the specified property.
- `isOccupied` given a location on the board, returns true or false depending upon whether the hex is ocupied.
- `canReach` given two locations on the board, returns true or false, depending upon whether the creature at the first location could reach the second location, given the maximum distance of the creature's abilities.

In addition, you will use the `HexAroundGameBuilder` to return an object that implements of the extended interface. You should use the Builder pattern to _build_ the object inthe builder, not the constructor of the returned object (the HexAroundFirstSubmission instance. I have modified the signature of the builder's `buildGameManager` method for this assignment.

You should also try to use some form of Factory Method or factory object to get instances of the correct creatures. This is not visible outside of your code, but is something to consider for your implementation.

My tests will also call the `PlaceCreature` method. When called, you will just place the creature piece on the board. None of my tests will try to put the piece on an occupied location. You can also assume that there will be no call to this method with a piece that is not in the configuration file.

Other requirements are specified in the code as comments on the methods to the IntelliJ project supplied.

You can use this to practice your TDD skills or not, but you should have code that is readable or undestandable. If, in the opinion of the grader, you have a complete hack that looks like it was thrown together at the last minute, you will have points taken off.

I have placed a single test in the project to get your started.

## Grading

Grading for this is solely based upon correctness. It should not be difficult for everyone to get all of the tests correct.