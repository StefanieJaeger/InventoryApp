import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(searchText: MutableState<String>, getSuggestions: (String) -> List<String>) {
    var active by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchText.value,
        onQueryChange = { searchText.value = it },
        onSearch = { active = false },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = { Text("Search the list") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Icon(Icons.Default.Close,
            modifier = Modifier.clickable(onClick = {
                active = false
                searchText.value = ""
            }),
            contentDescription = null)
       },
    ) {
        for (suggestion in getSuggestions(searchText.value)) {
            ListItem(
                headlineContent = { Text(suggestion) },
                modifier = Modifier
                    .clickable {
                        searchText.value = suggestion
                        active = false
                    }
                    .fillMaxWidth()
            )
        }
    }
}
