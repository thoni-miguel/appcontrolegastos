package br.ceub.app_controle_gastos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ceub.app_controle_gastos.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun listAll(): Flow<List<Category>>
}