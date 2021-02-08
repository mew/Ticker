package zone.nora.ticker.data

import com.google.gson.GsonBuilder
import net.hypixel.api.HypixelAPI
import net.hypixel.api.reply.skyblock.BazaarReply
import zone.nora.ticker.config.TickerConfig
import kotlin.math.absoluteValue

// bazaar data on game launch
var baseBazaarData: Map<String, BazaarReply.Product> = emptyMap()

val gson = GsonBuilder().setPrettyPrinting().create()

fun getBazaarData(): Map<String, BazaarReply.Product> {
    val api = HypixelAPI(null)
    val x = api.bazaar.get().products
    api.shutdown()
    return x
}

fun BazaarReply.Product.Status.compareWith(base: BazaarReply.Product.Status, type: Comparison): String {
    if (productId != base.productId) return ""

    val basePrice = if (type == Comparison.BUY) base.buyPrice else base.sellPrice
    val productPrice = if (type == Comparison.BUY) buyPrice else sellPrice

    if (basePrice == 0.0 || productPrice == 0.0) return ""

    val diff = productPrice - basePrice
    val state = when {
        diff > 0 -> State.INCREASE
        diff < 0 -> State.DECREASE
        else -> State.NO_CHANGE
    }

    if (state == State.NO_CHANGE) {
        return if (TickerConfig.showNoChange) "$productId (NO CHANGE)" else ""
    }

    val percent = diff.absoluteValue / basePrice
    val percentStr = if (TickerConfig.showPercents) " (${if (state == State.INCREASE) '+' else '-'}${percent.format()}%)" else ""

    return "$productId ${getArrow(percent, state)} ${diff.format(true)}$percentStr"
}

fun Double.format(prefix: Boolean = false): String {
    val d = String.format("%.3f", this)
    val p = if (prefix && this > 0) "+" else ""
    return if (d == "0.000") "${p}0.001" else if (d == "-0.000") "-0.001" else "$p$d"
}

private fun getArrow(percent: Double, state: State): String {
    val s = if (state == State.INCREASE) {
        if (percent >= 10) {
            // ▲▼
            "\u00a72\u00a7l\u25b2"
        } else {
            "\u00a7a\u00a7l\u25b2"
        }
    } else {
        if (percent > -10) {
            "\u00a7c\u00a7l\u25bc"
        } else {
            "\u00a74\u00a7l\u25bc"
        }
    }
    return "$s\u00a7r"
}

enum class Comparison {
    BUY,
    SELL
}

enum class State {
    INCREASE,
    DECREASE,
    NO_CHANGE
}