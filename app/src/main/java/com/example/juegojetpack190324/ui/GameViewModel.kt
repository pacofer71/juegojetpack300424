package com.example.juegojetpack190324.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.juegojetpack190324.data.GameUiState
import com.example.juegojetpack190324.data.MAX_NO_OF_WORDS
import com.example.juegojetpack190324.data.SCORE_INCREASE
import com.example.juegojetpack190324.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {


    private lateinit var palabraActual: String
    private var palabrasUsadas: MutableSet<String> = mutableSetOf()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userIntento by mutableStateOf("")
        private set

    init {
        initGame()
    }


    //----------------------------------------------------------------------------------------------
    fun initGame(){
        //palabrasUsadas.add("")
        palabrasUsadas.clear()
        _uiState.value=GameUiState(palabraDesordenadaActual = cogerYDesordenarPalabra())
    }

    //----------------------------------------------------------------------------------------------

    private fun desordenaPalabraActual(palabra: String): String {
        //iniciarPalabra()
        val temp = palabra.toCharArray()
        do {
            temp.shuffle()
        } while (String(temp) == palabra)

        return String(temp)
    }
    //----------------------------------------------------------------------------------------------
    private fun cogerYDesordenarPalabra(): String{
        palabraActual = allWords.random()
        return if (palabrasUsadas.contains(palabraActual)) {
            cogerYDesordenarPalabra()
        } else {
            palabrasUsadas.add(palabraActual)
            desordenaPalabraActual(palabraActual)
        }
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    fun checkIntentoUsuario(){
        //Log.d("CLIK--------------->", "Click en submit")
        if(userIntento.equals(palabraActual, ignoreCase = true)){
            val updateScore=_uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        }else{
            _uiState.update {
                currentState->currentState.copy(
                isGuessedWordWrong = true
                )
            }
        }
        updateUserIntento("")
    }
    //----------------------------------------------------------------------------------------------
    fun updateUserIntento(palabra: String){
        userIntento=palabra
    }
    //---------------------------------------------------------------------------------------------

    fun updateGameState(score: Int){
        if(palabrasUsadas.size== MAX_NO_OF_WORDS) {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    score = score,
                    isGameOver = true
                )
            }
        }else {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    palabraDesordenadaActual = cogerYDesordenarPalabra(),
                    score = score,
                    currentWordCount = it.currentWordCount.inc()
                )
            }
        }

    }
    //--------------------------------------------------------------------------------------------------
    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserIntento("")
    }
}

