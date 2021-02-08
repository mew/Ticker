package zone.nora.ticker.config

import club.sk1er.elementa.state.BasicState
import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import zone.nora.ticker.data.gson
import java.io.File
import kotlin.concurrent.fixedRateTimer

object TickerConfig : Vigilant(File("./ticker/ticker.toml")) {
    //@Property(
    //    type = PropertyType.TEXT,
    //    name = "Hypixel API Key",
    //    description = "Your Hypixel API Key, required to get bazaar data. Do not share this with anyone.",
    //    category = "shrug",
    //)
    //var hypixelApiKey: String = ""

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Percent",
        description = "Show percent increase/decreases next to value amount",
        category = "HUD"
    )
    var showPercents = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Divider",
        description = "Draw a divider between the buy ticker and the sell ticker",
        category = "HUD"
    )
    var drawDivider = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Text Shadow",
        description = "Render text shadow on the ticker",
        category = "HUD"
    )
    var textShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show items with no change",
        description = "that",
        category = "HUD"
    )
    var showNoChange = true

    val textShadowState = BasicState(textShadow)

    private val dataFile: File = File("./ticker/bazaar_items.json")

    init {
        initialize()

        registerListener(::textShadow, {
            textShadowState.set(it)
        })
    }

    var localDataObject: JsonObject = JsonObject()

    fun initData() {
        var content = "{}"
        if (dataFile.exists()) {
            content = dataFile.readText()
        } else {
            dataFile.createNewFile()
        }
        localDataObject = try {
            JsonParser().parse(content).asJsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            JsonObject()
        }

        fixedRateTimer(period = 30 * 1000) { dataFile.writeText(gson.toJson(localDataObject)) }
    }
}