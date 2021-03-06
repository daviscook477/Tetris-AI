This project is a simple Tetris game controlled by an AI. The game runs in a window while the AI automatically places the pieces trying to clear as many lines as possible. The number of lines cleared is logged to the console.

The AI works by examining the piece that is falling and first determining all possible places that the piece could fall to. Then it temporarily places the piece where it would fall for each location and calculates a cost function on the board. In that way, the AI knows the cost of each possible move it could make and it then makes the cheapest move.

	> __NOTE:__ The AI doesn't technically look at all possible places - it actually only looks at places that can be reached by dropping the pieces straight down. It doesn't look at the fancy drop down and then move left or right cases.
	
This is what the application looks like when it's running:

![The example image could not be displayed!](/examples/example.png?raw=true "Tetris AI in Action")