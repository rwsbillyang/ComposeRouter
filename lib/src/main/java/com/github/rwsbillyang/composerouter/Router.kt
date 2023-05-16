package com.github.rwsbillyang.composerouter

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf



object Router{
    private val AllRoutesMap = mutableMapOf<String, Route>()

    private val routeHistory = mutableListOf<Route>()
    private var pathMather: PathMather = EqualPathMather()

    private var _currentRoute = mutableStateOf(Route("Empty","/null", component = ScaffoldScreen(@Composable {})))
    val currentRoute: Route
        get() = _currentRoute.value

    fun withPathMather(mather: PathMather) {pathMather = mather}

    fun withRoutes(vararg routes: Route): Router{
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

        nav(r)

        return this
    }


    /**
     * navigate by name
     * @param name name of route
     * @param props any value/property pass to destination screen
     * */
    fun navByName(name: String, props: Any? = null){
        nav(AllRoutesMap[name], props)
    }
    fun navByPath(path: String, props: Any? = null){
        for(kv in AllRoutesMap){
           if(pathMather.isMatch(kv.value.path, path)){
               nav(AllRoutesMap[kv.key], props)
               break
           }
        }
    }

    fun nav(route: Route?, props: Any? = null){
        if(route == null){
            Log.w("Router", "Route not exist?")
            return
        }
        //TODO: check route permission

        val r = route.beforeEnter?.let { it(_currentRoute.value, route) }?: route

        r.props = props

        routeHistory.remove(r)
        routeHistory.add(r)

        _currentRoute.value = r
    }

    fun isLast() = routeHistory.size == 1

    fun back(): Boolean{
        return if(routeHistory.size <= 1){
            //exit
            Log.d("Router", "Router: exit...")
            false
        }else{
            Log.d("Router", "Router: back one")
            routeHistory.removeLast()
            _currentRoute.value = routeHistory.last()
            true
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return back()
        }
        return false
    }


    @Composable
    fun Screen(paddingValues: PaddingValues = PaddingValues()){
        Log.d("Router", "Router stack: ${routeHistory.joinToString(" -> ") { it.name }}")

        //TODO 从上面的navByPath中解析出parameters和query参数，构建ScreenCall
        val call = ScreenCall(_currentRoute.value, paddingValues)
        _currentRoute.value.component.content(call)
    }
}