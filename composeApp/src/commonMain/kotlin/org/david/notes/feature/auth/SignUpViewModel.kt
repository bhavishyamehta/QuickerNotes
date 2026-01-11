package org.david.notes.feature.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.data.remote.ApiService
import org.david.notes.data.remote.HttpClientFactory
import org.david.notes.models.AuthRequest
import org.david.notes.models.AuthResponse

class SignUpViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Normal)
    val uiState = _uiState.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<AuthNavigation>()
    val navigationFlow = _navigationFlow.asSharedFlow()

    fun onErrorClick() {
        viewModelScope.launch {
            _uiState.value = AuthState.Normal
        }
    }

    fun onSuccessClick(email: String) {
        viewModelScope.launch {
            _navigationFlow.emit(AuthNavigation.NavigateToHome(email))
        }
    }


    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow<String>("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            val request = AuthRequest(email.value, password.value)
            _uiState.value = AuthState.Loading

            val result = apiService.signup(request)
            if (result.isSuccess) {
                _uiState.value = AuthState.Success(result.getOrNull()!!)

                result.getOrNull()?.let {
                    dataStoreManager.storeToken(it.accessToken)
                    dataStoreManager.storeRefreshToken(it.refreshToken)
                    dataStoreManager.storeUserId(it.userId)
                    dataStoreManager.storeEmail(it.email)
                }
            } else {
                _uiState.value =
                    AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthNavigation {
    class NavigateToHome(val email: String) : AuthNavigation()
    object NavigateToSignUp : AuthNavigation()
    object NavigateToLogin : AuthNavigation()
}

sealed class AuthState {
    object Normal : AuthState()
    object Loading : AuthState()
    class Success(val response: AuthResponse) : AuthState()
    class Error(val error: String) : AuthState()
}