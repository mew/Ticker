package zone.nora.ticker

import com.google.gson.JsonObject
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.modcore.api.ModCoreAPI
import zone.nora.ticker.command.TickerCommand
import zone.nora.ticker.config.TickerConfig
import zone.nora.ticker.data.baseBazaarData
import zone.nora.ticker.data.getBazaarData
import zone.nora.ticker.hud.type.TickerHudElementType

@Mod(
    modid = "ticker",
    name = "BazaarTicker",
    version = "1.0",
    modLanguage = "kotlin",
    modLanguageAdapter = "net.modcore.api.utils.KotlinAdapter"
) object BazaarTicker {
    @EventHandler
    fun onPreInit(event: FMLPreInitializationEvent) {
        TickerConfig.initData()
        baseBazaarData = getBazaarData()
        baseBazaarData.keys.forEach {
            if (!TickerConfig.localDataObject.has(it)) {
                val obj = JsonObject()
                obj.addProperty("alias", "")
                obj.addProperty("shown", true)
                TickerConfig.localDataObject.add(it, obj)
            }
        }
    }

    @EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ModCoreAPI.getCommandRegistry().registerCommand(TickerCommand())
        ModCoreAPI.getHudRegistry().registerElement(TickerHudElementType)
    }
}