package settings.data.mapper

import database.entity.CategoryEntity
import settings.domain.model.category.Category
import settings.domain.model.category.CategoryWithColor
import settings.domain.model.product.ProductColor

fun Category.toCategoryEntity():CategoryEntity{
    return CategoryEntity(
        id = id,
        name = name,
        colorId = colorId
    )
}

fun  CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        colorId = colorId
    )
}

fun Category.toCategoryWithColor(colors:List<ProductColor>): CategoryWithColor {
    return CategoryWithColor(
        id = id,
        name = name,
        color = colors.find { it.id == colorId } ?: colors[0]
    )
}

fun  CategoryWithColor.toCategory(colors:List<ProductColor>): Category {
    return Category(
        id = id,
        name = name,
        colorId = color.id
    )
}