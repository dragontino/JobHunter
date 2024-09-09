package com.jobhunter.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.jobhunter.data.room.model.VacancyEntity
import kotlinx.coroutines.flow.Flow

@TypeConverters(ListConverter::class)
@Dao
interface LikedVacanciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacancy(vacancyEntity: VacancyEntity)

    @Query("SELECT COUNT(1) FROM Vacancy WHERE id = :id")
    suspend fun checkVacancyById(id: String): Int

    @Query("UPDATE Vacancy SET isFavorite = 1 WHERE id = :id")
    suspend fun likeVacancy(id: String): Int

    @Query("UPDATE Vacancy SET isFavorite = 0 WHERE id = :id")
    suspend fun dislikeVacancy(id: String): Int

    @Query("SELECT * FROM Vacancy WHERE isFavorite = 1")
    fun getLikedVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT * FROM Vacancy WHERE id = :id")
    fun getVacancyById(id: String): Flow<VacancyEntity?>
}