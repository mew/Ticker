package zone.nora.ticker.hud.type

import club.sk1er.elementa.dsl.pixels
import net.modcore.api.gui.hud.CommonOptions
import net.modcore.api.gui.hud.HudElementType
import net.modcore.api.gui.hud.SimpleContextMenuOptionData
import zone.nora.ticker.hud.element.TickerHudElement

object TickerHudElementType : HudElementType<TickerHudElement> {
    override fun constructElement(): TickerHudElement = TickerHudElement()

    override fun getDescription(): String = "Bazaar Ticker"

    override fun getElementClass(): Class<TickerHudElement> = TickerHudElement::class.java

    override fun getElementName(): String  = "Bazaar Ticker"

    override fun shouldSizeInformationSave(): Boolean = true

    override fun getSimpleContextMenuOptions(): List<SimpleContextMenuOptionData<TickerHudElement>> = listOf(
        CommonOptions.createResizeOption(false, minWidth = 50.pixels(), minHeight = 20.pixels())
    )
}