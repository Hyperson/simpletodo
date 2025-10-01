package com.wasaap.androidstarterkit.core.network.retrofit

import com.wasaap.androidstarterkit.core.network.BuildConfig
import com.wasaap.androidstarterkit.core.network.TodoNetworkDataSource
import com.wasaap.androidstarterkit.core.network.model.NetworkTodo
import com.wasaap.androidstarterkit.core.network.model.NetworkTodoWrite
import dagger.Lazy
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for Todo Network API
 */
private interface RetrofitTodoNetworkApi {
    @GET("todos")
    suspend fun getTodos(): List<NetworkTodo>

    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: String): NetworkTodo

    @POST("todos")
    suspend fun addTodo(@Body todo: NetworkTodoWrite): NetworkTodo

    // CrudCrud returns 200/204 with empty body on PUT
    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Body todo: NetworkTodoWrite
    )

    // CrudCrud returns 200/204 with empty body on DELETE
    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: String)
}

private const val TODO_BASE_URL = "${BuildConfig.BACKEND_URL}${BuildConfig.BACKEND_API}/"

@Singleton
internal class RetrofitTodoNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Lazy<Call.Factory>,
) : TodoNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(TODO_BASE_URL)
        // We use callFactory lambda here with dagger.Lazy<Call.Factory>
        // to prevent initializing OkHttp on the main thread.
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitTodoNetworkApi::class.java)

    override suspend fun getTodos(): List<NetworkTodo> = networkApi.getTodos()

    override suspend fun getTodoById(id: String): NetworkTodo = networkApi.getTodoById(id)

    override suspend fun addTodo(todo: NetworkTodoWrite): NetworkTodo = networkApi.addTodo(todo)

    override suspend fun updateTodo(
        id: String,
        todo: NetworkTodoWrite
    ) = networkApi.updateTodo(id, todo)

    override suspend fun deleteTodo(id: String) = networkApi.deleteTodo(id)
}