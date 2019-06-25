package com.mishenka.cookingmvvm.data.source.remote

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.mishenka.cookingmvvm.data.*
import com.mishenka.cookingmvvm.data.source.RecipeDataSource
import com.mishenka.cookingmvvm.util.AppExecutors
import com.mishenka.cookingmvvm.util.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeRemoteDataSource private constructor(
        private val appExecutors: AppExecutors
) : RecipeDataSource {

    override fun loadRecipes(callback: RecipeDataSource.LoadRecipesCallback) {
        Log.i("NYA", "From recipe remote data source. About to load recipes.")

        val dbRef = FirebaseDatabase.getInstance().reference
        val recipesRef = dbRef.child(Utils.CHILD_RECIPE)
        recipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("NYA", "Recipes loading failed")
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val recipes = p0.children
                val recipesToCallback = ArrayList<Recipe>(recipes.count())
                p0.children.forEach { recipeChild -> recipesToCallback.add(Recipe(
                        key = recipeChild.child(Utils.CHILD_RECIPE_KEY).value as String?,
                        author = recipeChild.child(Utils.CHILD_RECIPE_AUTHOR).value as String?,
                        authorUID = recipeChild.child(Utils.CHILD_RECIPE_AUTHOR_UID).value as String?,
                        name = recipeChild.child(Utils.CHILD_RECIPE_NAME).value as String?,
                        description = recipeChild.child(Utils.CHILD_RECIPE_DESCRIPTION).value as String?,
                        mainPicUrl = recipeChild.child(Utils.CHILD_RECIPE_MAIN_PIC_URL).value as String?,
                        readCount = recipeChild.child(Utils.CHILD_RECIPE_READ_COUNT).value as Long?,
                        starCount = recipeChild.child(Utils.CHILD_RECIPE_STAR_COUNT).value as Long?))
                }
                callback.onRecipesLoaded(recipesToCallback.reversed())
            }
        })
    }

    override fun getCombinedRecipe(recipeId: String, callback: RecipeDataSource.GetCombinedRecipeCallback) {
        Log.i("NYA", "From recipe recipe remote data source. About to get recipe.")

        val dbRef = FirebaseDatabase.getInstance().reference
        val recipesRef = dbRef.child(Utils.CHILD_RECIPE)
        val wholeRecipesRef = dbRef.child(Utils.CHILD_WHOLE_RECIPE)
        val combinator = Combinator(callback)

        recipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val recipeChild = p0.child(recipeId)
                val recipeToCombine = Recipe(
                        key = recipeChild.child(Utils.CHILD_RECIPE_KEY).value as String?,
                        author = recipeChild.child(Utils.CHILD_RECIPE_AUTHOR).value as String?,
                        authorUID = recipeChild.child(Utils.CHILD_RECIPE_AUTHOR_UID).value as String?,
                        name = recipeChild.child(Utils.CHILD_RECIPE_NAME).value as String?,
                        description = recipeChild.child(Utils.CHILD_RECIPE_DESCRIPTION).value as String?,
                        mainPicUrl = recipeChild.child(Utils.CHILD_RECIPE_MAIN_PIC_URL).value as String?,
                        readCount = recipeChild.child(Utils.CHILD_RECIPE_READ_COUNT).value as Long?,
                        starCount = recipeChild.child(Utils.CHILD_RECIPE_STAR_COUNT).value as Long?
                )
                combinator.addRecipe(recipeToCombine)
            }
        })

        wholeRecipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val wholeRecipeChild = p0.child(recipeId)
                val wholeRecipeToCombine = WholeRecipe()
                if (wholeRecipeChild.hasChild(Utils.CHILD_WHOLE_RECIPE_INGREDIENTS)) {
                    val ingredientsToCombine = ArrayList<Ingredient>(wholeRecipeChild.child(Utils.CHILD_WHOLE_RECIPE_INGREDIENTS).children.count())
                    wholeRecipeChild.child(Utils.CHILD_WHOLE_RECIPE_INGREDIENTS).children.forEach { singleChild ->
                        if (singleChild.hasChild(Utils.CHILD_WHOLE_RECIPE_INGREDIENT_TEXT)) {
                            ingredientsToCombine.add(Ingredient(singleChild.child(Utils.CHILD_WHOLE_RECIPE_INGREDIENT_TEXT).value as String?))
                        }
                    }
                    wholeRecipeToCombine.ingredientsList = ingredientsToCombine
                }
                if (wholeRecipeChild.hasChild(Utils.CHILD_WHOLE_RECIPE_STEPS)) {
                    val stepsToCombine = ArrayList<FirebaseStep>(wholeRecipeChild.child(Utils.CHILD_WHOLE_RECIPE_STEPS).children.count())
                    wholeRecipeChild.child(Utils.CHILD_WHOLE_RECIPE_STEPS).children.forEach { singleChild ->
                        val stepToCombine = FirebaseStep()
                        if (singleChild.hasChild(Utils.CHILD_WHOLE_RECIPE_STEP_DESCRIPTION)) {
                            stepToCombine.stepDescription = singleChild.child(Utils.CHILD_WHOLE_RECIPE_STEP_DESCRIPTION).value as String?
                        }
                        if (singleChild.hasChild(Utils.CHILD_WHOLE_RECIPE_STEP_PICS)) {
                            val picsToCombine = ArrayList<String?>(singleChild.child(Utils.CHILD_WHOLE_RECIPE_STEP_PICS).children.count())
                            singleChild.child(Utils.CHILD_WHOLE_RECIPE_STEP_PICS).children.forEach { singleStepPic ->
                                picsToCombine.add(singleStepPic.value as String?)
                            }
                            stepToCombine.picUrls = ArrayList(picsToCombine.filterNotNull())
                        }
                        stepsToCombine.add(stepToCombine)
                    }
                    wholeRecipeToCombine.stepsList = stepsToCombine
                }
                combinator.addWholeRecipe(wholeRecipeToCombine)
            }
        })
    }

    override fun getWholeRecipe(recipeId: String, callback: RecipeDataSource.GetWholeRecipeCallback) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTotalPostsNumber(userUID: String, callback: RecipeDataSource.GetTotalPostsNumberCallback) {
        val dbRef = FirebaseDatabase.getInstance().reference
        val userTotalPostsRef = dbRef.child(Utils.CHILD_USER).child(userUID).child(Utils.CHILD_USER_TOTAL_POSTS_COUNT)
        userTotalPostsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    callback.onTotalPostsNumberGot(p0.value as Long)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        })
    }

    override fun getTotalViewsCount(userUID: String, callback: RecipeDataSource.GetTotalViewsCountCallback) {
        val dbRef = FirebaseDatabase.getInstance().reference
        val userCreatedPostsRef = dbRef.child(Utils.CHILD_USER).child(userUID).child(Utils.CHILD_USER_CREATED_POSTS)
        userCreatedPostsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val userCreatedPosts = ArrayList<String>(p0.children.count())
                p0.children.forEach { singleCreatedPost ->
                    singleCreatedPost.key?.let { safeKey ->
                        userCreatedPosts.add(safeKey)
                    }
                }
                Log.i("NYA", "From remote data source. createdPostsKeys: $userCreatedPosts")
                if (userCreatedPosts.count() != 0) {
                    getUserCreatedPostsCount(userCreatedPosts, callback)
                } else {
                    Log.i("NYA", "User created posts count is 0")
                    callback.onDataNotAvailable()
                }
            }
        })
    }

    private fun getUserCreatedPostsCount(userCreatedPosts: List<String>, callback: RecipeDataSource.GetTotalViewsCountCallback) {
        val dbRef = FirebaseDatabase.getInstance().reference
        val recipesRef = dbRef.child(Utils.CHILD_RECIPE)
        recipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onDataNotAvailable()
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val createdChildren = ArrayList<DataSnapshot>(p0.children.count())
                p0.children.forEach { singleChild ->
                    if (userCreatedPosts.contains(singleChild.key)) {
                        createdChildren.add(singleChild)
                    }
                }

                Log.i("NYA", "Created children: $createdChildren")
                var totalViews = 0.toLong()
                for (createdChild in createdChildren) {
                    createdChild.child(Utils.CHILD_RECIPE_READ_COUNT).value?.let { safeCount ->
                        totalViews += safeCount as Long
                    }
                }
                callback.onTotalViewCountGot(totalViews)
            }
        })
    }

    override fun incrementReadCount(recipeId: String) {
        val dbRef = FirebaseDatabase.getInstance().reference
        val recipeRef = dbRef.child(Utils.CHILD_RECIPE).child(recipeId)
        recipeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException() as Throwable
            }

            override fun onDataChange(p0: DataSnapshot) {
                val viewCount = p0.child(Utils.CHILD_RECIPE_READ_COUNT).value as Long?
                if (viewCount == null) {
                    recipeRef.child(Utils.CHILD_RECIPE_READ_COUNT).setValue(0.toLong())
                } else {
                    recipeRef.child(Utils.CHILD_RECIPE_READ_COUNT).setValue(viewCount + 1.toLong())
                }
            }
        })
    }

    override fun saveCombinedRecipe(combinedRecipe: CombinedRecipe, callback: RecipeDataSource.SaveRecipeCallback): String? {
        Log.i("NYA", "From recipe remote data source. About to save recipe. Key: ${combinedRecipe.recipe.key} -- Name: ${combinedRecipe.recipe.name}"
                + " -- Author: ${combinedRecipe.recipe.author} -- AuthorUID: ${combinedRecipe.recipe.authorUID}"
                + " -- Description: ${combinedRecipe.recipe.description} -- MainPicUrl: ${combinedRecipe.recipe.mainPicUrl}"
                + " -- StarCount: ${combinedRecipe.recipe.starCount} -- WatchCount: ${combinedRecipe.recipe.readCount}"
                + " -- Steps: ${combinedRecipe.wholeRecipe.stepsList} -- Ingredients: ${combinedRecipe.wholeRecipe.ingredientsList}")

        val saveJob = GlobalScope.launch {
            var mainPicDownloadUrl: String? = null
            val mainPicJob = GlobalScope.launch {
                combinedRecipe.recipe.mainPicUrl?.let { safeMainPicString ->
                    val mainPicUri = Uri.parse(safeMainPicString)
                    val cookingSRef = FirebaseStorage.getInstance().reference.child(Utils.CHILD_COOKING_PICTURES)
                    val metadata = StorageMetadata.Builder().setContentType(Utils.IMAGE_CONTENT_TYPE).build()
                    val photoRef = cookingSRef.child("${combinedRecipe.recipe.authorUID}/${mainPicUri.lastPathSegment!!}")
                    val uploadTask = photoRef.putFile(mainPicUri, metadata)
                    var potentialDownloadTask: Task<Uri>? = null
                    uploadTask.addOnSuccessListener {
                        potentialDownloadTask = photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            mainPicDownloadUrl = downloadUrl.toString()
                        }
                    }
                    Tasks.await(uploadTask)
                    delay(50)
                    if (potentialDownloadTask != null) {
                        Tasks.await(potentialDownloadTask!!)
                    }
                }
            }

            var stepPicsDownloadUrls: List<String?>? = null
            val stepPicsJob = GlobalScope.launch {
                combinedRecipe.wholeRecipe.stepsList?.let { safeStepsList ->
                    val stepPicUrisList = ArrayList<String>()
                    for (safeStep in safeStepsList) {
                        safeStep.picUrls?.let { safePickUrls ->
                            stepPicUrisList.addAll(safePickUrls.filter { !it.isEmpty() })
                        }
                    }
                    val potentialDownloadUrls = arrayOfNulls<String?>(stepPicUrisList.size)
                    val cookingSRef = FirebaseStorage.getInstance().reference.child(Utils.CHILD_COOKING_PICTURES)
                    val metadata = StorageMetadata.Builder().setContentType(Utils.IMAGE_CONTENT_TYPE).build()
                    val uploadTasks = ArrayList<UploadTask>(stepPicUrisList.size)
                    val potentialTasks =  ArrayList<Task<Uri>?>()

                    for ((counter, localStepPicUri) in stepPicUrisList.withIndex()) {
                        val uriRepresentation = Uri.parse(localStepPicUri)
                        val photoRef = cookingSRef.child("${combinedRecipe.recipe.authorUID}/${uriRepresentation.lastPathSegment}")
                        val uploadTask = photoRef.putFile(uriRepresentation, metadata)
                        uploadTasks.add(uploadTask)
                        var potentialTask: Task<Uri>? = null
                        uploadTask.addOnSuccessListener {
                            potentialTask = photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                potentialDownloadUrls[counter] = downloadUrl.toString()
                            }
                            potentialTasks.add(potentialTask)
                        }
                    }
                    Tasks.await(Tasks.whenAll(uploadTasks))
                    if (potentialTasks.size == stepPicUrisList.size) {
                        Tasks.await(Tasks.whenAll(potentialTasks))
                        stepPicsDownloadUrls = potentialDownloadUrls.asList()
                    }
                }
            }

            mainPicJob.join()
            stepPicsJob.join()
            val dbRef = FirebaseDatabase.getInstance().reference
            val key = dbRef.child(Utils.CHILD_RECIPE).push().key!!

            var stepsListToSave: ArrayList<FirebaseStep>? = null
            combinedRecipe.wholeRecipe.stepsList?.let { safeStepsList ->
                stepsListToSave = ArrayList(safeStepsList.size)
                var urlsCounter = 0
                for (safeStep in safeStepsList) {
                    val stepToSave = FirebaseStep(stepDescription = safeStep.stepDescription)
                    safeStep.picUrls?.let { safePicUrls ->
                        stepPicsDownloadUrls?.let { safePicDownloadUrls ->
                            val arrayToAssign = arrayOfNulls<String?>(safePicUrls.size)
                            for ((counter, safePicUrl) in safePicUrls.withIndex()) {
                                arrayToAssign[counter] = safePicDownloadUrls[urlsCounter++]
                            }
                            stepToSave.picUrls = ArrayList(arrayToAssign.filterNotNull())
                        }
                    }
                    if (!stepToSave.stepDescription.isNullOrBlank() || !stepToSave.picUrls.isNullOrEmpty()) {
                        stepsListToSave?.add(stepToSave)
                    }
                }
            }

            var ingredientsListToSave: List<Ingredient>? = null
            combinedRecipe.wholeRecipe.ingredientsList?.let { safeIngredientsList ->
                ingredientsListToSave = safeIngredientsList.filter { ingredient -> !ingredient.text.isNullOrBlank() }
            }

            if (stepsListToSave.isNullOrEmpty()) {
                stepsListToSave = null
            }
            var nameToSave: String? = null
            if (!combinedRecipe.recipe.name.isNullOrBlank()) {
                nameToSave = combinedRecipe.recipe.name
            }
            var descriptionToSave: String? = null
            if (!combinedRecipe.recipe.description.isNullOrBlank()) {
                descriptionToSave = combinedRecipe.recipe.description
            }
            dbRef.child(Utils.CHILD_RECIPE).child(key).setValue(
                    Recipe(
                            key = key,
                            name = nameToSave,
                            description = descriptionToSave,
                            author = combinedRecipe.recipe.author,
                            authorUID = combinedRecipe.recipe.authorUID,
                            mainPicUrl = mainPicDownloadUrl
                    )
            )
            dbRef.child(Utils.CHILD_WHOLE_RECIPE).child(key).setValue(
                    WholeRecipe(
                            key = key,
                            ingredientsList = ingredientsListToSave,
                            stepsList = stepsListToSave
                    )
            )

            val authorRef = dbRef.child(Utils.CHILD_USER).child(combinedRecipe.recipe.authorUID!!)
            authorRef.child(Utils.CHILD_USER_CREATED_POSTS).child(key).setValue(true)
            authorRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException() as Throwable
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val value = p0.child(Utils.CHILD_USER_TOTAL_POSTS_COUNT).value
                    if (value == null) {
                        authorRef.child(Utils.CHILD_USER_TOTAL_POSTS_COUNT).setValue(1)
                    } else {
                        authorRef.child(Utils.CHILD_USER_TOTAL_POSTS_COUNT).setValue(value as Long + 1)
                    }
                }
            })
        }

        // TODO("Implement proper key return")
        return "nya"
    }

    override fun refreshRecipes() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class Combinator(
            private val callback: RecipeDataSource.GetCombinedRecipeCallback
    ) {
        var recipe: Recipe? = null
        var wholeRecipe: WholeRecipe? = null
        var elementCounter = 0

        fun addRecipe(recipe: Recipe) {
            this.recipe = recipe
            elementCounter++
            checkForCompletion()
        }

        fun addWholeRecipe(wholeRecipe: WholeRecipe) {
            this.wholeRecipe = wholeRecipe
            elementCounter++
            checkForCompletion()
        }

        private fun checkForCompletion() {
            if (elementCounter == 2) {
                callback.onRecipeLoaded(CombinedRecipe(recipe!!, wholeRecipe!!))
            }
        }
    }

    companion object {
        private var INSTANCE: RecipeRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors) =
                INSTANCE ?: synchronized(RecipeRemoteDataSource::class.java) {
                    INSTANCE ?: RecipeRemoteDataSource(appExecutors).also {INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}