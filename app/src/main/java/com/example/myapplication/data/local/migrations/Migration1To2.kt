package com.example.myapplication
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.util.Log

object AppMigrations {
    val MIGRATION_1_2 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            Log.d("Migration", "Приложение запущено!")


            database.execSQL("DROP TABLE IF EXISTS `cart_items`")
            database.execSQL("DROP TABLE IF EXISTS `gpus`")
            database.execSQL("DROP TABLE IF EXISTS `users`")
            // Создаем таблицу users
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `users` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `username` TEXT NOT NULL,
                `email` TEXT NOT NULL,
                `password` TEXT NOT NULL
            )
        """.trimIndent()
            )

            // Создаем таблицу gpus
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `gpus` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `manufacturer` TEXT NOT NULL,
                `price` REAL NOT NULL,
                `imageUrl` TEXT NOT NULL,
                `memorySize` INTEGER NOT NULL,
                `memoryType` TEXT NOT NULL,
                `coreClock` INTEGER NOT NULL,
                `boostClock` INTEGER NOT NULL,
                `cudaCores` INTEGER NOT NULL,
                `description` TEXT NOT NULL
            )
        """.trimIndent()
            )

            // Создаем таблицу cart_items с внешними ключами
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `cart_items` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `userId` INTEGER NOT NULL,
                `gpuId` INTEGER NOT NULL,
                `quantity` INTEGER NOT NULL,
                FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`gpuId`) REFERENCES `gpus`(`id`) ON DELETE CASCADE
            )
        """.trimIndent()
            )

            // Добавляем тестовых пользователей
            database.execSQL("INSERT INTO users (username, email, password) VALUES ('admin', 'admin@example.com', 'admin123')")
            database.execSQL("INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1')")
            database.execSQL("INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2')")

            // Добавляем тестовые видеокарты
            val gpus = listOf(
                GPU(
                    name = "NVIDIA GeForce RTX 4090",
                    manufacturer = "NVIDIA",
                    price = 1599.99,
                    imageUrl = "",
                    memorySize = 24,
                    memoryType = "GDDR6X",
                    coreClock = 2235,
                    boostClock = 2520,
                    cudaCores = 16384,
                    description = "Flagship GPU from NVIDIA"
                ),
                GPU(
                    name = "AMD Radeon RX 7900 XTX",
                    manufacturer = "AMD",
                    price = 999.99,
                    imageUrl = "",
                    memorySize = 24,
                    memoryType = "GDDR6",
                    coreClock = 1900,
                    boostClock = 2500,
                    cudaCores = 6144,
                    description = "High-end GPU from AMD"
                ),
                GPU(
                    name = "NVIDIA GeForce RTX 3060",
                    manufacturer = "NVIDIA",
                    price = 329.99,
                    imageUrl = "",
                    memorySize = 12,
                    memoryType = "GDDR6",
                    coreClock = 1320,
                    boostClock = 1777,
                    cudaCores = 3584,
                    description = "Mid-range GPU from NVIDIA"
                )
            )

            // Вставляем видеокарты в базу данных
            val gpuStmt = database.compileStatement(
                "INSERT INTO gpus (name, manufacturer, price, imageUrl, memorySize, memoryType, coreClock, boostClock, cudaCores, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            )

            gpus.forEach { gpu ->
                gpuStmt.bindString(1, gpu.name)
                gpuStmt.bindString(2, gpu.manufacturer)
                gpuStmt.bindDouble(3, gpu.price)
                gpuStmt.bindString(4, gpu.imageUrl)
                gpuStmt.bindLong(5, gpu.memorySize.toLong())
                gpuStmt.bindString(6, gpu.memoryType)
                gpuStmt.bindLong(7, gpu.coreClock.toLong())
                gpuStmt.bindLong(8, gpu.boostClock.toLong())
                gpuStmt.bindLong(9, gpu.cudaCores.toLong())
                gpuStmt.bindString(10, gpu.description)
                gpuStmt.executeInsert()
                gpuStmt.clearBindings()
            }

            // Добавляем тестовые элементы корзины
            val cartStmt = database.compileStatement(
                "INSERT INTO cart_items (userId, gpuId, quantity) VALUES (?, ?, ?)"
            )

            // User 1 добавляет GPU 1 и 2 в корзину
            cartStmt.bindLong(1, 1)
            cartStmt.bindLong(2, 1)
            cartStmt.bindLong(3, 1)
            cartStmt.executeInsert()
            cartStmt.clearBindings()

            cartStmt.bindLong(1, 1)
            cartStmt.bindLong(2, 2)
            cartStmt.bindLong(3, 2)
            cartStmt.executeInsert()
            cartStmt.clearBindings()

            // User 2 добавляет GPU 3 в корзину
            cartStmt.bindLong(1, 2)
            cartStmt.bindLong(2, 3)
            cartStmt.bindLong(3, 1)
            cartStmt.executeInsert()
            cartStmt.clearBindings()
        }
    }
}