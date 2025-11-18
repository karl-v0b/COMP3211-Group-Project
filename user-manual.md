### User Manual for The Jungle Game

#### Input Command Support:
<ol>
    <li>Number / Character Specified In Prompt</li>
    <li>Full Keyword Specified in Prompt</li>
</ol>
Notice: All English letter / word input is case-insensitive.

#### Input Command Format:
<ol>
    <li>1 word only (Number / English Character / English Wording)</li>
</ol>
Notice: There shouldn't be anything not related (including space) in between a word.

#### Invalid Command Handling:
<ol>
    <li>The Program will simply return to the input state, again</li>
    <li>The Program will return to the previous state of the input state</li>
</ol>

#### Step-By-Step Instruction:
<ol>
    <li>In Main Menu of The Game, 4 option can be Selected:</li>
        <ol>
            <li>"1. New Game [NEW]": Start a new game</li>
            <li>"2. Replay A Game [REPLAY]": Start a replay of the recorded game by its file (.record)</li>
            <li>"3. Continue / Load A Game [CONT][CONTINUE][LOAD]": Resume a saved game by its file (.jungle)</li>
            <li>"4. Exit [EXIT]": End the program.</li>
        </ol>
    <li>In First Option (1. New Game [NEW]), Each Round:</li>
        <ol>
            <li>Player Naming Prompt:</li>
                <ol>
                    <li>"1. Input Name [INPUT]": Manually Input Name For Both Player</li>
                    <li>"2. Random Name [RANDOM]": Randomly Assigned A Pre-defined Name For Both Player</li>
                </ol>
            <li>Option Selected Prompt:</li>
                <ol>
                    <li>Show the game board with the remaining piece with current position</li>
                    <li>"Possible Piece: ...": Select the available piece current player have by Number / Name</li>
                        <ol>
                            <li>It then show the board with possible movement (with [*])</li>
                            <li>"Possible Movement: ...": 
                        </ol>
                    <li>"Possible Option: ...": Select the utilities option by English Letter / Name</li>
                </ol>
        </ol>
</ol>