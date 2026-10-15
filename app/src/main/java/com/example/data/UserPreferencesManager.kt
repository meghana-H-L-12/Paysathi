package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.models.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "paysaathi_preferences")

class UserPreferencesManager(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("selected_language")
        val KEY_TEXT_SIZE = stringPreferencesKey("text_size_scale")
        val KEY_HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_completed")
        val KEY_FAMILY_NAME = stringPreferencesKey("family_contact_name")
        val KEY_FAMILY_PHONE = stringPreferencesKey("family_contact_phone")
    }

    val selectedLanguage: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val code = preferences[KEY_LANGUAGE] ?: AppLanguage.ENGLISH.code
        AppLanguage.fromCode(code)
    }

    val textSizeScale: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_TEXT_SIZE] ?: "regular" // "regular", "large", "xlarge"
    }

    val isHighContrast: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_HIGH_CONTRAST] ?: false
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_ONBOARDING_DONE] ?: false
    }

    val familyContactName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_FAMILY_NAME] ?: "Priya (Daughter)"
    }

    val familyContactPhone: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_FAMILY_PHONE] ?: "1930"
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language.code
        }
    }

    suspend fun setTextSizeScale(scale: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TEXT_SIZE] = scale
        }
    }

    suspend fun setHighContrast(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_HIGH_CONTRAST] = enabled
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ONBOARDING_DONE] = completed
        }
    }

    suspend fun updateFamilyContact(name: String, phone: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FAMILY_NAME] = name
            preferences[KEY_FAMILY_PHONE] = phone
        }
    }
}
