package ley.anvil.modpacktools.util

import ley.anvil.addonscript.v1.AddonscriptJSON
import ley.anvil.addonscript.wrapper.ASWrapper
import java.io.File
import java.io.FileReader

class ModpackJsonHandler(val modpackJsonFile: File) {
    //Null if no file exists
    val asWrapper: ASWrapper? get() {
        return if(modpackJsonFile.exists()) {
            val reader = FileReader(modpackJsonFile)
            val ret = ASWrapper(AddonscriptJSON.read(reader, AddonscriptJSON::class.java))
            reader.close()
            ret
        } else null
    }

    val json: AddonscriptJSON?
        get() = asWrapper?.json
}