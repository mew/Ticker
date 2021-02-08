package zone.nora.ticker.hud.element

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.components.UIContainer
import club.sk1er.elementa.components.UIText
import club.sk1er.elementa.constraints.SiblingConstraint
import club.sk1er.elementa.constraints.animation.Animations
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.ScissorEffect
import club.sk1er.vigilance.gui.VigilancePalette
import net.hypixel.api.reply.skyblock.BazaarReply
import net.modcore.api.gui.hud.HudElement
import zone.nora.ticker.config.TickerConfig
import zone.nora.ticker.data.Comparison
import zone.nora.ticker.data.baseBazaarData
import zone.nora.ticker.data.compareWith
import zone.nora.ticker.data.getBazaarData
import zone.nora.ticker.hud.type.TickerHudElementType

class TickerHudElement : HudElement(TickerHudElementType) {
    var bazaarData: Map<String, BazaarReply.Product> = getBazaarData()

    init {
        constrain {
            width = 100.pixels()
            height = 30.pixels()
        }
    }

    val container = UIContainer().constrain {
        x = 0.pixels()
        y = 0.pixels()
        height = 100.percent()
        width = 100.percent()
    } effect ScissorEffect() childOf this

    private val buyText: UIText
    private val sellText: UIText
    var buy = ""
    var sell = ""

    init {
        for (product in bazaarData.values) {
            val pid = product.productId
            val baseStatus = (baseBazaarData[pid] ?: continue).quickStatus
            val qs = product.quickStatus
            buy += "${qs.compareWith(baseStatus, Comparison.BUY)}   "
            sell += "${qs.compareWith(baseStatus, Comparison.SELL)}   "
        }

        UIText("Buy:").bindShadow(TickerConfig.textShadowState).constrain {
            x = 0.pixels()
            y = 0.pixels()
            textScale = .4f.pixels()
        } childOf container

        buyText = UIText(buy).bindShadow(TickerConfig.textShadowState).constrain {
            x = 0.pixels()
            y = SiblingConstraint(1f)
        } childOf container

        if (TickerConfig.drawDivider) UIBlock(VigilancePalette.DIVIDER).constrain {
            x = 0.pixels()
            y = SiblingConstraint(1f)
            width = 100.percent()
            height = 1.pixel()
        } childOf container

        UIText("Sell:").bindShadow(TickerConfig.textShadowState).constrain {
            x = 0.pixels()
            y = SiblingConstraint(.5f)
            textScale = .4f.pixels()
        } childOf container

        sellText = UIText(sell).bindShadow(TickerConfig.textShadowState).constrain {
            x = 0.pixels()
            y = SiblingConstraint(1f)
        } childOf container
    }

    override fun afterInitialization() {
        super.afterInitialization()

        buyText.animate {
            setXAnimation(Animations.LINEAR, buy.width() / 10, 0.pixels(alignOutside = true))
        }
        sellText.animate {
            setXAnimation(Animations.LINEAR, sell.width() / 10, 0.pixels(alignOutside = true))
        }

        /*Multithreading.runAsync(Runnable {
            buyText.animate {
                setXAnimation(Animations.LINEAR, buy.width() / 10, 0.pixels(alignOutside = true))
            }
            sellText.animate {
                setXAnimation(Animations.LINEAR, sell.width() / 10, 0.pixels(alignOutside = true))
            }
        })*/
    }
}