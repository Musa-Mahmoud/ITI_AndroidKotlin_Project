package com.iti.forecozy.model.local.weather

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.forecozy.model.dto.weather.FavoriteLocation

@Dao
interface IFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteLocation)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteLocation)

    @Query("SELECT * FROM favorite_locations ORDER BY timestamp DESC")
    suspend fun getAllFavorites(): List<FavoriteLocation>

    @Query("SELECT * FROM favorite_locations WHERE locationKey = :locationKey LIMIT 1")
    suspend fun getFavoriteByLocation(locationKey: String): FavoriteLocation?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_locations WHERE locationKey = :locationKey)")
    suspend fun isFavorite(locationKey: String): Boolean
}