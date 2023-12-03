package api.dto.responces.user

import bl.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(var id: Int, var login: String, var isAdmin: Boolean) {
    constructor(user: User) : this(user.id, user.login, user.isAdmin)
}