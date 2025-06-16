# PokeFusion Mod

This mod adds a fusion mechanic between Pixelmons to your Minecraft server. Players can combine two Pokémon to transfer the stats of one to the other. The mod works with both commands and an optional graphical user interface (GUI).

## Features

*   **Pokémon Fusion:** Combine two Pokémon to enhance the stats of the base Pokémon.
*   **User Interface:** An easy-to-use GUI for fusion operations.
*   **Command Support:** Command-based control for those who prefer not to use the GUI or for automation.
*   **Economy Integration:** Integration with your server's economy plugins (any plugin that supports commands) for fusion operations.
*   **Flexible Configuration:** Fully configure fusion rules, costs, and stat transfers.

## Usage

There are two ways to initiate a fusion:

1.  **Commands:**
    *   `/pokefusion`: Displays the currently selected Pokémon.
    *   `/pokefusion 1 <party slot>`: Selects the base Pokémon for the fusion (a number from 1-6 in your party).
    *   `/pokefusion 2 <party slot>`: Selects the Pokémon that will be sacrificed.
    *   `/pokefusion confirm`: Confirms and performs the fusion with the selected Pokémon.
    *   `/pokefusion decline`: Cancels the selection.
    *   `/pokefusion help`: Shows help commands.

2.  **Graphical User Interface (GUI):**
    *   If enabled in the configuration, you can open the fusion menu, place your Pokémon in the respective slots, and press the "Fuse" button to perform the operation.

## Configuration

All mod settings are managed from the `fusions-common.toml` file in your server's `config` folder.

### General Settings
*   `EnableGUI`: Enables/disables the graphical interface for fusion. (Default: `true`)
*   `CooldownSeconds`: The cooldown in seconds for the `/pokefusion` command. (Default: `30`)
*   `FusionCost`: The cost for each fusion operation. (Default: `1000.0`)
*   `FusionItem`: The item required for fusion. (e.g., `"minecraft:diamond"`. Leave empty to disable.)
*   `EconomyCommand`: The command to execute to take money from the player.
    *   **{player}**: Represents the player's name.
    *   **{cost}**: Represents the fusion cost.
    *   Default (Vault compatible): `"eco take {player} {cost}"`

### Fusion Rules
*   `FusionMode`: Determines which Pokémon can be fused with each other (`SPECIES_ONLY`, `EGG_GROUP`, `ANY`).
*   `MaxFuseCount`: The maximum number of times a Pokémon can be fused. (`-1` means unlimited).

### Transfer Settings
These settings determine which stats are transferred from the sacrificed Pokémon to the base Pokémon and in what proportion.
*   `ivGainPercentage`: The percentage of IVs gained from the sacrificed Pokémon.
*   `inheritShiny`: Whether the base Pokémon should become shiny if the sacrificed one is shiny.
*   `inheritHiddenAbility`: Transfer of the hidden ability.
*   ...and other stat transfer settings.

## Installation

1.  Make sure you have the correct version of Minecraft Forge installed on your server.
2.  Install the latest version of the Pixelmon Mod in your `mods` folder.
3.  Place the `.jar` file of this mod into your server's `mods` folder.
4.  Start the server. The `config/fusions-common.toml` file will be created after the first launch.

## For Developers (Installation from Source)

This project uses the Minecraft Forge MDK (Mod Development Kit) structure.

1.  Open your terminal/command line in the project folder.
2.  Set up for your preferred IDE:
    *   **Eclipse:** Run the command `gradlew genEclipseRuns` and then import the project into Eclipse as an "Existing Gradle Project".
    *   **IntelliJ IDEA:** Open the `build.gradle` file in IDEA to import the project. Then run the command `gradlew genIntellijRuns` and refresh the Gradle project.
3.  If you encounter problems like missing libraries, you can refresh the local cache with `gradlew --refresh-dependencies` and clean the project files with `gradlew clean` (your code will not be affected). 