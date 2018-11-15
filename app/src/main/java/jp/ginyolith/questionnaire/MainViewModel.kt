package jp.ginyolith.questionnaire

import android.databinding.ObservableField
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(db : FirebaseFirestore) {
    val collection = db.collection("questionnaire_count")

    val countRed = ObservableField<String>()
    val countBlue = ObservableField<String>()
    val countYellow = ObservableField<String>()
    val countGreen = ObservableField<String>()

    init {
        Vote.values().forEach {
            collection.document(it.name).addSnapshotListener {querySnapShot, exception ->
                requireNotNull(querySnapShot)
                refreshData(querySnapShot, it)
            }
        }

        collection.get().addOnCompleteListener {
            requireNotNull(it)
            refreshData(it.result)
        }
    }

    private fun refreshData(querySnapShot : QuerySnapshot)
        = querySnapShot.documents.forEach {
            refreshData(it, Vote.of(it.id))
        }

    private fun refreshData(it : DocumentSnapshot, vote : Vote?){
        val count = it.data?.get("count") ?: "0"

        when (vote) {
            Vote.RED -> countRed.set(count.toString())
            Vote.GREEN -> countGreen.set(count.toString())
            Vote.BLUE -> countBlue.set(count.toString())
            Vote.YELLOW -> countYellow.set(count.toString())
        }
    }

    fun voteRed() = vote(Vote.RED)
    fun voteGreen() = vote(Vote.GREEN)
    fun voteBlue() = vote(Vote.BLUE)
    fun voteYellow() = vote(Vote.YELLOW)

    private fun vote(vote : Vote) {
        val count = when(vote) {
            Vote.RED -> countRed.get()
            Vote.GREEN -> countGreen.get()
            Vote.BLUE -> countBlue.get()
            Vote.YELLOW -> countYellow.get()
        } ?: "0"

        val doc = collection.document(vote.name)
        val entity = HashMap<String, Any>().also {
            it["count"] = count.toInt() + 1
            it["time"] = Date()
        }

        doc.update(entity)
    }



}