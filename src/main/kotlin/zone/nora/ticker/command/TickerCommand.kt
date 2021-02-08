package zone.nora.ticker.command

import net.modcore.api.ModCoreAPI
import net.modcore.api.commands.Command
import net.modcore.api.commands.DefaultHandler
import net.modcore.api.utils.GuiUtil
import zone.nora.ticker.config.TickerConfig

class TickerCommand : Command("ticker") {
    @DefaultHandler
    fun handle() {
        val gui = try {
            TickerConfig.gui()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        if (gui == null) {
            ModCoreAPI.getMinecraftUtil().sendMessage("BazaarTicker", "Failed to open config gui :(")
        } else {
            GuiUtil.open(gui)
        }
    }
}