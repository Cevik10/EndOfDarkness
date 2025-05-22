package com.agile.endofdarknes


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoryScreen()
        }
    }
}

@Composable
fun StoryScreen(viewModel: StoryViewModel = hiltViewModel()) {
    val text = viewModel.getCurrentText()
    val choices = viewModel.getCurrentChoices()
    val stats = viewModel.stats.value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Health: ${stats.health}  Money: ${stats.money}  Fame: ${stats.fame}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text)
        Spacer(modifier = Modifier.height(16.dp))
        if (choices.isNotEmpty()) {
            LazyColumn {
                items(choices) { choice ->
                    Button(
                        onClick = { viewModel.selectChoice(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(choice.getString("text"))
                    }
                }
            }
        } else {
            Text("Hikaye burada sona erdi. / The story ends here.")
        }
    }
}

data class Stats(val health: Int, val money: Int, val fame: Int)