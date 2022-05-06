package com.example.rxkotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.rxkotlin.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    @SuppressLint("CheckResult")
    private fun initViews() {
        val searchTextObservable = createButtonObservable()

//        searchTextObservable.subscribe{
//            binding.textView.text = it
//            Log.d(TAG, "initViews: $it")
//        }

        searchTextObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .map {
//                if(it.length < 5){
//                    it.plus(it)
//                }
//            }
    /*
            .debounce(
                3L, TimeUnit.SECONDS
            )
            .subscribe{
                    Log.d(TAG, "initViews : $it")
            }
    */
            .filter {
                it.length > 3
            }
            .subscribe{
                binding.textView.text = it
            }

    }

    private fun createButtonObservable() : Observable<String>{
        return Observable.create{   emitter->

            binding.editText.addTextChangedListener {
                emitter.onNext(it.toString())
            }

            binding.button.setOnClickListener {
                emitter.onNext(binding.editText.text.toString() + "button pressed")
            }

            emitter.setCancellable(null)
        }
    }
}