package com.example.memeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

//  https://meme-api.com/gimme

class MainActivity : AppCompatActivity() {

    private var currentImageurl: String? = null
    private var a = 0


    private var list_of_meme = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getmemedata()

    }

    //    val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
    private fun getmemedata() {
//  API
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageurl = response.getString("url")
                val imageurl = response.getString("url")
                if (!list_of_meme.contains(currentImageurl)) {
                    list_of_meme.add(imageurl)


                val imageView = findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load(currentImageurl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false

                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)
            }
                else{
                    getmemedata()
                }

                a = list_of_meme.size - 1
            },

            {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_occur_error_localizedmessage),
                    Toast.LENGTH_SHORT
                ).show()
            })
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


    fun Share(view : View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Check this meme $currentImageurl")
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent, "Share this using...")
        startActivity(chooser)
    }

    fun Next(view: View) {
        if (list_of_meme.size == a+1){
            getmemedata()
        }
        else{
            a++ // Decrement to move to the previous meme
            val imageView = findViewById<ImageView>(R.id.imageView)
            Glide.with(this).load(list_of_meme[a]).into(imageView)
        }

    }

    @SuppressLint("ResourceType")
    fun Previous(view: View) {
        if (a <= 0) {
            Toast.makeText(this@MainActivity, "You haven't watched any previous memes", Toast.LENGTH_SHORT).show()
            return
        }
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        a-- // Decrement to move to the previous meme
        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(list_of_meme[a]).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false

            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }
        }).into(imageView)


    }

}


