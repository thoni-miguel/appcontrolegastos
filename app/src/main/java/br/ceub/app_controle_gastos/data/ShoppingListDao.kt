package br.ceub.app_controle_gastos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.ceub.app_controle_gastos.model.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: ShoppingList)

    @Update
    suspend fun update(list: ShoppingList)

    @Delete
    suspend fun delete(list: ShoppingList)

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    fun listAll(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE id = :listId LIMIT 1")
    fun getById(listId: Int): Flow<ShoppingList>
}