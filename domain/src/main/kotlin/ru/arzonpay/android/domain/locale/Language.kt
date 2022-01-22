package ru.arzonpay.android.domain.locale

enum class Language(val code: String) {

    UZBEK_LATIN("en"),
    UZBEK_CYRILL("uz"),
    RUSSIAN("ru");

    companion object {
        fun getByCode(code: String?): Language {
            return values().firstOrNull { it.code == code } ?: UZBEK_LATIN
        }
    }
}