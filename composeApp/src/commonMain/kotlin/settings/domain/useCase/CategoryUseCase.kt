package settings.domain.useCase

import settings.domain.useCase.categoryUseCase.ValidateCategoryForm

data class CategoryUseCase(
    val validate:ValidateCategoryForm = ValidateCategoryForm()
)
