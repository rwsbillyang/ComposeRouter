# ComposeRouter
Android Jetpack Compose React-router-style Router inspired by react dom router

## How to
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


Step 2. Add the dependency
```
	dependencies {
	        implementation 'com.github.rwsbillyang:ComposeRouter:1.0.1'
	}
```

## Usage
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRouterTheme {
                Router.withRoutes(
                        route("Home", "/", R.string.home){
                            Home() //any screen
                        },
                        route("MyScreen1", "/screen1", R.string.screen1){
                            Screen1(it) //any screen need props pass
                        },
                        route("MyScreen2", "/screen2", R.string.screen2){
                            Screen2()//any screen
                        },
                        route("MyScreen3", "/screen3", R.string.screen2){
                            Screen3() //any screen
                        },
                        route("Settings", "/settings", R.string.settings){
                            Settings() //any screen
                        },
                    ).Screen()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
       return if(Router.onKeyDown(keyCode, event)) true else super.onKeyDown(keyCode, event)
    }
}


@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }

        //pass props to screen
        Button(onClick = { Router.navByPath("/screen1", "some props") }) {
            Text(text = "screen1")
        }
        Button(onClick = { Router.navByName("MyScreen2") }) {
            Text(text = "screen2")
        }
        Button(onClick = { Router.navByName("MyScreen3") }) {
            Text(text = "screen3")
        }
    }
}
@Composable
fun Settings() {
    Text(text = "Settings Screen")
}

//need receive props
@Composable
fun Screen1(call: ScreenCall) {
    Text(text = "Hello, ${call.props}")
}

@Composable
fun Screen2() {
    Text(text = "Screen2")
}
@Composable
fun Screen3() {
    Text(text = "Screen3")
}
```

## TODO
1. PathMather to support path and query parameters
2. permission check
3. multi-activity support
