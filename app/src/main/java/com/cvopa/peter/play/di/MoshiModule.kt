package com.cvopa.peter.play.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class MoshiModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}