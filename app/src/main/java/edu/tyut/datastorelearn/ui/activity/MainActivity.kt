package edu.tyut.datastorelearn.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.Preferences
import edu.tyut.datastorelearn.ui.theme.DataStoreLearnTheme
import edu.tyut.datastorelearn.utils.dataStore
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import edu.tyut.datastorelearn.datastore.Person
import edu.tyut.datastorelearn.datastore.person
import edu.tyut.datastorelearn.utils.userPreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG: String = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStoreLearnTheme {
                val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        snackBarHostState = snackBarHostState,
                    )
                }
            }
        }
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier, snackBarHostState: SnackbarHostState) {
    val context: Context = LocalContext.current
    var content: String by remember { mutableStateOf("") }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    var userName: String by remember { mutableStateOf("") }
    var age: String by remember { mutableStateOf("") }
    var isMan: Boolean by remember { mutableStateOf(true) }
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        TextField(
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Text(
            textAlign = TextAlign.Center,
            text = "保存", fontSize = 16.sp, color = Color.White, modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(
                    color = Color(
                        0xFF64DD17
                    ), shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    coroutineScope.launch {
                        if (content.isEmpty()) {
                            snackBarHostState.showSnackbar("输入内容不能为空!")
                            return@launch
                        }
                        context.dataStore.edit { it: MutablePreferences ->
                            val contentKey: Preferences.Key<String> =
                                stringPreferencesKey(name = "content")
                            it[contentKey] = content
                            keyboardController?.hide()
                            snackBarHostState.showSnackbar("保存成功!")
                            Log.i(
                                TAG,
                                "Greeting -> dataStore: ${context.dataStore.javaClass},  读取: ${it[contentKey]}"
                            )
                        }
                    }
                }
        )
        Text(
            textAlign = TextAlign.Center,
            text = "展示内容", fontSize = 16.sp, color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFFFAB00), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .clickable {
                    val flow: Flow<String> =
                        context.dataStore.data.map { it: Preferences -> // Preferences -> value
                            val contentKey: Preferences.Key<String> =
                                stringPreferencesKey(name = "content")
                            it[contentKey] ?: "default"
                        }
                    coroutineScope.launch {
                        flow.collectLatest { content: String ->
                            snackBarHostState.showSnackbar("content: $content")
                        }
                    }
                }
        )
        // === 存取对象 ===
        TextField(
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            placeholder = { Text(text = "请输入用户名") },
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        TextField(
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            placeholder = { Text(text = "请输入用户年龄") },
            value = age,
            onValueChange = { age = it },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Switch(checked = isMan, onCheckedChange = { isMan = it })
            Text(text = "性别")
        }
        Text(
            textAlign = TextAlign.Center,
            text = "保存Person", fontSize = 16.sp, color = Color.White, modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(
                    color = Color(
                        0xFF64DD17
                    ), shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    coroutineScope.launch {
                        if (userName.isEmpty() || !age.isDigitsOnly()) {
                            snackBarHostState.showSnackbar("输入内容不合法!")
                            return@launch
                        }
                        context.userPreferencesStore.updateData { person: Person ->
                            Log.i(TAG, "Greeting -> userName: $userName, man: $isMan, age: $age")
                            person {
                                username = userName
                                man = isMan
                                this.age = age.toIntOrNull() ?: 0
                            }.apply {
                                snackBarHostState.showSnackbar("保存成功Person: $this, name: ${this.username}")
                            }
                            person.toBuilder().setUsername(userName)
                                .setAge(age.toIntOrNull() ?: 0)
                                .setMan(isMan)
                                .build().apply {
                                    snackBarHostState.showSnackbar("保存成功Person: $this")
                                }
                        }
                    }
                }
        )
        Text(
            textAlign = TextAlign.Center,
            text = "展示Person", fontSize = 16.sp, color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFFFAB00), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .clickable {
                    val flow: Flow<Person> =
                        context.userPreferencesStore.data.map { it: Person -> // Preferences -> value
                            it
                        }
                    coroutineScope.launch {
                        flow.collectLatest { person: Person ->
                            // person.username 原理就是下面这一行
                            val username: String = String(bytes = person.username.toByteArray(charset = Charsets.UTF_8), charset = Charsets.UTF_8)
                            Log.i(TAG, "Greeting -> person: $person, username: $username, person.username: ${person.username}")
                            snackBarHostState.showSnackbar("读取成功person: $person")
                        }
                        // 阻塞时读取
                        val person: Person? = context.userPreferencesStore.data.firstOrNull()
                        Log.i(TAG, "Greeting -> person Local: ${person.toString()}, person.username: ${person?.username}")
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    DataStoreLearnTheme {
        Greeting(name = "Android", snackBarHostState = snackBarHostState)
    }
}