package com.example.juegojetpack190324.ui


import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.juegojetpack190324.data.GameUiState
import com.example.juegojetpack190324.data.MAX_NO_OF_WORDS
import com.example.juegojetpack190324.ui.theme.Blue300
import com.example.juegojetpack190324.ui.theme.Blue400

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Cabecera(modifier = Modifier.align(Alignment.TopEnd))
        Juego(modifier = Modifier.align(Alignment.Center), gameViewModel)
    }
}

//--------------------------------------------------------------------------------------------------
@Composable
fun Cabecera(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(imageVector = Icons.Default.Clear, contentDescription = "", modifier.clickable {
        activity.finishAffinity()
    })
}

//--------------------------------------------------------------------------------------------------
@Composable
fun Juego(modifier: Modifier, gameViewModel: GameViewModel) {

    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Palabras Desordenadas",
            //textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            //modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        GameLayout(
            onUserIntentoChange = { gameViewModel.updateUserIntento(it) },
            onKeyboardDone = { gameViewModel.checkIntentoUsuario() },
            isGuessWrong = gameUiState.isGuessedWordWrong,
            userIntento = gameViewModel.userIntento,
            palabraDesordenadaActual = gameUiState.palabraDesordenadaActual,
            wordCount = gameUiState.currentWordCount
        )
        Spacer(modifier = Modifier.height(32.dp))
        Botones(gameViewModel, gameUiState)
        Spacer(modifier = Modifier.height(32.dp))
        Score(gameUiState)
    }

}

//--------------------------------------------------------------------------------------------------
@Composable
fun GameLayout(
    onUserIntentoChange: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    isGuessWrong: Boolean,
    userIntento: String,
    palabraDesordenadaActual: String,
    wordCount: Int = 0
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(2.dp),

        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                String.format("%d / %d", wordCount, MAX_NO_OF_WORDS),
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .background(Color.Blue)
                    .align(Alignment.End)
                    .clip(shape = RoundedCornerShape(12.dp)),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = palabraDesordenadaActual,
                //textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                //modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Averigua la palabra de arriba desordenada",
                //textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                //modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userIntento,
                onValueChange = {
                    if (it.length <= palabraDesordenadaActual.length) {
                        onUserIntentoChange(it)
                    }
                },
                placeholder = {
                    Text(text = "Escriba una palabra")
                },
                maxLines = 1,
                singleLine = true,
                isError = isGuessWrong,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorIndicatorColor = Color.Red,
                    //errorTextColor = Color.Red,
                    errorLabelColor = Color.Red,
                    errorContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardActions = KeyboardActions(onDone = { onKeyboardDone }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = {
                    if (isGuessWrong) {
                        Text("Palabra Incorrecta")
                    } else {
                        Text("Escriba una palabra")
                    }
                }
            )
            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

//-------------------------------------------------------------------------------------------------
@Composable
fun Botones(gameViewModel: GameViewModel, gameUiState: GameUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { gameViewModel.checkIntentoUsuario() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue400,
                disabledContainerColor = Blue300,
                contentColor = Color.White,
                disabledContentColor = Color.White

            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Enviar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { gameViewModel.skipWord() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.White,
                contentColor = Color.Blue

            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Saltar")
        }
    }
    if (gameUiState.isGameOver) {
        FinalScoreDialog(score = gameUiState.score, onPlayAgain = { gameViewModel.initGame() })
    }
}

//--------------------------------------------------------------------------------------------------
@Composable
fun Score(gameUiState: GameUiState) {
    Text(
        text = String.format(" SCORE: %d ", gameUiState.score),
        fontSize = 24.sp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .background(Color.LightGray)

    )
}
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


@Composable
private fun FinalScoreDialog(score: Int, onPlayAgain: () -> Unit, modifier: Modifier = Modifier) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "¿Juego Nuevo?")
            }
        },
        title = { Text("FELICIDADES", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text(text = String.format("Su puntuacion fue de: %d", score)) },
        dismissButton = {
            TextButton(onClick = { activity.finish() }) {
                Text("SALIR")
            }
        },

    )

}

