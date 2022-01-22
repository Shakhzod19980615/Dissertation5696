package ru.arzonpay.android.ui.util

fun String.isValidCardNumber(): Boolean {
    val filtered = this.filter(Char::isDigit)
    return filtered.length == 16 && checkLuhn(filtered)
}

private fun checkLuhn(numbers: String): Boolean {
    val nDigits = numbers.length
    var sum = numbers.lastOrNull()?.digitToIntOrNull() ?: return false
    val parity = nDigits % 2
    for (i in 0..nDigits - 2) {
        var digit = numbers[i].digitToIntOrNull() ?: return false
        if (i % 2 == parity) {
            digit *= 2
        }
        if (digit > 9) {
            digit -= 9
        }
        sum += digit
    }
    return (sum % 10) == 0
}

fun String.isValidUzPhoneNumber(): Boolean {
    val uzPhoneLength = 9
    return filter(Char::isDigit).length == uzPhoneLength
}