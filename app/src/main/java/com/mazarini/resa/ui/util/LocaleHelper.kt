import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.mazarini.resa.BuildConfig
import com.mazarini.resa.global.analytics.EventType
import com.mazarini.resa.global.analytics.loge
import java.util.Locale

object LocaleHelper {

    fun availableLanguages(): Map<String, String> {
        return try {
            BuildConfig.TRANSLATED_LOCALES
                .associateWith { Locale(it).displayLanguage }
                .toList()
                .sortedBy { it.second }
                .toMap()
        } catch (e: Exception) {
            e.printStackTrace()
            loge(
                EventType.LANGUAGE_LIST.name,
                mapOf("Failed to get available languages" to e.toString()),
            )
            mapOf()
        }
    }

    fun getNewLocaleContext(context: Context, languageCode: String): Context {
        return try {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            context.createConfigurationContext(config)
        } catch (e: Exception) {
            e.printStackTrace()
            loge(
                EventType.LANGUAGE_CHANGE.name,
                mapOf(languageCode to "Failed to update locale to $languageCode")
            )
            context
        }
    }
}
