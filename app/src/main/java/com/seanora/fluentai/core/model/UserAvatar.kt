package com.seanora.fluentai.core.model

import androidx.annotation.DrawableRes
import com.seanora.fluentai.R

enum class UserAvatar(
    val key: String,
    @DrawableRes val drawableRes: Int,
    val displayName: String
) {
    AVATAR_1("avatar_1", R.drawable.avatar_1, "Avatar 1"),
    AVATAR_2("avatar_2", R.drawable.avatar_2, "Avatar 2"),
    AVATAR_3("avatar_3", R.drawable.avatar_3, "Avatar 3");

    companion object {
        val ALL: List<UserAvatar> = entries.toList()

        fun fromKey(key: String?): UserAvatar? = entries.find { it.key == key }

        fun getDrawableRes(key: String?): Int? = fromKey(key)?.drawableRes
    }
}
