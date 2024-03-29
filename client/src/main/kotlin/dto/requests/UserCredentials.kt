package dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(var login: String, var password: String)
