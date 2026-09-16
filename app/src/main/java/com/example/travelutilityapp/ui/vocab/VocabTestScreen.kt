package com.example.travelutilityapp.ui.vocab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.VocabRepository
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary


/** Full-screen flashcard test: shows the word only, records "몰라요ㅠ"/"알아요!" back into the vocab list. */
@Composable
fun VocabTestScreen(
    source: VocabTestSource,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { VocabRepository(context) }
    val testWords = remember(source) {
        source.testWords(
            context,
            repository.loadMyWords(),
            repository.loadDontKnowWords(),
            repository.loadExcludedCefrIds()
        )
    }
    var currentIndex by remember(testWords) { mutableStateOf(0) }

    fun recordAnswer(word: VocabWord, isKnown: Boolean) {

        if (!isKnown) {
            val current = repository.loadDontKnowWords()

            if (current.none {
                    it.word.equals(word.word, ignoreCase = true)
                }) {

                repository.saveDontKnowWords(
                    current + word
                )
            }
        } else {

            if (source == VocabTestSource.DONT_KNOW) {
                val current = repository.loadDontKnowWords()

                repository.saveDontKnowWords(
                    current.filterNot {
                        it.word.equals(word.word, ignoreCase = true)
                    }
                )
            }
        }
    }

    fun answer(word: VocabWord, isKnown: Boolean) {
        recordAnswer(word, isKnown)
        if (currentIndex + 1 >= testWords.size) {
            onClose()
        } else {
            currentIndex++
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YwBackground)
    ) {
        if (testWords.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.vocab_test_empty),
                    color = YwTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = YwPrimary, contentColor = Color.White)
                ) {
                    Text(stringResource(R.string.action_close))
                }
            }
        } else {
            val currentWord = testWords[currentIndex]
            val progress = (currentIndex + 1f) / testWords.size

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(YwSurface, CircleShape)
                            .border(1.dp, YwBorderSoft, CircleShape)
                            .clickable(onClick = onClose),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.cd_close),
                            tint = YwTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.vocab_test_progress, currentIndex + 1, testWords.size),
                        color = YwTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(modifier = Modifier.size(34.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(YwBorderSoft)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(6.dp)
                            .background(YwPrimary, RoundedCornerShape(3.dp))
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(YwSurface)
                        .border(1.dp, YwBorderSoft, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentWord.word,
                        color = YwTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(YwSurface)
                            .border(1.5.dp, YwBorderSoft, RoundedCornerShape(16.dp))
                            .clickable { answer(currentWord, isKnown = false) },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SentimentDissatisfied,
                            contentDescription = null,
                            tint = YwTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.vocab_test_dont_know),
                            color = YwTextSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(YwPrimary)
                            .clickable { answer(currentWord, isKnown = true) },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SentimentSatisfied,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.vocab_test_know),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun VocabTestScreenPreview() {
    TravelUtilityAppTheme {
        VocabTestScreen(
            // 프로젝트에 정의된 VocabTestSource의 더미/기본 구현체를 전달하세요.
            source = VocabTestSource.DONT_KNOW,    //MY, DONT_KNOW, CEFR
            onClose = {}
        )
    }
}