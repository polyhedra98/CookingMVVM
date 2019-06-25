package com.mishenka.cookingmvvm.addrecipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mishenka.cookingmvvm.addrecipe.interfaces.StepClickListener
import com.mishenka.cookingmvvm.addrecipe.interfaces.StepDescriptionListener
import com.mishenka.cookingmvvm.addrecipe.interfaces.StepPicUrls
import com.mishenka.cookingmvvm.data.*
import com.mishenka.cookingmvvm.data.source.RecipeDataSource
import com.mishenka.cookingmvvm.data.source.RecipeRepository
import com.mishenka.cookingmvvm.util.Event

class AddRecipeViewModel(
        private val recipeRepository: RecipeRepository
) : ViewModel() {
    val mainPictureButtonEvent: LiveData<Event<Unit>>
        get() = _mainPictureButtonEvent
    private val _mainPictureButtonEvent = MutableLiveData<Event<Unit>>()

    val submitButtonEvent: LiveData<Event<Unit>>
        get() = _submitButtonEvent
    private val _submitButtonEvent = MutableLiveData<Event<Unit>>()

    val recipeName: LiveData<String>
        get() = _recipeName
    private val _recipeName = MutableLiveData<String>()

    val recipeDescription: LiveData<String>
        get() = _recipeDescription
    private val _recipeDescription = MutableLiveData<String>()

    val mainPicUrl: LiveData<Uri>
        get() = _mainPicUrl
    private val _mainPicUrl = MutableLiveData<Uri>()

    val editableIngredientsList: LiveData<List<EditableIngredientWrapper>>
        get() = _editableIngredientsList
    private val _editableIngredientsList = MutableLiveData<List<EditableIngredientWrapper>>().apply {
        value = listOf(
                EditableIngredientWrapper(Ingredient(), getIngredientIndex()),
                EditableIngredientWrapper(Ingredient(), getIngredientIndex()),
                EditableIngredientWrapper(Ingredient(), getIngredientIndex()),
                EditableIngredientWrapper(Ingredient(), getIngredientIndex()),
                EditableIngredientWrapper(Ingredient(), getIngredientIndex()))
    }

    private var editableIngredientIndex = 0

    val firstStepDescription: LiveData<String>
        get() = _firstStepDescription
    private val _firstStepDescription = MutableLiveData<String>()

    val firstStepDescriptionListener = object : StepDescriptionListener {
        override fun onTextChanged(newText: CharSequence) {
            _firstStepDescription.value = newText.toString()
        }
    }

    val secondStepDescription: LiveData<String>
        get() = _secondStepDescription
    private val _secondStepDescription = MutableLiveData<String>()

    val secondStepDescriptionListener = object : StepDescriptionListener {
        override fun onTextChanged(newText: CharSequence) {
            _secondStepDescription.value = newText.toString()
        }
    }

    val thirdStepDescription: LiveData<String>
        get() = _thirdStepDescription
    private val _thirdStepDescription = MutableLiveData<String>()

    val thirdStepDescriptionListener = object : StepDescriptionListener {
        override fun onTextChanged(newText: CharSequence) {
            _thirdStepDescription.value = newText.toString()
        }
    }

    class AdvancedStepPicUrls : StepPicUrls {
        override val firstPicUrl: LiveData<String>
            get() = _firstPicUrl
        val _firstPicUrl = MutableLiveData<String>()

        override val secondPicUrl: LiveData<String>
            get() = _secondPicUrl
        val _secondPicUrl = MutableLiveData<String>()

        override val thirdPicUrl: LiveData<String>
            get() = _thirdPicUrl
        val _thirdPicUrl = MutableLiveData<String>()
    }

    val firstStepPicUrls = AdvancedStepPicUrls()
    val secondStepPicUrls = AdvancedStepPicUrls()
    val thirdStepPicUrls = AdvancedStepPicUrls()

    val stepPicEvent: LiveData<Event<Unit>>
        get() = _stepPicEvent
    private val _stepPicEvent = MutableLiveData<Event<Unit>>()

    var lastStepIndex: String? = null

    class AdvancedStepClickListener(
            private val firstString: String,
            private val secondString: String,
            private val thirdString: String,
            private val perform: (String) -> Unit
    ) : StepClickListener {
        override fun firstStepButtonClicked() {
            perform(firstString)
        }

        override fun secondStepButtonClicked() {
            perform(secondString)
        }

        override fun thirdStepButtonClicked() {
            perform(thirdString)
        }
    }

    val firstStepClickListener = AdvancedStepClickListener("1_1", "1_2", "1_3") { lastIndex ->
        run {
            lastStepIndex = lastIndex
            _stepPicEvent.value = Event(Unit)
        }
    }
    val secondStepClickListener = AdvancedStepClickListener("2_1", "2_2", "2_3") { lastIndex ->
        run {
            lastStepIndex = lastIndex
            _stepPicEvent.value = Event(Unit)
        }
    }
    val thirdStepClickListener = AdvancedStepClickListener("3_1", "3_2", "3_3") { lastIndex ->
        run {
            lastStepIndex = lastIndex
            _stepPicEvent.value = Event(Unit)
        }
    }

    fun onRecipeNameChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        _recipeName.value = text.toString()
    }

    fun onRecipeDescriptionChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        _recipeDescription.value = text.toString()
    }

    fun submit() {
        if (recipeName.value.isNullOrEmpty()) {
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        val ingredientsList = ArrayList<Ingredient>()
        editableIngredientsList.value?.forEach { editableIngredient ->
            ingredientsList.add(editableIngredient.ingredient)
        }

        val combinedRecipe = CombinedRecipe(
                Recipe(
                        name = recipeName.value,
                        author = user?.displayName,
                        authorUID = user?.uid,
                        description = recipeDescription.value,
                        mainPicUrl = mainPicUrl.value?.toString(),
                        readCount = 0,
                        starCount = 0
                ),
                WholeRecipe(
                        ingredientsList = ingredientsList,
                        stepsList = listOf(
                                FirebaseStep(
                                        firstStepDescription.value,
                                        ArrayList(listOfNotNull(
                                                firstStepPicUrls.firstPicUrl.value,
                                                firstStepPicUrls.secondPicUrl.value,
                                                firstStepPicUrls.thirdPicUrl.value))
                                ),
                                FirebaseStep(
                                        secondStepDescription.value,
                                        ArrayList(listOfNotNull(
                                                secondStepPicUrls.firstPicUrl.value,
                                                secondStepPicUrls.secondPicUrl.value,
                                                secondStepPicUrls.thirdPicUrl.value))
                                ),
                                FirebaseStep(
                                        thirdStepDescription.value,
                                        ArrayList(listOfNotNull(
                                                thirdStepPicUrls.firstPicUrl.value,
                                                thirdStepPicUrls.secondPicUrl.value,
                                                thirdStepPicUrls.thirdPicUrl.value))
                                )
                        )
                )
        )
        recipeRepository.saveCombinedRecipe(combinedRecipe, object : RecipeDataSource.SaveRecipeCallback {
            override fun onRecipeSaved(recipe: Recipe) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataNotAvailable() {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        _submitButtonEvent.value = Event(Unit)
    }

    fun start() {
        updateDataBindingObservables()
    }

    fun onMainPicButtonClicked() {
        _mainPictureButtonEvent.value = Event(Unit)
    }

    fun onAddIngredientClicked() {
        val ingredientsToReplaceWith = ArrayList<EditableIngredientWrapper>(editableIngredientsList.value!!)
        ingredientsToReplaceWith.add(EditableIngredientWrapper(Ingredient(), getIngredientIndex()))
        _editableIngredientsList.value = ingredientsToReplaceWith.toList()
    }

    fun updateStepPicture(uri: Uri) {
        when(lastStepIndex) {
            "1_1" -> firstStepPicUrls._firstPicUrl.value = uri.toString()
            "1_2" -> firstStepPicUrls._secondPicUrl.value = uri.toString()
            "1_3" -> firstStepPicUrls._thirdPicUrl.value = uri.toString()

            "2_1" -> secondStepPicUrls._firstPicUrl.value = uri.toString()
            "2_2" -> secondStepPicUrls._secondPicUrl.value = uri.toString()
            "2_3" -> secondStepPicUrls._thirdPicUrl.value = uri.toString()

            "3_1" -> thirdStepPicUrls._firstPicUrl.value = uri.toString()
            "3_2" -> thirdStepPicUrls._secondPicUrl.value = uri.toString()
            "3_3" -> thirdStepPicUrls._thirdPicUrl.value = uri.toString()
        }
    }

    fun updateMainPicture(uri: Uri) {
        _mainPicUrl.value = uri
    }

    private fun getIngredientIndex(): Int = editableIngredientIndex++

    private fun updateDataBindingObservables() {

    }

}