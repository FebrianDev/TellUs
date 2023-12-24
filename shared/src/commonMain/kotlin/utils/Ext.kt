package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration


fun getUid(): String {
    val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()
    return keyValueStorage.observableIdUser
}

fun getTime(date: String): String {

    try {
        val instant1 = Instant.parse(date)

        val instant2 = Clock.System.now()

        val diff: Duration = instant2 - instant1
        val getTime = diff.inWholeMinutes
        var finalTime = getTime.toString() + "m\nago"
        if (getTime >= 60) {
            finalTime = "${getTime / 60}h\nago"
        }
        if (getTime >= 1440) {
            finalTime = "${getTime / 1440}d\nago"
        }

        return finalTime
    } catch (e: Exception) {
        return "0"
    }
}

fun getDateNow(): String {
    return Clock.System.now().toString()
}

fun String.getDate(date: String): String {
    val convert = Instant.parse(date)
    val toLocalDate = convert.toLocalDateTime(TimeZone.currentSystemDefault())
    return toLocalDate.date.toString()
}

fun String.getTimeChat(): String {
    val convert = Instant.parse(this)
    val toLocalDate = convert.toLocalDateTime(TimeZone.currentSystemDefault())
    return toLocalDate.time.toString().substring(0, 5)
}

fun generatedFakeName(): String {
    val prefixes = listOf("Mr.", "Ms.", "Dr.", "Miss", "Mrs.")
    val firstNames = listOf("John", "Jane", "Alex", "Emily", "Chris", "Olivia", "Michael", "Sophia", "David", "Emma", "Daniel", "Isabella", "Matthew", "Ava", "Ethan", "Mia", "Liam", "Grace", "Nathan", "Zoe")
    val middleInitials = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
    val lastNames = listOf("Smith", "Johnson", "Davis", "Brown", "Miller", "Wilson", "Jones", "Taylor", "Anderson", "Harris", "Garcia", "Martinez", "Jackson", "Moore", "Lee", "Perez", "Kim", "Walker", "Cooper", "Baker")

    val randomPrefix = prefixes.random()
    val randomFirstName = firstNames.random()
    val randomMiddleInitial = middleInitials.random()
    val randomLastName = lastNames.random()

    return "$randomPrefix $randomFirstName $randomMiddleInitial. $randomLastName"
}