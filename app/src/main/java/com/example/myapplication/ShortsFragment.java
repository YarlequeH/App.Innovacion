package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ShortsFragment extends Fragment {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private List<News> filteredNewsList;

    public ShortsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shorts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsList = new ArrayList<>();
        filteredNewsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(filteredNewsList);
        newsRecyclerView.setAdapter(newsAdapter);

        // Cargar noticias filtradas (por denuncias)
        loadNews("denuncias");
    }

    // Método para cargar noticias filtradas desde la API
    private void loadNews(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsdata.io/api/1/") // Base URL de NewsData.io
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService service = retrofit.create(NewsApiService.class);
        Call<NewsResponse> call = service.getNews(
                "pub_6025051fe96a69353dab94a816074a4524a65", // Clave de API
                query // Término de búsqueda fijo: "denuncias"
        );

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    List<News> articles = response.body().getResults();
                    filteredNewsList.clear(); // Limpiar la lista de resultados anteriores
                    if (!articles.isEmpty()) {
                        filteredNewsList.addAll(articles);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        showNoNewsMessage("No se encontraron noticias relacionadas con denuncias.");
                    }
                } else {
                    showNoNewsMessage("No se pudo cargar las noticias. Intenta más tarde.");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                showNoNewsMessage("Error al conectarse al servidor. Verifica tu conexión a Internet.");
            }
        });
    }

    // Método para mostrar un mensaje si no hay noticias o ocurre un error
    private void showNoNewsMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Modelo de datos para las noticias
    static class News {
        private String title;
        private String description;
        private String image_url;

        public News(String title, String description, String image_url) {
            this.title = title;
            this.description = description;
            this.image_url = image_url;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getImageUrl() { return image_url; }
    }

    // Respuesta de la API
    static class NewsResponse {
        private List<News> results;

        public List<News> getResults() { return results; }
    }

    // Interfaz de Retrofit
    interface NewsApiService {
        @GET("news")
        Call<NewsResponse> getNews(
                @Query("apikey") String apiKey,
                @Query("q") String query
        );
    }

    // Adaptador del RecyclerView
    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        private List<News> newsList;

        public NewsAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            News news = newsList.get(position);
            holder.titleTextView.setText(news.getTitle());
            holder.descriptionTextView.setText(news.getDescription());

            // Cargar la imagen usando Picasso
            Picasso.get().load(news.getImageUrl()).into(holder.newsImageView);
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        // ViewHolder para el adaptador
        class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView, descriptionTextView;
            ImageView newsImageView;

            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                newsImageView = itemView.findViewById(R.id.newsImageView);
            }
        }
    }
}

