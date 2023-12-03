package com.github.rwsbillyang.composerouter

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.github.rwsbillyang.composerouter.nav.LocalRoutableActivity


@Composable
fun useRouter(): LocalRouter {
    val ctx = LocalContext.current
    val router = remember {
         if(ctx is LocalRoutableActivity){
            ctx.localRouter
        } else Router
    }
    return router
}

/**
 * Global Router
 * */
object Router: LocalRouter(){
    val TAG = "Router"
}


/**
 * only valid in a scope such as Activity
 * eg. In multi-activity app, every Activity has its Router
 * */
open class LocalRouter{
    private val AllRoutesMap = mutableMapOf<String, Route>()

    private val routeHistory = mutableListOf<Route>()

    private var pathMather: PathMather = EqualPathMather()

    private var _currentRoute = mutableStateOf<Route?>(null)
    val currentRoute: Route?
        get() = _currentRoute.value


    fun withPathMather(mather: PathMather) {pathMather = mather}

    fun addRoutes(vararg routes: Route) = addRoutes(routes.toList())
    fun addRoutes(routes: List<Route>){
        var firstDefault: Route? = null
        var secondDefault: Route? = null
        routes.forEach {
            AllRoutesMap[it.name] = it

            if(it.isDefault && firstDefault == null){
                firstDefault = it
            }
            if(it.path == "/" && secondDefault == null){
                secondDefault = it
            }
        }

        //优先使用isDefault=true, 其次 path="/"，否则使用第一个作为default
        val r = if(firstDefault != null){
            firstDefault!!
        }else if(secondDefault != null){
            secondDefault!!
        }else{
            routes[0]
        }

        if(currentRoute == null) nav(r)

        Log.d(Router.TAG, "${routes.size} routes added in addRoutes")
    }



    /**
     * navigate by name
     * @param name name of route
     * @param props any value/property pass to destination screen
     * @param title dynamic nav bar title, highest priority
     * */
    fun navByName(name: String, props: Any? = null, title: String? = null){
        nav(AllRoutesMap[name], props, title)
    }

    /**
     * navigate by path
     * @param path path of route
     * @param props any value/property pass to destination screen
     * @param title dynamic nav bar title, highest priority
     * */
    fun navByPath(path: String, props: Any? = null, title: String? = null){
        for(kv in AllRoutesMap){
            if(pathMather.isMatch(kv.value.path, path)){
                nav(AllRoutesMap[kv.key], props, title)
                break
            }
        }
    }

    /**
     * navigate by route
     * @param route route
     * @param props any value/property pass to destination screen
     * @param title dynamic nav bar title, highest priority
     * */
    fun nav(route: Route?, props: Any? = null, title: String? = null){
        if(route == null){
            Log.w(Router.TAG, "Route not exist?")
            return
        }
        //TODO: check route permission


        val r = if(route.beforeEnter == null) route else route.beforeEnter.invoke(_currentRoute.value, route)

        routeHistory.remove(r)
        if(!r.skipHistoryStack){
            routeHistory.add(r)
        }

        _currentRoute.value = r

        if(props != null) r.ctxMap["props"] = props else r.ctxMap.remove("props")
        if(title != null) r.ctxMap["title"] = title else r.ctxMap.remove("title")

        Log.d(Router.TAG, "nav to route=${r.name}")
    }

    fun removeRoute(name: String){
        routeHistory.forEach{
            if(it.name == name){
                routeHistory.remove(it)
            }
        }
    }

    fun isLast() = routeHistory.size <= 1

    /**
     * if add routes in Application.onCreate, set clearRouteWhenExit = false
     * if add routes in Activity.onCreate, set clearRouteWhenExit = true
     * */
    fun back(): Boolean{
        return if(routeHistory.size <= 1){//exit
            false
        }else{
            Log.d(Router.TAG, "Router: back one")
            routeHistory.removeLast()
            _currentRoute.value = routeHistory.last()
            true
        }
    }

    fun release(){
        if(routeHistory.size <= 1){//exit
            //release all routes and  memory
            AllRoutesMap.clear()
            routeHistory.clear()
            _currentRoute.value = null

            Log.d(Router.TAG, "exit, clear and release all routes done")
            false
        }
    }

    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return back()
        }
        return false
    }


    @Composable
    fun Screen(paddingValues: PaddingValues = PaddingValues()){
        Log.d(Router.TAG, "ShowScreen: Router stack=${routeHistory.joinToString(" -> ") { it.name }}, currentRoute=${_currentRoute.value?.name}")

        val route = _currentRoute.value
        //TODO 从上面的navByPath中解析出parameters和query参数，构建ScreenCall
        if(route != null){
            val call = ScreenCall(route, route.ctxMap["props"] ?:(route.props), paddingValues)

            route.screen.content(call)
        }else{
            Log.d(Router.TAG, "No current route, do nothing")
        }

    }
}
