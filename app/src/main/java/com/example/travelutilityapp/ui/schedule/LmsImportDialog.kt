package com.example.travelutilityapp.ui.schedule

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.parseLmsScheduleResponse
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

private const val LMS_ENTRY_URL = "https://lms.jicportal.com/s/mn_front.html"

private const val FETCH_SCRIPT = """
(function() {
    fetch('/s/portal/schedule_ajax.html', {
        method: 'POST',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: 'req_type=get_schedule_list'
    }).then(function(r) { return r.text(); })
      .then(function(text) { YeoWonBridge.onScheduleResult(text); })
      .catch(function(e) { YeoWonBridge.onScheduleError(String(e)); });
})();
"""

private class ScheduleJsBridge(
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun onScheduleResult(json: String) {
        mainHandler.post { onResult(json) }
    }

    @JavascriptInterface
    fun onScheduleError(message: String) {
        mainHandler.post { onError(message) }
    }
}

/**
 * Lets the user log into lms.jicportal.com inside an embedded WebView, then (on tapping
 * "가져오기") runs the schedule-list fetch as JS inside that same authenticated page —
 * this keeps Cloudflare's bot check happy since it never leaves the real browser context.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LmsImportDialog(
    onDismiss: () -> Unit,
    onImported: (List<ScheduleEntry>) -> Unit,
    modifier: Modifier = Modifier
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isFetching by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val noEntriesError = stringResource(R.string.lms_import_error_no_entries)
    val failedErrorTemplate = stringResource(R.string.lms_import_error_failed)

    val bridge = remember {
        ScheduleJsBridge(
            onResult = { json ->
                isFetching = false
                val entries = parseLmsScheduleResponse(json)
                if (entries.isEmpty()) {
                    errorMessage = noEntriesError
                } else {
                    onImported(entries)
                }
            },
            onError = { message ->
                isFetching = false
                errorMessage = String.format(failedErrorTemplate, message)
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = modifier.fillMaxSize(), color = YwBackground) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YwSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(YwBackground, CircleShape)
                            .clickable(onClick = onDismiss),
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
                        text = stringResource(R.string.lms_import_title),
                        color = YwTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        onClick = {
                            errorMessage = null
                            isFetching = true
                            webView?.evaluateJavascript(FETCH_SCRIPT, null)
                        },
                        enabled = !isFetching,
                        colors = ButtonDefaults.buttonColors(containerColor = YwPrimary, contentColor = Color.White)
                    ) {
                        Text(
                            text = stringResource(
                                if (isFetching) R.string.lms_import_loading else R.string.lms_import_action
                            ),
                            fontSize = 13.sp
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.lms_import_instructions),
                    color = YwTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YwSurface)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = YwPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwSurface)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Box(modifier = Modifier.weight(1f).background(YwBorderSoft)) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                webViewClient = WebViewClient()
                                addJavascriptInterface(bridge, "YeoWonBridge")
                                loadUrl(LMS_ENTRY_URL)
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { view -> webView = view }
                    )
                }
            }
        }
    }
}
