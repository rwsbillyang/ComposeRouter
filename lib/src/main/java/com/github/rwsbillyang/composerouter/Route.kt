package com.github.rwsbillyang.composerouter

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

/**
 * @param name
 * @param path
 * @param titleId
 * @param isDefault
 * @param permission permission needed when enter route
 * @param beforeEnter check before when enter destination, eg: login. if success enter destination, or else enter error or login screen
 * //@param beforeLeave
 * @param props properties/values passe to destination when navigation
 * @param component component for rendering
 * */
class Route(
    val name: String,
    val path: String,
    val titleId: Int? = null, //scaffold context
    val isDefault: Boolean = false,
    val permission: List<String>? = null,
    val beforeEnter: ((from: Route, to: Route) -> Route)? = null,
    val props: Any? = null,
    val component: ScaffoldScreen
) {
    override fun equals(other: Any?): Boolean {
        return if (other == null) {
            false
        } else {
            if (other is Route) {
                other.name == name
            } else false
        }
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + component.hashCode()
        result = 31 * result + (titleId?.hashCode() ?: 0)
        result = 31 * result + isDefault.hashCode()
        result = 31 * result + (props?.hashCode() ?: 0)
        result = 31 * result + (permission?.hashCode() ?: 0)
        return result
    }
}

fun route(
    name: String,
    path: String,
    titleId: Int? = null,
    component: ScaffoldScreen,
) = Route(name, path, titleId, component = component)

fun route(
    name: String,
    path: String,
    titleId: Int? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (call: ScreenCall) -> Unit
) = Route(name, path, titleId, component =  ScaffoldScreen(content, actions))
