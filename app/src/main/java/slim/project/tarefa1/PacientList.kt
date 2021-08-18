package slim.project.tarefa1

import com.beust.klaxon.Json
import java.util.ArrayList


data class Pacient (
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String)

data class Data (val value: Int)

data class UserResponse (
    var data: ArrayList<Data>?,
    @Json(name = "patient")
    val user:Pacient?)