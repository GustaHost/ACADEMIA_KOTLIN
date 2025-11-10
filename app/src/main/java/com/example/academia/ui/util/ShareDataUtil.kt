package com.example.academia.ui.util

import android.content.Context
import android.content.Intent

fun shareText(context: Context, text: String, subject: String = "Compartilhamento do Meu Diário de Treinos") {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}