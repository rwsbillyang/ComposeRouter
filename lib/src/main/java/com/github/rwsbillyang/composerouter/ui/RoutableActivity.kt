package com.github.rwsbillyang.composerouter.ui



import android.view.KeyEvent
import androidx.activity.ComponentActivity
import com.github.rwsbillyang.composerouter.Router

 open class RoutableActivity : ComponentActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
        return if(Router.onKeyDown(keyCode, event)) true else super.onKeyDown(keyCode, event)
    }
}