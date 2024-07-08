package com.mazarini.resa.ui.util

import android.content.Context
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale


object Translator {
    fun translate(text: String, result: (TranslationResult) -> Unit) {
        val tag = Locale.getDefault().language
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SWEDISH)
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(tag) ?: TranslateLanguage.ENGLISH)
            .build()
        val swedishTranslator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        swedishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                swedishTranslator.translate(text).addOnSuccessListener {
                    result(TranslationResult.Success(it))
                }.addOnFailureListener {
                    result(TranslationResult.Error(it.message.orEmpty()))
                }
            }
            .addOnFailureListener { exception ->
                result(TranslationResult.Error(exception.message.orEmpty()))
            }
    }

    fun isTranslationPossible(context: Context, result: (Boolean) -> Unit) {
        if (context.isWifiConnected().not()) {
            val modelManager = RemoteModelManager.getInstance()
            val currentLanguage = Locale.getDefault().language
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { models ->
                    result (
                        listOf(currentLanguage, TranslateLanguage.SWEDISH).all { requiredLanguage ->
                            models.any { it.language == requiredLanguage }
                        }
                    )
                }
                .addOnFailureListener {
                    result(false)
                }
        } else {
            result(true)
        }
    }
}

sealed class TranslationResult {
    data class Success(val result: String) : TranslationResult()
    data class Error(val message: String) : TranslationResult()
}