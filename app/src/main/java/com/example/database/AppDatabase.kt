package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SafetyCheckEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun safetyCheckDao(): SafetyCheckDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paysaathi_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.safetyCheckDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: SafetyCheckDao) {
                if (dao.getCount() == 0) {
                    val initialChecks = listOf(
                        SafetyCheckEntity(
                            title = "BESCOM Electricity Bill",
                            description = "Scanned QR matched official Karnataka electricity provider account.",
                            amount = 1240.0,
                            isSafe = true,
                            statusBadge = "Safe to Pay (₹1,240)",
                            timeLabel = "Today, 10:15 AM",
                            payeeOrSender = "bescom.utility@karnataka.gov",
                            category = "UTILITY"
                        ),
                        SafetyCheckEntity(
                            title = "Lottery SMS Alert",
                            description = "SMS: \"Claim ₹50,000 cash prize immediately by sharing OTP\".",
                            amount = 50000.0,
                            isSafe = false,
                            statusBadge = "Blocked • Fake Impersonation Scam",
                            timeLabel = "Yesterday",
                            payeeOrSender = "+91 98765 43210",
                            category = "PHISHING"
                        ),
                        SafetyCheckEntity(
                            title = "Nandini Milk Booth Store",
                            description = "Verified merchant QR scanned at neighborhood store.",
                            amount = 145.0,
                            isSafe = true,
                            statusBadge = "Verified Merchant (₹145)",
                            timeLabel = "2 days ago",
                            payeeOrSender = "nandini.dairy@sbi",
                            category = "MERCHANT"
                        )
                    )
                    dao.insertAll(initialChecks)
                }
            }
        }
    }
}
