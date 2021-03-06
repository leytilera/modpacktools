package ley.anvil.modpacktools.commands

import ley.anvil.modpacktools.CONFIG
import ley.anvil.modpacktools.command.CommandReturn
import ley.anvil.modpacktools.command.CommandReturn.Companion.fail
import ley.anvil.modpacktools.command.CommandReturn.Companion.success
import ley.anvil.modpacktools.command.ICommand
import ley.anvil.modpacktools.command.LoadCommand

@LoadCommand
object Init : ICommand {
    override val name: String = "init"
    override val helpMessage: String = "initializes the MPT dev environment (currently only creates config file)"
    override val needsConfig: Boolean = false
    override val needsModpackjson: Boolean = false

    override fun execute(args: Array<out String>): CommandReturn {
        if(CONFIG.exists)
            return fail("Config exists")
        CONFIG.copyConfig()
        return success("Config Created")
    }
}