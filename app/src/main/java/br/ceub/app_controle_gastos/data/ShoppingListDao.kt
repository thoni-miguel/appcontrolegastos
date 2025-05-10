package br.ceub.app_controle_gastos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ceub.app_controle_gastos.model.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: ShoppingList)

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    fun listAll(): Flow<List<ShoppingList>>
}