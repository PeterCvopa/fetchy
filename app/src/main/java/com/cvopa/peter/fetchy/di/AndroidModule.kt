package com.cvopa.peter.fetchy.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AndroidModule {

    @Provides
    fun provideApplicationContext(app: Application): Context = app
}
