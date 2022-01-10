package ru.arzonpay.android.base.util

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private const val DAY_MONTH_YEAR_TIME_FORMATTER_PATTERN = "d MMMM yyyy, HH:mm"
private const val DAY_MONTH_TIME_FORMATTER_PATTERN = "d MMMM, HH:mm"
private const val TIME_FORMATTER_PATTERN = "HH:mm"
private const val DAY_MONTH_YEAR_DOT_FORMATTER_PATTERN = "dd.MM.yyyy"
private const val DATE_MONTH_FORMATTER_PATTERN = "d MMMM"
private const val DATE_MONTH_YEAR_FORMATTER_PATTERN = "d MMMM yyyy"
private const val YEAR_MONTH_FORMATTER_PATTERN = "yyyy-MM"
private const val CARD_MONTH_YEAR_FORMATTER_PATTERN = "MM/yy"
private const val REVIEW_DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss"
private const val DEVICE_DATE_TIME_FILTER_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val WEEK_NAME_DATE_TIME_PATTERN = "EEE, d MMM yyyy HH:mm:ss 'GMT'"

val DEFAULT_LOCALE = Locale("ru")

val DEFAULT_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    .withLocale(DEFAULT_LOCALE)

val UTC_TIME_ZONE = ZoneId.of("UTC")

/**
 * Метод парсинга строки с датой и временем в объект [LocalDateTime].
 *
 * @return если в качестве параметра передан null или пустая строка, то метод вернет null;
 * иначе - объект [LocalDateTime].
 */
fun tryParseDateTime(rawDateTime: String?): LocalDateTime? {
    if (rawDateTime.isNullOrEmpty()) {
        Logger.d("Attempt to parse empty or null string!")
        return null
    }

    val resultDateTimeList = try {
        LocalDateTime.parse(rawDateTime, DEFAULT_DATE_FORMATTER)
    } catch (e: Throwable) {
        null
    }

    return resultDateTimeList ?: run {
        Logger.d("Unable to parse rawDateTime ($rawDateTime)")
        null
    }
}

/** Метод для форматирования даты в определенную ([DateTimeFormatter]'ом) строку. */
fun formatDateToString(date: LocalDateTime?, formatter: DateTimeFormatter): String {
    date ?: return EMPTY_STRING
    return date.format(formatter)
}

/** Метод для форматирования даты в определенную ([DateTimeFormatter]'ом) строку. */
fun formatDateToString(date: YearMonth?, formatter: DateTimeFormatter): String {
    date ?: return EMPTY_STRING
    return date.format(formatter)
}

/**Конвертирует unix timestamp на [LocalDateTime] если не удалось то возвращает текущее время*/
fun tryConvert(milliseconds: Long?): LocalDateTime {
    milliseconds ?: return LocalDateTime.now()
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())
}
