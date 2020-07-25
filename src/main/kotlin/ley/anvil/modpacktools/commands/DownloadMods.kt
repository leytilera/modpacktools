package ley.anvil.modpacktools.commands

import ley.anvil.addonscript.wrapper.LinkInstPair
import ley.anvil.modpacktools.Main.MPJH
import ley.anvil.modpacktools.command.CommandReturn
import ley.anvil.modpacktools.command.CommandReturn.fail
import ley.anvil.modpacktools.command.CommandReturn.success
import ley.anvil.modpacktools.command.ICommand
import ley.anvil.modpacktools.command.LoadCommand
import ley.anvil.modpacktools.util.FileDownloader
import ley.anvil.modpacktools.util.FileDownloader.AsyncDownloader.ExistingFileBehaviour.OVERWRITE
import ley.anvil.modpacktools.util.FileDownloader.AsyncDownloader.ExistingFileBehaviour.SKIP
import ley.anvil.modpacktools.util.FileDownloader.downloadAsync
import ley.anvil.modpacktools.util.Util.sanitizeURL
import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util.stream.Collectors.toMap

@LoadCommand
class DownloadMods : ICommand {
    override fun getName(): String = "downloadmods"
    override fun getHelpMessage(): String = "Downloads all mods. force always downloads files even if they are already present Syntax: <OutDir> [force]"

    override fun execute(args: Array<out String>): CommandReturn {
        if(!args.checkArgs())
            return fail("Invalid Args")

        val json = MPJH.json
        downloadAsync(
            json.defaultVersion.getRelLinks(json.indexes, "client", false, "internal.dir:mods", null).stream()
                .collect(toMap<LinkInstPair, URL, File>(
                    {sanitizeURL(URL(it.link))},
                    {File(args[1], Paths.get(URL(it.getLink()).path).fileName.toString())}
                )),
            //Synced to prevent the exception being printed too late
            {r: FileDownloader.AsyncDownloader.DownloadFileTask.Return ->
                synchronized(this) {
                    println("${r.responseCode} ${r.responseMessage} ${r.url} ${r.file}")
                    if(r.exception != null)
                        println(r.exception.message)
                }
            },
            if("force" in args) OVERWRITE else SKIP
        )
        return success()
    }

    private fun Array<out String>.checkArgs(): Boolean = this.size >= 2 && this.elementAtOrNull(2)?.equals("force") ?: true
}