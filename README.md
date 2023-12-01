# ComposeRouter
Android Jetpack Compose React-router-style Router inspired by react dom router

Feature:
1. Web style and extremely lightweight
2. optional: support navbar using scaffold, and configurable
3. scaffold supports Material2 and Material3 theme 

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
	        implementation 'com.github.rwsbillyang:ComposeRouter:1.2.0'
	}
```

## Quickstart

A simple example:
```kotlin
class MainActivity : RoutableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Router.addRoutes(
            route("Home", "/", R.string.home){ Home() },
            route("MyScreen1", "/screen1", R.string.screen1){ Screen1(it) },
            route("MyScreen2", "/screen2", R.string.screen2){ Screen2() },
            route("MyScreen3", "/screen3", R.string.screen3){ Screen3() },
            route("Settings", "/settings", R.string.settings){ Settings() },
        )

        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //materia3 theme. if use materia2: here is NavScaffold2(R.string.app_name)
                //Router.Screen() //no navigation title bar
            }
        }
    }
// RoutableActivity already provided it
//    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
//       return if(Router.onKeyDown(keyCode, event)) true else super.onKeyDown(keyCode, event)
//    }
}

@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }

        //pass props to screen
        Button(onClick = { Router.navByPath("/screen1", "any props") }) {
            Text(text = "enter screen1")
        }

        Button(onClick = { Router.navByName("MyScreen2") }) {
            Text(text = "enter screen2")
        }

        Button(onClick = { Router.navByName("MyScreen3") }) {
            Text(text = "enter screen3")
        }
    }
}


//need receive props
@Composable
fun Screen1(call: ScreenCall) {  Text(text = "Hello, ${call.props}") }

@Composable
fun Screen2() { Text(text = "Screen2") }

@Composable
fun Screen3() {   Text(text = "Screen3") }

@Composable
fun Settings() { Text(text = "Settings Screen") }

```



Just a little complicated:
```kotlin

class MainActivity : RoutableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Router.addRoutes(
            route("Home", "/", R.string.home){ Home() },
            route("MyScreen1", "/screen1", R.string.screen1,screen = ScaffoldScreen(
                { Screen1(it) },
                {
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { Router.navByName("Settings") }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                }
            )),
            route("MyScreen2", "/screen2", R.string.screen2,
                screen = ScaffoldScreen(
                    content = { Screen2(it) },
                    drawerContent = {
                        Text("Drawer title", modifier = Modifier.padding(16.dp))
                        HorizontalDivider()
                        // Drawer items
                        Text("item1", modifier = Modifier.padding(14.dp))
                        Text("item2", modifier = Modifier.padding(14.dp))
                    }
                )),
            route("MyScreen3", "/screen3", R.string.screen3){ Screen3(it) },
            route("Settings", "/settings", R.string.settings){ Settings(it) },
        )

        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //materia3 theme. if use materia2: here is NavScaffold2(R.string.app_name)
                //Router.Screen() //no navigation title bar
            }
        }
    }
}

//left same as the above simple example
```

## TODO
1. PathMather to support path and query parameters
2. permission check
3. multi-activity support
