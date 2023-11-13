package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration


@Composable
fun getUid(): String {
    val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()
    return keyValueStorage.observableUid?.collectAsState("")?.value.toString()
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
