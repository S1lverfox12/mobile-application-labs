package com.example.myapplication
import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideGPUDao(database: AppDatabase): GPUDao {
        return database.gpuDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}