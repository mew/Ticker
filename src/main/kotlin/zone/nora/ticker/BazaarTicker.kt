package zone.nora.ticker

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.modcore.api.ModCoreAPI
import net.modcore.api.utils.Multithreading
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
        Multithreading.runAsync(Runnable { baseBazaarData = getBazaarData() })

        TickerConfig.initialize()
    }

    @EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ModCoreAPI.getCommandRegistry().apply {
            registerCommand(TickerCommand())
        }
        ModCoreAPI.getHudRegistry().apply {
            registerElement(TickerHudElementType)
        }
    }
}