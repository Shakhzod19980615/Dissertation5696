package ru.arzonpay.android.ui.mvi.reducer

/** Редюсером не является, но предоставляет метод для валидации номера телефона в сущности [Reducer]. */
interface PhoneValidationReducer {

    /**
     * Произвести валидацию мобильного номера телефона (передавать нефильтрованный ввод пользователя!).
     * Если передана ненулевая [customMask], то валидация будет происходить по ней.
     * Возвращает true -- если валиден.
     * */
    fun validatePhone(
        phoneNumber: String,
        onInvalidAction: () -> Unit = { /*empty body*/ }
    ): Boolean {
        return phoneNumber.filter(Char::isDigit)
            .matches(PHONE_REGEX)
            .also { if (!it) onInvalidAction() }
    }

    private companion object {
        const val PHONE_PATTERN =
            "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}\$"
        val PHONE_REGEX = PHONE_PATTERN.toRegex()
    }
}
