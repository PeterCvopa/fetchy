package com.cvopa.peter.play.di

import com.cvopa.peter.fetchy.BuildConfig
import com.cvopa.peter.play.api.LoginService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Reusable
    @Provides
    fun provideMoshiConverter(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }


    @Reusable
    @Provides
    fun provideMInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
}
