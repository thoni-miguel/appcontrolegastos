package br.ceub.app_controle_gastos.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import java.text.NumberFormat
import java.util.*


/**
 * Formata uma string numérica para o formato de moeda brasileira (R$).
 *
 * Exemplo: "1234" → "R$12,34"
 */
fun formatToBRL(input: String): String {
    val digitsOnly = input.replace(Regex("[^\\d]"), "") //Remove tudo que não for número
    val parsed = digitsOnly.toLongOrNull() ?: 0L
    val currency = parsed / 100.0
    val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return formatter.format(currency)
}

/**
 * Solicita foco automaticamente ao composable quando ele for exibido.
 *
 * Útil para focar campos de texto ao abrir diálogos ou formulários.
 */
@Composable
fun Modifier.autoFocus(): Modifier {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    return this.focusRequester(focusRequester)
}