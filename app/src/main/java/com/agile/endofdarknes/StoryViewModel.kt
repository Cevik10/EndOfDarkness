package com.agile.endofdarknes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    private val _currentNodeId = mutableStateOf("beginning")
    val currentNodeId: State<String> = _currentNodeId

    private val _stats = mutableStateOf(Stats(100, 50, 10))
    val stats: State<Stats> = _stats

    private val currentLanguage = if (Locale.getDefault().language == "en") "en" else "en"
    private val storyData: JSONObject? = storyRepository.loadStory()

    init {
        storyData?.let {
            val initialStats = it.getJSONObject("initial_stats")
            _stats.value = Stats(
                initialStats.getInt("health"),
                initialStats.getInt("money"),
                initialStats.getInt("fame")
            )
        }
    }

    private fun getNode(nodeId: String): JSONObject? {
        val nodes = storyData?.getJSONArray("nodes")
        for (i in 0 until (nodes?.length() ?: 0)) {
            val node = nodes?.getJSONObject(i)
            if (node?.getString("id") == nodeId) {
                return node
            }
        }
        return null
    }

    fun getCurrentText(): String {
        val node = getNode(_currentNodeId.value)
        return node?.optJSONObject("locale")
            ?.optJSONObject(currentLanguage)
            ?.optString("text") ?: "Düğüm bulunamadı / Node not found"
    }

    fun getCurrentChoices(): List<JSONObject> {
        val node = getNode(_currentNodeId.value)
        val choicesArray = node?.optJSONObject("locale")
            ?.optJSONObject(currentLanguage)
            ?.optJSONArray("choices") ?: return emptyList()
        return (0 until choicesArray.length()).map { choicesArray.getJSONObject(it) }
    }

    fun selectChoice(choice: JSONObject) {
        choice.optJSONObject("effects")?.let { effects ->
            _stats.value = _stats.value.copy(
                health = _stats.value.health + effects.optInt("health", 0),
                money = _stats.value.money + effects.optInt("money", 0),
                fame = _stats.value.fame + effects.optInt("fame", 0)
            )
        }
        val nextNodeId = choice.optString("next")
        if (nextNodeId.isNotEmpty()) {
            _currentNodeId.value = nextNodeId
        }
    }
}