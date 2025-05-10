package br.ceub.app_controle_gastos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String? = null,
    val color: Int? = null // Formato ARGB, mas pode ser nulo
)