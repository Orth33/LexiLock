package com.lexlock.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.lexlock.domain.repository.WordRepository
import com.lexlock.domain.usecase.GetWordOfTheDayUseCase
import com.lexlock.utils.AssetLoader
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WordRepository,
    private val getWordOfTheDayUseCase: GetWordOfTheDayUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Seed database if empty
            val allWords = repository.getAllWords().first()
            if (allWords.isEmpty()) {
                val words = AssetLoader.loadWordsFromJson(applicationContext, "words_seed.json")
                repository.insertWords(words)
            }

            // In a real app, you might trigger a notification here
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun startPeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<DailyUpdateWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "DailyUpdateWork",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
