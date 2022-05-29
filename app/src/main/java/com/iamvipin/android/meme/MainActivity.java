package com.iamvipin.android.meme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SyncRequest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ImageView memeImage;
    ProgressBar progressBar;
    String currImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeImage = findViewById(R.id.memeImageView);
        progressBar = findViewById(R.id.progress_id);
        loadMeme();
    }

   private void loadMeme(){
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://meme-api.herokuapp.com/gimme";

       // Request a jsonObject response from the provided URL.
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           currImageUrl = response.getString("url");
                           Glide.with(MainActivity.this).load(currImageUrl)
                                   .listener(new RequestListener<Drawable>() {
                                       @Override
                                       public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                           progressBar.setVisibility(View.GONE);
                                           return false;
                                       }

                                       @Override
                                       public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                           progressBar.setVisibility(View.GONE);
                                           return false;
                                       }
                                   })
                                   .into(memeImage);
                           } catch (JSONException e) {
                            e.printStackTrace();
                           }

                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               //Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
           }
       });

       // Add the request to the RequestQueue.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest  );
   }


    public void shareMeme(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setType("text/plain");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this cool meme from my app" + currImageUrl);

        Intent chooser = Intent.createChooser(sendIntent, "Share this meme using...");
        startActivity(chooser);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}