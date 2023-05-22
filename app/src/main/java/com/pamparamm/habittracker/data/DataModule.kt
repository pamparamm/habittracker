package com.pamparamm.habittracker.data

import android.content.Context
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.data.repository.HabitsRepositoryImpl
import com.pamparamm.habittracker.data.store.local.RoomDataStore
import com.pamparamm.habittracker.data.store.local.HabitsDao
import com.pamparamm.habittracker.data.store.remote.RemoteDataApi
import com.pamparamm.habittracker.data.store.remote.RemoteDataStorage
import com.pamparamm.habittracker.data.store.remote.RemoteDataStorageImpl
import com.pamparamm.habittracker.domain.repository.HabitsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindRemoteDataStorage(
        remoteDataStorageImpl: RemoteDataStorageImpl
    ): RemoteDataStorage

    @Binds
    abstract fun bindHabitsRepository(habitsRepositoryImpl: HabitsRepositoryImpl): HabitsRepository

    companion object {
        @Provides
        fun provideHabitsDao(@ApplicationContext context: Context): HabitsDao =
            Room.databaseBuilder(context, RoomDataStore::class.java, "appdb").build().habitsDao()

        @Provides
        @Named("authToken")
        fun authToken(@ApplicationContext context: Context): String =
            context.getString(R.string.auth_token)

        @Provides
        @Named("authInterceptor")
        fun authInterceptor(@Named("authToken") authToken: String): Interceptor {
            return Interceptor {
                val newRequest: Request = it.request().newBuilder()
                    .addHeader("Authorization", authToken)
                    .build()
                it.proceed(newRequest)
            }
        }

        @Provides
        @Named("loggingInterceptor")
        fun loggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        fun okHttp(
            @Named("authInterceptor") authInterceptor: Interceptor,
            @Named("loggingInterceptor") loggingInterceptor: Interceptor
        ): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

        @Provides
        fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .build()

        @Provides
        fun remoteDataSource(retrofit: Retrofit): RemoteDataApi =
            retrofit.create(RemoteDataApi::class.java)
    }
}