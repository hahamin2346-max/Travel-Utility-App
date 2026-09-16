package com.example.travelutilityapp.ui.vocab

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.VocabRepository
import com.example.travelutilityapp.ui.components.AppTab
import com.example.travelutilityapp.ui.components.AppTabBar
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwPrimarySoft
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

import androidx.compose.ui.tooling.preview.Preview
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
private enum class VocabViewFilter { DONT_KNOW, CEFR }

@Composable
fun VocabScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToChecklist: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    onStartTest: (VocabTestSource) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { VocabRepository(context) }

    var words by remember { mutableStateOf(repository.loadMyWords()) }
    var excludedCefrIds by remember { mutableStateOf(repository.loadExcludedCefrIds()) }
    var selectedTab by remember { mutableStateOf(AppTab.Vocab) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingWord by remember { mutableStateOf<VocabWord?>(null) }
    var showTestSourceSheet by remember { mutableStateOf(false) }
    // null = normal screen; otherwise filtered to only show that source's words.
    var activeFilter by remember { mutableStateOf<VocabViewFilter?>(null) }
    // Let the phone's system back button close the filtered view instead of leaving the screen.
    BackHandler(enabled = activeFilter != null) { activeFilter = null }
    // Word tapped in a filtered view, pending the "move to learning list" confirm/cancel panel.
    var wordToMove by remember { mutableStateOf<VocabWord?>(null) }
    // Word tapped in a filtered view, pending delete confirmation.
    var wordPendingDelete by remember { mutableStateOf<VocabWord?>(null) }

    fun updateWords(update: (List<VocabWord>) -> List<VocabWord>) {
        val updated = update(words)
        words = updated
        repository.saveMyWords(updated)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YwBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val filter = activeFilter
            if (filter == null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.nav_vocab),
                            color = YwTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.vocab_screen_subtitle),
                            color = YwTextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwPrimary, RoundedCornerShape(20.dp))
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.vocab_test_title),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.vocab_test_subtitle, words.size),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .background(YwSurface, RoundedCornerShape(14.dp))
                                .clickable { showTestSourceSheet = true }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.vocab_test_start),
                                color = YwPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                                tint = YwPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.vocab_section_title),
                            color = YwTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(YwPrimary, CircleShape)
                                .clickable { showAddDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = stringResource(R.string.cd_add_vocab),
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    if (words.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(YwSurface, RoundedCornerShape(20.dp))
                                .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.vocab_empty), color = YwTextSecondary, fontSize = 13.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(390.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(YwSurface)
                                .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                itemsIndexed(
                                    items = words,
                                    key = { _, word -> word.id }
                                ) { index, word ->

                                    VocabRow(
                                        word = word,
                                        onClick = { editingWord = word }
                                    )

                                    if (index != words.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(YwBorderSoft)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .background(YwSurface, RoundedCornerShape(14.dp))
                                .border(1.dp, YwBorderSoft, RoundedCornerShape(14.dp))
                                .clickable { activeFilter = VocabViewFilter.DONT_KNOW },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SentimentDissatisfied,
                                contentDescription = null,
                                tint = YwTextSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = stringResource(R.string.vocab_filter_dont_know),
                                color = YwTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .background(YwPrimarySoft, RoundedCornerShape(14.dp))
                                .clickable { activeFilter = VocabViewFilter.CEFR },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = YwPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = stringResource(R.string.vocab_filter_CEFR),
                                color = YwPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
            } else {
                val filteredWords = when (filter) {
                    VocabViewFilter.DONT_KNOW ->
                        repository.loadDontKnowWords()

                    VocabViewFilter.CEFR ->
                        CefrVocabulary.words(context).filterNot { it.id in excludedCefrIds }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(YwSurface, CircleShape)
                                .border(1.dp, YwBorderSoft, CircleShape)
                                .clickable { activeFilter = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.cd_back),
                                tint = YwTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = stringResource(
                                if (filter == VocabViewFilter.CEFR) R.string.vocab_filter_CEFR else R.string.vocab_filter_dont_know
                            ),
                            color = YwTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (filteredWords.isEmpty()) {
                        val emptyMessage = if (filter == VocabViewFilter.CEFR) {
                            R.string.vocab_cefr_empty
                        } else {
                            R.string.vocab_filter_empty
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(YwSurface, RoundedCornerShape(20.dp))
                                .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(emptyMessage), color = YwTextSecondary, fontSize = 13.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(YwSurface)
                                .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                itemsIndexed(
                                    items = filteredWords,
                                    key = { _, word -> word.id }
                                ) { index, word ->

                                    VocabRow(
                                        word = word,
                                        onClick = { wordToMove = word }
                                        //onDelete = { wordPendingDelete = word }
                                    )

                                    if (index != filteredWords.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(YwBorderSoft)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AppTabBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        AppTab.Home -> onNavigateHome()
                        AppTab.Schedule -> onNavigateToSchedule()
                        AppTab.Checklist -> onNavigateToChecklist()
                        AppTab.Budget -> onNavigateToBudget()
                        else -> {}
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }

    if (showTestSourceSheet) {
        VocabTestSourceSheet(
            onDismiss = { showTestSourceSheet = false },
            onSelect = { source ->
                showTestSourceSheet = false
                onStartTest(source)
            }
        )
    }

    if (showAddDialog) {
        VocabAddDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { word, meaning ->
                updateWords { list -> list + VocabWord(word = word, meaning = meaning) }
                showAddDialog = false
            }
        )
    }

    editingWord?.let { vocabWord ->
        VocabEditDialog(
            vocabWord = vocabWord,
            onDismiss = { editingWord = null },
            onSave = { newWord, newMeaning ->
                updateWords { list ->
                    list.map { if (it.id == vocabWord.id) it.copy(word = newWord, meaning = newMeaning) else it }
                }
                editingWord = null
            },
            onDelete = {
                updateWords { list -> list.filterNot { it.id == vocabWord.id } }
                editingWord = null
            }
        )
    }

    wordToMove?.let { target ->
        AlertDialog(
            onDismissRequest = { wordToMove = null },
            title = { Text(stringResource(R.string.vocab_move_to_learning_title)) },
            text = { Text(stringResource(R.string.vocab_move_to_learning_message, target.word)) },
            confirmButton = {
                TextButton(onClick = {
                    updateWords { list ->
                        if (list.any { it.word.equals(target.word, ignoreCase = true) }) {
                            list
                        } else {
                            list + VocabWord(word = target.word, meaning = target.meaning)
                        }
                    }
                    wordToMove = null
                }) {
                    Text(stringResource(R.string.action_confirm), color = YwPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { wordToMove = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    wordPendingDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { wordPendingDelete = null },
            title = { Text(stringResource(R.string.vocab_delete_confirm_title)) },
            text = { Text(stringResource(R.string.vocab_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    when (activeFilter) {
                        VocabViewFilter.DONT_KNOW -> updateWords { list -> list.filterNot { it.id == target.id } }
                        VocabViewFilter.CEFR -> {
                            val updated = excludedCefrIds + target.id
                            excludedCefrIds = updated
                            repository.saveExcludedCefrIds(updated)
                        }
                        null -> {}
                    }
                    wordPendingDelete = null
                }) {
                    Text(stringResource(R.string.action_delete), color = YwPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { wordPendingDelete = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VocabScreenPreview() {
    TravelUtilityAppTheme {
        VocabScreen()
    }
}