package dev.deepslate.fallacy.client.screen.race

import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.components.interactable.Button
import com.github.wintersteve25.tau.components.interactable.ListView
import com.github.wintersteve25.tau.components.utils.Text
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.Color
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.network.packet.SelectRacePacket
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.network.PacketDistributor

class RaceDescriptionUI(val race: Holder<Race>, val raceKey: String, val descriptionKey: String) : UIComponent {
    override fun build(layout: Layout, theme: Theme): UIComponent {
        val title = Text.Builder(Component.translatable(raceKey)).withColor(Color.WHITE)
        val description = Text.Builder(Component.translatable(descriptionKey)).withColor(Color.WHITE)

        val selectButton = Button.Builder().withOnPress { selectRace() }

        val container = ListView.Builder().build()
        return container
    }

    fun selectRace() {
        try {
            PacketDistributor.sendToServer(SelectRacePacket(race.key!!.location()))
        } catch (e: Exception) {
            Fallacy.LOGGER.error(e)
        }
    }
}