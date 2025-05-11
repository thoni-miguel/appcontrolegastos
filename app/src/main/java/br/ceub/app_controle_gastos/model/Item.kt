package br.ceub.app_controle_gastos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["id"],
            childColumns = ["shopping_list_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    @ColumnInfo(name = "category_id")
    val categoryId: Int? = null, // ID da categoria associada, pode ser null (usamos ID 1 como padrão)
    @ColumnInfo(name = "shopping_list_id")
    val shoppingListId: Int, // ID da lista à qual este item pertence
    val description: String? = null // a descrição é opcional
)
