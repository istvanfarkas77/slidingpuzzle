#SLIDING PUZZLE

I've used the following articles/pages to give a solution:

[article #1](http://www.cs.brandeis.edu/~storer/JimPuzzles/ZPAGES/zzzQuzzle.html)

[article #2](https://blog.goodaudience.com/solving-8-puzzle-using-a-algorithm-7b509c331288)

#Assumptions
The pieces in the puzzle holds only color information - dark gray (d), gray (g), empty white (E). 
Beside the color I have appended a discriminator-value in order to visualize certain pieces.

#Proposal
Based on the articles I tried to implement an algorithm that builds a weighted graph of the possible routes, and then looks for a route that has minimal cost.

#Major steps:
1. The main class com.foo.Quzzle is initialized with the starting and the termination state.
2. The Quzzle#buildSolutionGraph method iteratively walks through all possible boards by evaluating valid moves in each step. 
As a result we gain a tree of boards, where each node is aware of its predecessor (from where it is reached by making a move), and all the possible successors with assigned f-scores.
3. The Quzzle#buildSolutionGraph can be executed two ways:
First is without A* search, simply with brute force. 
Second is with using A* search, when while building the graph, the optimal path is defined by considering the best f-scores.
4. The code can be tried by executing the JUnit test com.foo.QuzzleTest by an IDE.
5. The code can be compiled by an IDE or be gradle from command line.

#Models:
1. Board - Represents a particular state of puzzle board, and acts as a node in the graph. That is each node maintains its predecessor and successors.
2. Piece - Expresses a piece on the board with its coordinates, dimensions and color.
3. Empty - A special piece that represents an empty cell on the board.
4. EmptyPair - Holds the information of the two empty cells. Acts as the root of analysis of possible moves.
5. Direction - An enumeration of all possible direction a cell can be moved.
6. Move - Represents the movement of a single or double sided piece toward a direction.

#Defects and faults:
- For the sake of simplicity I have used a single class (com.foo.Quzzle) with several inner classes, and I haven't used getters and setters. 
  The helper lines are only commented out, so any time they can switced on to make debugging easier and more descriptive.
- My code design might be far from the minimal and most effective representation of the problem, but I tried to set up classes/models which enables further extension. 
- The field and method accessibility are too permissive, but easy to use.
- The encapsulation is violated, but the code became simpler by this.
- The code is full of pseudo-java-docs that won't be necessary in a production-grade code.
- I failed to solve it with A* search. Probably my f-score definition is not appropriate. With brute force I can solve the puzzle in ~ 200 steps.

#Example output
[An output with using brute force](output_brute_force.txt)

[An output with using A* search](output_a_star_search.txt)
