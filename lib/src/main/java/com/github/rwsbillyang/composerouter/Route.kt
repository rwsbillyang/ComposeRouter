package com.github.rwsbillyang.composerouter


import androidx.compose.runtime.Composable

/**
 * @param name route name, should be unique
 * @param path default is "/${name}"
 * @param titleId nav bar title string resource id
 * @param isDefault
 * @param skipHistoryStack not add route into history stack if true, especially first welcome page
 * @param useNavScaffold only valid when use NavScaffold2/NavScaffold3 in MainActivity: if false not use Scaffold, only use provided screen
 * @param permission permission needed when enter route
 * @param beforeEnter check before when enter destination, eg: login. if success enter destination, or else enter error or login screen
 * //@param beforeLeave
 * @param props properties/values passe to destination when navigation
 * @param ctxMap context map
 * @param screen component screen for rendering
 * */
class Route(
    val name: String,
    val path: String,
    val titleId: Int? = null, //nav bar title
    val isDefault: Boolean = false,
    val skipHistoryStack: Boolean = false,
    val useNavScaffold: Boolean = true,
    val permission: List<String>? = null,
    val beforeEnter: ((from: Route?, to: Route) -> Route)? = null,
    val props: Any? = null,
    val ctxMap: MutableMap<String, Any> = mutableMapOf(),
    val screen: Screen
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
        result = 31 * result + screen.hashCode()
        result = 31 * result + (titleId?.hashCode() ?: 0)
        result = 31 * result + isDefault.hashCode()
        result = 31 * result + (props?.hashCode() ?: 0)
        result = 31 * result + (permission?.hashCode() ?: 0)
        return result
    }
}


/**
 * @param name route name, if no titleId or no title when call Router.nav, it will be nav bar title
 * @param titleId nav bar title res id
 * @param path route path, default is "/$name"
 * @param isDefault  default page(first page) if true
 * @param skipHistoryStack  not add route into history stack if true, especially first welcome page
 * @param screen the page, also call screen
 * */
fun route(
    name: String,
    titleId: Int? = null,
    path: String? = null,
    isDefault: Boolean = false,
    skipHistoryStack: Boolean = false,
    useNavScaffold: Boolean = true,
    screen: @Composable (call: ScreenCall) -> Unit
) = Route(name, path?:"/$name", titleId, isDefault, skipHistoryStack, useNavScaffold,  screen =  Screen(screen))

fun route(
    name: String,
    titleId: Int? = null,
    path: String? = null,
    isDefault: Boolean = false,
    skipHistoryStack: Boolean = false,
    useNavScaffold: Boolean = true,
    screen: Screen
) = Route(name, path?:"/$name", titleId, isDefault,skipHistoryStack, useNavScaffold,  screen = screen)
