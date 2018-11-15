package jp.ginyolith.questionnaire

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import jp.ginyolith.questionnaire.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var viewModel : MainViewModel
    lateinit var binding : ActivityMainBinding

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = MainViewModel(FirebaseFirestore.getInstance())

        binding.viewModel = viewModel
    }

}

enum class Vote {
    RED,
    GREEN,
    BLUE,
    YELLOW;

    companion object {
        fun of(name : String) : Vote?
            = Vote.values().first { it.name == name }
    }
}
