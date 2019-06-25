package com.mishenka.cookingmvvm.util

object Utils {
    // Request codes
    const val RC_SIGN_IN = 1

    // Threading
    const val NETWORK_THREAD_COUNT = 3

    // Firebase database
    const val CHILD_RECIPE = "recipes"
    const val CHILD_RECIPE_AUTHOR = "author"
    const val CHILD_RECIPE_AUTHOR_UID = "authorUID"
    const val CHILD_RECIPE_DESCRIPTION = "description"
    const val CHILD_RECIPE_KEY = "key"
    const val CHILD_RECIPE_MAIN_PIC_URL = "mainPicUrl"
    const val CHILD_RECIPE_NAME = "name"
    const val CHILD_RECIPE_READ_COUNT = "readCount"
    const val CHILD_RECIPE_STAR_COUNT = "starCount"

    const val CHILD_WHOLE_RECIPE = "whole_recipes"
    const val CHILD_WHOLE_RECIPE_INGREDIENTS = "ingredientsList"
    const val CHILD_WHOLE_RECIPE_INGREDIENT_TEXT = "text"
    const val CHILD_WHOLE_RECIPE_STEPS = "stepsList"
    const val CHILD_WHOLE_RECIPE_STEP_DESCRIPTION = "stepDescription"
    const val CHILD_WHOLE_RECIPE_STEP_PICS = "picUrls"

    const val CHILD_USER = "users"
    const val CHILD_USER_CREATED_POSTS = "createdPosts"
    const val CHILD_USER_TOTAL_POSTS_COUNT = "totalPostsCount"

    // Firebase storage
    const val CHILD_COOKING_PICTURES = "cooking_pictures"

    // Local database
    const val DB_LOCAL_NAME = "cooking_database"
    const val DB_BOOKMARK_TABLE = "bookmark_table"
    const val DB_BOOKMARK_COLUMN_ID = "bookmark_id"
    const val DB_BOOKMARK_COLUMN_DATA = "bookmark_data"

    // Other
    const val IMAGE_CONTENT_TYPE = "image/jpg"
    const val MAIN_GALLERY = 1
    const val STEP_GALLERY = 2

    // Intent keys
    const val INTENT_RECIPE_KEY_KEY = "recipe_key"
}