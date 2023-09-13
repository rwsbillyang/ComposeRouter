package com.github.rwsbillyang.composerouter

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf



object Router{
    var TAG = "Router"
    private val AllRoutesMap = mutableMapOf<String, Route>()

    private val routeHistory = mutableListOf<Route>()
    private var pathMather: PathMather = EqualPathMather()

    private var _currentRoute = mutableStateOf<Route?>(null)
    val currentRoute: Route?
        get() = _currentRoute.value
    var _currentProps: Any? = null

    fun withPathMather(mather: PathMather) {pathMather = mather}

    fun addRoutes(vararg routes: Route){
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

        Log.d(TAG, "${routes.size} routes added in addRoutes")
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
            Log.w(TAG, "Route not exist?")
            return
        }
        //TODO: check route permission

        val r = route.beforeEnter?.let { it(_currentRoute.value, route) }?: route

        routeHistory.remove(r)
        if(!r.skipHistoryStack){
            routeHistory.add(r)
        }

        _currentProps = props
        _currentRoute.value = r

        Log.d(TAG, "nav to route=${r.name}")
    }

    fun removeRoute(name: String){
        routeHistory.forEach{
            if(it.name == name){
                routeHistory.remove(it)
            }
        }
    }

    fun isLast() = routeHistory.size <= 1

    fun back(): Boolean{
        return if(routeHistory.size <= 1){//exit
            //release all routes and  memory
            AllRoutesMap.clear()
            routeHistory.clear()
            _currentRoute.value = null

            Log.d(TAG, "Router: exit, clear and release all done")
            false
        }else{
            Log.d(TAG, "Router: back one")
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
        Log.d(TAG, "ShowScreen: Router stack=${routeHistory.joinToString(" -> ") { it.name }}, currentRoute=${_currentRoute.value?.name}")

        //TODO 从上面的navByPath中解析出parameters和query参数，构建ScreenCall
        if(_currentRoute.value != null){
            val call = ScreenCall(currentRoute!!, _currentProps?:currentRoute?.props, paddingValues)
            _currentProps = null
            currentRoute!!.component.content(call)
        }else{
            Log.d(TAG, "No current route, do nothing")
        }

    }
}