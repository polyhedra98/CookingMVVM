package com.mishenka.cookingmvvm.util

import android.content.Context
import com.mishenka.cookingmvvm.data.source.RecipeRepository
import com.mishenka.cookingmvvm.data.source.remote.RecipeRemoteDataSource

object Injection {
    /*
    fun provideTasksRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        return TasksRepository.getInstance(FakeTasksRemoteDataSource,
                TasksLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }

     */
    fun provideRecipeRepository(context: Context) : RecipeRepository {
        return RecipeRepository.getInstance(RecipeRemoteDataSource.getInstance(AppExecutors()))
    }
}