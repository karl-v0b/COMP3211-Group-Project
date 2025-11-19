### Developer Manual for The Jungle Game

#### Development Information:
<ol>
    <li>Platform: macOS</li>
    <li>Programming Language: Java</li>
    <li>Version of JDK: 23</li>
    <li>IDE: IntelliJ IDEA CE</li>
</ol>

#### Code Explanation:
<ol>
    <li>Class "Main": Main menu of the game, user select option and execute corresponding method for each option</li>
    <li>Class "Game": Info and execution of the game, apply directly to option 1 (New Game) and option 3 (Load Game) in Main Menu</li>
    <ol>
        <li>Fields of "Game"</li>
            <ol>
                <li>Board b: Game Board</li>
                <li>Player[] players = new Player[2]: 2 players' info in game</li>
                <li>Move[] moves = new Move[1024]: Moves by players in each round of game</li>
                <li>int mCounter = 0: Counter for moves</li>
                <li>boolean cont = true: see if the game should continue, where a winner not determined</li>
                <li>int turn_count = 1: Counter for turns</li>
                <li>transient Scanner s: No effect on game, just to avoid initialize input Scanner in every function</li>
            </ol>
        <li>Constructor: Initialize a new game with necessary game info properly assigned (as a new game)</li>
        <li>Method "process": Handling game match (rounds played) execution, return winner info</li>
        <li>Method "namePlayer": Handling player naming when a new game</li>
        <li>Method "select": Handling user input options in each round, return piece (zero or positive) utilities option (negative)</li>
        <li>Method "Direction_select": Handling user input direction after selecting piece, return direction (zero or positive) return to select (negative)</li>
        <li>Method "showStatus": Show the game status in current round, including current board and remaining pieces of both player</li>
        <li>Method "Confirm" and any method with "confirm": Ask the confirmation of previous action, return Accept (1) Deny[Return to previous state] (-1)</li>
        <li>Method "showMoves": Show the moves by each player in rounds up until current point of game.</li>
        <li>Method "record"/"save": record or save until / in the current round of game, return finishing function (1) / escaping function (-1) </li>
    </ol>
    <li>Class "Replay": Replay of the recorded game using "Move Object in String written into the file", apply directly to option 2 (Replay Game) in Main Menu</li>
    <ol>
        <li>Fields of "Replay"</li>
            <ol>
                <li>int mCounter: Counter for move</li>
                <li>String[] movement: Array of Full String Form of Move Object</li>
                <li>Board b: game board used in the replay, similar to game</li>
            </ol>
        <li>Constructor: Apply the string form of move object into replay class</li>
        <li>Method "Replaying": Open a new board, and make move automatically based on the string form of the Move object[must be a valid move]</li>
        <li>Method "getEatenString": translate the name of piece (object) into string, with the case of null piece be "Nothing", return the string</li>
    </ol>
    <li>Enum "Animals": Constants of different piece with their attribute (rank and initial position (fixed) to game board)</li>
    <ol>
        <li>Enum Constants of "Animals": Each Piece</li>
        <li>Values of "Animals": Attribute of Piece in (rank, initial_row, initial_column)</li>
    </ol>
    <li>Class "Board": Game board used during a game for new, loaded, replay (all function of game)</li>
    <ol>
        <li>Fields of "Board"</li>
            <ol>
                <li>Piece[] p1 = new Piece[8]: All pieces owned by player 1 (in initial of game)</li>
                <li>Piece[] p2 = new Piece[8]: All piece owned by player 2 (in initial of game)</li>
                <li>int p1n = 8: Number of pieces owned by player 1 (in initial of game)</li>
                <li>int p2n = 8: Number of pieces owned by player 2 (in initial of game)</li>
                <li>Square[][] boardMap = new Square[9][7]; Full board used in the game (nothing on it)</li>
            </ol>
        <li>Constructor: Create piece for both player, add new square each space in array, assigned pieces into appropriate square location of board</li>
        <li>Method "showBoard": show the board with the position of square (fixed) and pieces (dynamic)</li>
        <li>Method "selectedBoard": Handle the next possible step of the piece, given the selected piece, return array of possible location for 4 direction</li>
        <li>Method "cleanSelectables": Clean the square of board with [*], which shows the next possible step of the piece</li>
        <li>Method "change": Apply the change after player confirm its move, handle any status of piece and board after the changed, return changed state</li>
        <li>Method "revert": Special "change" method for taking step back, after player confirm its back move, return changed state</li>
        <li>Method "simpleMove": Special "change" method for replay function, return 1 if round ended with winner 0 if not</li>
    </ol>
    <li>Class "Move": Piece move made by either player, used to write the file to record the game (game replay)</li>
    <ol>
        <li>Field of "Move"</li>
        <ol>
            <li>Player player: the player (object) who made this move</li>
            <li>int align: the alignment of the player making this move</li>
            <li>Piece piece: source piece making the move</li>
            <li>Piece eaten: destination piece being affected by source piece because of the move, eaten if move / returned if back</li>
            <li>String move: direction of move</li>
            <li>String type: either the player move is piece's moving forward or piece's take step back</li>
        </ol>
        <li>Constructor: Initial Move Based on the each round of "piece's move" or "piece's step back"</li>
        <li>Method "getPieceName"/"getEatenPiece": return the name of piece in string, with the case of null piece be String "NULL"</li>
        <li>Method "flipMove": return the reverse version of piece's move, used in piece's step back</li>
    </ol>
    <li>Class "Piece": Animals' representation as piece on the board</li>
    <ol>
        <li>Field of "Piece"</li>
        <ol>
            <li>Animals piece: Piece as Animal Enum Constants(Name)</li>
            <li>String name: String representation of piece's name</li>
            <li>int rank: Rank of the piece</li>
            <li>int align: Player alignment of the piece</li>
            <li>boolean trapped: see if the piece is on trap, different interaction from other piece</li>
            <li>int positionR / int positionC: Piece location on board</li>
        </ol>
        <li>Constructor: Initialize piece when board initialized</li>
        <li>Method "getNameNoAlign": return string purely for its name, while original default String is name(align)</li>
        <li>Method "eatable": check if a piece can eat another target piece and return boolean, complement to next method "moveTo"</li>
        <li>Method "moveTo": check if a piece can move to target square, return target square location, complemented by the previous method "eatable"</li>
        <li>Method "getMove": return array of row and column, used in take step back (detail info in override section)</li>
        <li>Overridden Method or Addition Field/Method of "Piece"</li>
        <ol>
            <li>Additional Field "boolean swim = false" in Class "Rat": used to indicate if rat is in river or not</li>
            <li>Overridden Method "eatable" in Class "Rat"/"Elephant": Add handling special case of eating rule of rat and elephant</li>
            <li>Overridden Method "moveTo" in Class "Rat": Add handling special case of capture rule of rat in/not-in river</li>
            <li>Additional Method "flipSwim" in Class "Rat": Used to change rat swimming status when entering and leaving river</li>
            <li>Overridden Method "moveTo"/"getMove" in Class "Lion"/"Tiger": Add handling river jump case of moving rule</li>
            <li>Additional Method "checkRiverMouse": check if there is rat in-between the river when (complementing) "moveTo" is into river may be able to cross</li>
        </ol>
    </ol>
    <li>Class "Player": Store player information during the match of the game</li>
    <ol>
        <li>Field of "Player"</li>
        <ol>
            <li>String id: Name of player</li>
            <li>int align: Alignment of player (1 or 2 only)</li>
            <li>Piece[] pieces: Remaining piece at that specific point of the game</li>
            <li>int pN: Pieces currently owned count (counter for pieces)</li>
        </ol>
        <li>Constructor: Player object created when a new game object initializing</li>
        <li>Method "showPiece": Show all remaining piece at that specific point of the game</li>
        <li>Method "findPiece": find location of a specific piece exist in the player's remaining piece at that specific point of the game, return index or fail(-1)</li>
        <li>Method "removePiece": remove a specific piece in the player's remaining piece at that specific point of the game, used in player's move</li>
        <li>Method "inRange": check if index out of range of from zero to total pieces currently owned count, return true if in range, or false if not</li>
        <li>Method "addPiece": add a specific piece in the player's remaining piece at that specific point of the game, used in taking step back</li>
    </ol>
    <li>Class "Square": A square with info (including environment and piece) on the game board</li>
    <ol>
        <li>Field of "Square"</li>
        <ol>
            <li>String type: environment type of the square</li>
            <li>int align: player's/neutral alignment of that square</li>
            <li>String current: String representation (type + align) of the square on board</li>
            <li>boolean selectable = false: see if the square is selectable after player's selection of piece and deciding the direction of piece's movement</li>
            <li>Piece piece = null: Piece on that square</li>
        </ol>
        <li>Constructor: Set up proper environment type, alignment and String representation on board for that square during board initializing</li>
        <li>Method "getTypeNoAlign": return string of environment type without align, in default type string, it has a alignment</li>
        <li>Method "assignPiece": put a piece in that square, and make current more proper when piece name conflict with type name</li>
        <li>Method "clearPiece": remove a piece in that square, and remove the piece name for current</li>
        <li>Method "flipSelectable": reverse the selectable and change current representation if it is selectable (current + [*])</li>
    </ol>
</ol>