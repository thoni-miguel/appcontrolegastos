package br.ceub.app_controle_gastos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ceub.app_controle_gastos.model.Item
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun listAll(): Flow<List<Item>>
}
