package com.bzh.dytt.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.bzh.dytt.vo.MovieDetail

@Dao
interface MovieDetailDAO {

    @Query("SELECT * FROM movie_detail WHERE isPrefect = :isPrefect")
    fun getMovieListByPrefect(isPrefect: Boolean): List<MovieDetail>

    @Query("SELECT * FROM movie_detail WHERE categoryId=:type  ORDER BY id DESC LIMIT :limit")
    fun movieList(type: Int?, limit: Int = 30): LiveData<List<MovieDetail>>

    @Query("SELECT * FROM movie_detail WHERE id in (:rowIds)")
    fun movieListByRowIds(rowIds: IntArray): LiveData<List<MovieDetail>>

    @Query("SELECT * FROM movie_detail WHERE categoryId=:categoryId AND id=:id")
    fun getMovieByCategoryIdAndId(categoryId: Int, id: Int): LiveData<MovieDetail>

    @Insert(onConflict = IGNORE)
    fun insertMovieList(movieList: List<MovieDetail>)

    @Update
    fun updateMovieList(movieList: List<MovieDetail>)

    @Update
    fun updateMovie(movie: MovieDetail)

}
