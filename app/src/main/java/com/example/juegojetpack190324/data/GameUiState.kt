package com.example.juegojetpack190324.data

data class GameUiState(
    val palabraDesordenadaActual: String="",
    val isGuessedWordWrong: Boolean = false,
    val score: Int=0,
    val currentWordCount: Int=1,  //para contar palabras que llevamosjugadas
    val isGameOver: Boolean = false
    )
