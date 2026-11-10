package ua.knu.maksym_pashchenko.movieexplorer.ui.common

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Throwable.toUserMessage() : String {
    return when(this) {
       is HttpException -> {
           when (code()) {
               401 -> "Помилка авторизації сервісу"
               404 -> "Фільм не знайдено"
               429 -> "Забагато запитів. Спробуйте пізніше"
               in 500..599 -> "Сервер тимчасово недоступний"
               else -> "Не вдалося виконати запит"
           }
       }

        is UnknownHostException -> "Не вдалося підключитися. Перевірте інтернет з'єднання"

        is SocketTimeoutException -> "Сервер не відповідає. Спробуйте ще раз"

        is IOException -> "Помилка мережі. Спробуйте ще раз"

        else -> "Сталася непередбачена помилка"
    }
}