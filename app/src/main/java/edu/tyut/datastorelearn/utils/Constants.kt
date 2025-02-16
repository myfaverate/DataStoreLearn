package edu.tyut.datastorelearn.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import edu.tyut.datastorelearn.datastore.Person
import edu.tyut.datastorelearn.datastore.PersonSerializer
import java.io.File

// MultiProcessDataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.userPreferencesStore: DataStore<Person> by dataStore(
    fileName = "person_prefs.pb",
    serializer = PersonSerializer()
)
// TODO 使用 hilt 注入
// val dataStore: DataStore<Person> = MultiProcessDataStoreFactory.create(
//     serializer = PersonSerializer(),
//     produceFile = {
//         File("/myapp.preferences_pb")
//     }
// )