package edu.tyut.datastorelearn.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class PersonSerializer() : Serializer<Person> {

    override val defaultValue: Person = Person.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Person {
        try {
            return Person.parseFrom(input)
        }catch (exception: Exception){
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: Person,
        output: OutputStream
    ) {
        t.writeTo(output)
    }

}