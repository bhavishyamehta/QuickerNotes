package org.david.notes.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.Response
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.models.AuthRequest
import org.david.notes.models.AuthResponse
import org.david.notes.models.SyncRequest
import org.david.notes.models.SyncResponse

expect val BASE_URL_EMULATOR: String
const val BASE_URL = "http://localhost:8080"


class ApiService(val client: HttpClient, val dataStoreManager: DataStoreManager) {
    private val ACTIVE_URL = BASE_URL_EMULATOR
    private val LOGIN_ENDPOINT = "${ACTIVE_URL}/auth/login"
    private val SIGNUP_ENDPOINT = "${ACTIVE_URL}/auth/signup"
    private val SYNC_ENDPOINT = "${ACTIVE_URL}/sync"


    suspend fun login(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = client.post(urlString = LOGIN_ENDPOINT) {
                setBody(request)
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(exception = Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun signup(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = client.post(urlString = SIGNUP_ENDPOINT) {
                setBody(request)
            }
            if (response.status == HttpStatusCode.Created) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(exception = Exception("Signup failed"))
            }
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun sync(request: SyncRequest): Result<SyncResponse> {
        return try {
            val response = client.post(urlString = SYNC_ENDPOINT) {
                setBody(request)
                header(HttpHeaders.Authorization, "Bearer ${dataStoreManager.getToken() ?: ""}")
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body() as SyncResponse)
            } else {
                Result.failure(exception = Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }


}

