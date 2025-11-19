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
    <li>In First Option (1. New Game [NEW]) or Third Option (3. Continue / Load A Game [CONT][CONTINUE][LOAD]), Each Round:</li>
        <ol>
            <li>Player Naming Prompt: (First Option Only)</li>
                <ol>
                    <li>"1. Input Name [INPUT]": Manually Input Name For Both Player</li>
                    <li>"2. Random Name [RANDOM]": Randomly Assigned A Pre-defined Name For Both Player</li>
                </ol>
            <li>File loading prompt: (Third Option Only)</li>
                <ol>
                    <li>Input your record file name. (No File Extension): Input the file name you would like to load. Input the word "ESCAPE" to quit loading a file.</li>
                    <li>It will then load the file you want, return to file name inputting if any error on file.</li>
                </ol>
            <li>Option Selected Prompt:</li>
                <ol>
                    <li>Show the game board with the remaining piece with current position</li>
                    <li>"Possible Piece: ...": Select the available piece current player have by Number / Name</li>
                    <li>"Possible Option: ...": Select the utilities option by English Letter / Name</li>
                    <li>Depends on the option, there will be different output.</li>
                        <ol>
                            <li>If you select a piece (Part 1): It then show the board with possible movement (with [*])</li>
                            <li>If you select a piece (Part 2): "Possible Movement: ...": Select the Number or Direction / Option Full Name for input</li>
                            <li>If you select END: the game will ended immediately after your confirmation</li>
                            <li>If you select STATUS: It will display current board, and remaining piece for both players.</li>
                            <li>If you select BACK: It will ask and confirm if you need to take a step back (of the previous player), return to most appropriate (non step back) previous step.</li>
                            <li>If you select RECORD or SAVE (Part 1): It will direct you to file saving function, RECORD for replay, SAVE for save current game state</li>
                            <li>If you select RECORD or SAVE (Part 2): You may have to confirm you choice first, then need to input the file name without extension (avoid using the word "ESCAPE")</li>
                        </ol>
                    <li>Some of the option may include confirm page, input 1 or y to accept / 2 or n for deny(return to previous state of confirm)</li>
                </ol>
            <li>Notice: Only Make A Move and Take Step Back count a round, Other option may return to select option prompt or end the game (for end option).</li>
            <li>Ending Prompt</li>
                <ol>
                    <li>a player info of winner will displayed after that player reach their opponent's den</li>
                    <li>you will be given a chance to record the replay this full game.</li>
                </ol>
        </ol>
        <li>In Third Option,</li>
            <li>It will output no match if the file empty or carry nothing, then end the whole replay function.</li>
            <ol>
                <li>Replay in process: </li>
                    <ol>
                        <li>It will display the N-th Board of the recorded game, where N = round of a game match.</li>
                        <li>Under the board, It will display the N-th Move of the recorded game, where N = round of a game match. (Player Info, Piece Moved, Piece Changed)</li>
                        <li>Wait 5 second, then proceed to the next move</li>
                        <li>If a winner determined in this replay, it display total turn used and winning player info, then end the replay function.</li>
                    </ol>
            </ol>
        <li>In Forth Option,</li>
            <ol>
                <li>End the program immediately after select.</li>
            </ol>
</ol>