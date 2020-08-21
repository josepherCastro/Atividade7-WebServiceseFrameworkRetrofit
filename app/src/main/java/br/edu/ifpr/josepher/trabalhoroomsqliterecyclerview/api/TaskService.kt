package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.api

import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model.Task
import org.intellij.lang.annotations.JdkConstants
import retrofit2.Call
import retrofit2.http.*

interface TaskService {
    @GET("tasks")
    fun getAll(): Call<List<Task>>

    @GET("tasks/{id}")
    fun getOneTask(@Path("id") id: Long): Call<Task>

    @Headers("Content-Type: applition/json")
    @POST("tasks")
    fun insert(@Body task: Task): Call<Task>

    @Headers("Content-Type: applition/json")
    @PATCH("tasks/{id}")
    fun update(@Path("id") id: Long, @Body task: Task): Call<Task>

    @DELETE("tasks/{id}")
    fun delete(@Path("id") id: Long): Call<Void>
}