package com.fendustries.employees.listall.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fendustries.employees.listall.network.FetchEmployeesAPIService
import com.fendustries.employees.listall.repository.local.EmployeeDao
import com.fendustries.employees.listall.repository.local.EmployessDatabase
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

object ListAllDIModule {
    @Module
    @InstallIn(SingletonComponent::class)
    class Providers {
        @Provides
        @Singleton
        fun providesRetrofit(): Retrofit {
            val kotlinxJson = Json { ignoreUnknownKeys = true }

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit
                .Builder()
                .baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
                .client(client)
                .addConverterFactory(kotlinxJson.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        @Provides
        @Singleton
        fun provideFetch(retrofit: Retrofit): FetchEmployeesAPIService {
            return retrofit.create(FetchEmployeesAPIService::class.java)
        }

        @Provides
        @Singleton
        fun providesEmployeesDao(db: EmployessDatabase): EmployeeDao {
            return db.employeeDao()
        }

        @Provides
        @Singleton
        fun providesDB(@ApplicationContext context: Context): EmployessDatabase {
            return Room.databaseBuilder(context, EmployessDatabase::class.java, "employees")
                .fallbackToDestructiveMigration().build()
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface Bindings {
        @Binds
        @Singleton
        fun bindsFetchEmployeesRemoteRepository(fetchEmployeesRemoteRepository: FetchEmployeesRemoteRepositoryImpl): FetchEmployeesRemoteRepository
    }
}