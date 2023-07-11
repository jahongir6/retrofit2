package com.example.retrofit2.retrofit

import com.example.retrofit2.models.AddTodoResponse
import com.example.retrofit2.models.TodoGEtRespose
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("plan")
    fun getAllTodo(): Call<List<TodoGEtRespose>>

    @DELETE("plan/{id}/")
    fun deleteTodo(@Path("id") id: Int): Call<Int>

    @POST("plan/")
    fun createUser(@Body addTodoResponse: AddTodoResponse): Call<ResponseBody>

    @PATCH("plan/{id}/")
    fun updateUser(@Path("id") id: Int, @Body todo: TodoGEtRespose): Call<TodoGEtRespose>
}

