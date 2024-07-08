package com.mazarini.resa.ui.util

import android.content.Context
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.domain.model.journey.filteredMessage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun translateWarnings(
    context: Context,
    coroutine: CoroutineScope,
    warnings: List<Warning>,
    result: (List<Warning>) -> Unit,
) {
    val translated = mutableListOf<Warning>()

    Translator.isTranslationPossible(context) { isPossible ->
        if (isPossible) {
            try {
                coroutine.launch {
                    warnings.forEach { warning ->
                        val deferred = async {
                            val completionPromise = CompletableDeferred<TranslationResult>()
                            Translator.translate(warning.filteredMessage) { translationResult ->
                                completionPromise.complete(translationResult)
                            }
                            val promiseResult = completionPromise.await()
                            when (promiseResult) {
                                is TranslationResult.Success -> {
                                    warning.copy(message = promiseResult.result)
                                }

                                is TranslationResult.Error -> {
                                    context.showMessage(promiseResult.message)
                                    throw Exception(promiseResult.message)
                                }
                            }
                        }
                        translated.add(deferred.await())
                        if (translated.size == warnings.size) {
                            result(translated)
                        }
                    }
                }
            } catch (e: Exception) {
                context.showMessage(context.getString(R.string.translate_error))
            }
        } else {
            context.showMessage(context.getString(R.string.wifi_language_error))
        }
    }
}