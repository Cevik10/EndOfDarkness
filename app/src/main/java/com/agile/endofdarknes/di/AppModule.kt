package com.agile.endofdarknes.di

import android.content.Context
import com.agile.endofdarknes.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideStoryRepository(@ApplicationContext context: Context): StoryRepository {
        return StoryRepository(context)
    }
}