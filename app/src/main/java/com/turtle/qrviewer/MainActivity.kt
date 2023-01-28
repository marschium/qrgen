package com.turtle.qrviewer

import android.graphics.Color
import android.os.Bundle
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.turtle.qrviewer.model.AppDatabase
import com.turtle.qrviewer.model.QREntry
import com.turtle.qrviewer.model.QRViewModelViewmodel
import com.turtle.qrviewer.ui.theme.QrViewerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// <a href="https://www.flaticon.com/free-icons/qr-code" title="qr code icons">Qr code icons created by Earthz Stocker - Flaticon</a>

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customerViewModel = ViewModelProvider(this).get(QRViewModelViewmodel::class.java)
        setContent {
            QrViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Default(customerViewModel)
                }
            }
        }
    }
}

@Composable
fun Default(viewModel: QRViewModelViewmodel) {

    val coroutineScope = rememberCoroutineScope()
    val coroutineScope2 = rememberCoroutineScope()
    var focusManager = LocalFocusManager.current;

    var input by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(coroutineScope2) {
        input = viewModel.getDefault().data
    }

    if (input != null) {
        var text by rememberSaveable { mutableStateOf(input.orEmpty()) }
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("QR data") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        viewModel.update(QREntry("default", text))
                    }
                })
            )

            if (!text.isEmpty()) {
                val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 256)
                qrgEncoder.setColorBlack(Color.WHITE)
                qrgEncoder.setColorWhite(Color.BLACK)
                val bitmap = qrgEncoder.getBitmap()
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize() )
            }
            else {
                Text(text = "nothing to see here", modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center))
            }
        }
    }
    else {
        Text(text = "loading...", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center))
    }


}