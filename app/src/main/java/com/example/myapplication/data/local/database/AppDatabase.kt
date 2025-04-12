package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [User::class, GPU::class, CartItem::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gpuDao(): GPUDao
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao

    companion object {
        const val DATABASE_NAME = "gpu_shop_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback(context))
                    .addMigrations(AppMigrations.MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)


            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `users` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `username` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `password` TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.execSQL(
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

            db.execSQL(
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


            db.execSQL("INSERT INTO users (username, email, password) VALUES ('admin', 'admin@example.com', 'admin123')")
            db.execSQL("INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1')")
            db.execSQL("INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2')")


            val gpus = listOf(
                GPU(
                    name = "Palit GeForce RTX 4080 SUPER GamingPro",
                    manufacturer = "NVIDIA",
                    price = 134999.00,
                    imageUrl = "palit_geforce_rtx_4080_super_gamingpro",
                    memorySize = 16,
                    memoryType = "GDDR6X",
                    coreClock = 2295,
                    boostClock = 2610,
                    cudaCores = 10240,
                    description = "Видеокарта Palit GeForce RTX 4080 SUPER GamingPro OC – оснащение для высокопроизводительных игровых компьютеров. Быстродействие модели достаточно для использования почти любых видеоигр на максимальных настройках графики. Видеоадаптер может быть бесшумным: при минимальной нагрузке вентиляторы снижают скорость вращения до нуля. Металлический бекплейт защищает печатную плату от деформации."
                ),
                GPU(
                    name = "MSI GeForce RTX 4080 SUPER EXPERT",
                    manufacturer = "NVIDIA",
                    price = 141999.00,
                    imageUrl = "msi_geforce_rtx_4080_super_expert",
                    memorySize = 16,
                    memoryType = "GDDR6X",
                    coreClock = 2295,
                    boostClock = 2550,
                    cudaCores =  10240,
                    description = "Видеокарта Palit GeForce RTX 4080 SUPER GamingPro – оснащение для мощных игровых ПК. Устройство гарантирует возможность использования почти любых игр на максимальных настройках графики. Главный конструктивный элемент видеоадаптера – графический процессор GeForce RTX 4080 SUPER"
                ),
                GPU(
                    name = "GIGABYTE GeForce RTX 4070 GAMING OC 12G",
                    manufacturer = "NVIDIA",
                    price = 74999.00,
                    imageUrl = "gigabyte_geforce_rtx_4070_gaming_oc_12g",
                    memorySize = 12,
                    memoryType = "GDDR6X",
                    coreClock = 1920,
                    boostClock = 2565,
                    cudaCores = 5888,
                    description = "Видеокарта GIGABYTE GeForce RTX 4070 GAMING OC 12G с архитектурой Ada Lovelace, технологией DLSS 3 и полноценной реализацией трассировки лучей гарантирует плавность изображения и реализм графики в компьютерных играх. Модель оснащена процессором с возможностью разгона частоты до 2565 МГц и 12 ГБ видеопамяти стандарта GDDR6X, которые обеспечивают высокую вычислительную мощность."
                ),
                GPU(
                    name = "ASRock AMD Radeon RX 6600 Challenger White",
                    manufacturer = "AMD",
                    price = 24999.00,
                    imageUrl = "asrock_amd_radeon_rx_6600_challenger_white",
                    memorySize = 8,
                    memoryType = "GDDR6",
                    coreClock = 1626,
                    boostClock = 2491,
                    cudaCores = 1792,
                    description = "Видеокарта ASRock AMD Radeon RX 6600 Challenger White – оснащение для игровых системных блоков. Видеоадаптер отличается нетипичным для компьютерных компонентов дизайном: он выполнен в белом цвете. Модель оснащена графическим процессором Radeon RX 6600"
                ),
                GPU(
                    name = "GIGABYTE AMD Radeon RX 6900 XT AORUS XTREME WATERFORCE WB",
                    manufacturer = "AMD",
                    price = 59999.00,
                    imageUrl = "gigabyte_amd_radeon_rx_6900_xt_aorus_xtreme_waterforce_wb",
                    memorySize = 16,
                    memoryType = "GDDR6",
                    coreClock = 2200,
                    boostClock = 2525,
                    cudaCores = 5120,
                    description = "Видеокарта GIGABYTE Radeon RX 6900 XT AORUS XTREME WATERFORCE WB совершенна с любой точки зрения, от дизайна до «начинки». К ее преимуществам относятся интегрированная видеопамять на 16 ГБ и способность обеспечивать разрешение вплоть до Ultra HD 8K"
                ),
                GPU(
                    name = "ZOTAC GeForce RTX 4060 Ti Twin Edge OC White Edition",
                    manufacturer = "NVIDIA",
                    price = 50299.00,
                    imageUrl = "zotac_geforce_rtx_4060_ti_twin_edge_oc_white_edition",
                    memorySize = 8,
                    memoryType = "GDDR6",
                    coreClock = 2310,
                    boostClock = 2565,
                    cudaCores = 4352,
                    description = "Zotac GeForce RTX 4060 Ti Twin Edge OC White Edition обеспечивают оптимальную производительность в топовых играх благодаря возможностям Ada Lovelace — архитектуры NVIDIA RTX 3-го поколения. Оптимальная производительность и качество графики благодаря улучшенным ядрам RT и тензорным ядрам, потоковым мультипроцессорам и высокоскоростной памяти G6."
                ),
                GPU(
                    name = "ASRock AMD Radeon RX 7600 XT Challenger OC",
                    manufacturer = "AMD",
                    price = 39799.00,
                    imageUrl = "asrock_amd_radeon_rx_7600_xt_challenger_oc",
                    memorySize = 16,
                    memoryType = "GDDR6",
                    coreClock = 1720,
                    boostClock = 2799,
                    cudaCores = 2048,
                    description = "Видеокарта ASRock AMD Radeon RX 7600 XT Challenger OC – оснащение для игровых системных блоков. Модель поддерживает технологию 0dB Silent Cooling: при минимальной нагрузке оба вентилятора останавливаются. Металлическая задняя панель предотвращает деформацию устройства. В сочетании с термопрокладками, тепловыми трубками и массивным радиатором она обеспечивает требуемую эффективность охлаждения в любых режимах."
                ),
                GPU(
                    name = "ASRock Intel Arc A770 Phantom Gaming D OC",
                    manufacturer = "INTEL",
                    price = 37999.00,
                    imageUrl = "asrock_intel_arc_a770_phantom_gaming_d_oc",
                    memorySize = 16,
                    memoryType = "GDDR6",
                    coreClock = 2100,
                    boostClock = 2200,
                    cudaCores = 4096,
                    description = "Видеокарта ASRock Intel Arc A770 Phantom Gaming D OC обеспечивает высокие показатели производительности, скорости и плавности графики при запуске игр и ресурсоемких программ. В ее основе содержится архитектура Intel Xe-HPG. Процессор функционирует на частоте 2100 МГц, которая способна увеличиваться до 2200 МГц при разгоне."
                )
            )

            val gpuStmt = db.compileStatement(
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

            val cartStmt = db.compileStatement(
                "INSERT INTO cart_items (userId, gpuId, quantity) VALUES (?, ?, ?)"
            )

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

            cartStmt.bindLong(1, 2)
            cartStmt.bindLong(2, 3)
            cartStmt.bindLong(3, 1)
            cartStmt.executeInsert()
            cartStmt.clearBindings()
        }
    }

}