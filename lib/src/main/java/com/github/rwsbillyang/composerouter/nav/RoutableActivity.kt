package com.github.rwsbillyang.composerouter.nav




import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity

import com.github.rwsbillyang.composerouter.LocalRouter
import com.github.rwsbillyang.composerouter.Route

import com.github.rwsbillyang.composerouter.Router




open class RoutableActivity : ComponentActivity(){
    private var cleanRoutesIfExit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = getRoutes()
        if(!list.isNullOrEmpty()){
            cleanRoutesIfExit = true //if add routes in Application, should not override addRoutes
            Router.addRoutes(list)
        }
    }

    open fun getRoutes(): List<Route>?{
       return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
        return if(Router.onKeyDown(keyCode)) true else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(cleanRoutesIfExit) Router.release()//if add routes in Application, not call release
    }
}

/**
 * routes is added in A
 * */
abstract class LocalRoutableActivity: ComponentActivity(){
    val  localRouter = LocalRouter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localRouter.addRoutes(getRoutes())
    }

    abstract fun getRoutes(): List<Route>

    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
        return if(localRouter.onKeyDown(keyCode)) true else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        localRouter.release()
    }
}