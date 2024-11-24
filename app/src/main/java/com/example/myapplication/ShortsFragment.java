package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.squareup.picasso.Picasso;

public class ShortsFragment extends Fragment {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private List<News> filteredNewsList;
    private SearchView searchView;

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

        // Buscar la vista del SearchView
        searchView = view.findViewById(R.id.searchView);

        // Configurar RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsList = new ArrayList<>();
        filteredNewsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(filteredNewsList);
        newsRecyclerView.setAdapter(newsAdapter);

        // Cargar noticias desde la API
        loadNews();

        // Configuración del SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Este método no debería hacer nada si no es necesario
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar noticias mientras se escribe
                filterNews(newText);
                return true;
            }
        });
    }

    // Método para cargar noticias desde la API
    private void loadNews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService service = retrofit.create(NewsApiService.class);

        // Incluye el idioma 'es' para obtener noticias en español
        Call<NewsResponse> call = service.getTopHeadlines("us", "9f8e175fa9f8474dbeb798a31e2cad9e", "es");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsList.addAll(response.body().getArticles());
                    filteredNewsList.addAll(newsList); // Inicialmente todas las noticias
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Manejo de error
            }
        });
    }

    // Filtrar noticias según la búsqueda
    private void filterNews(String query) {
        if (query == null) {
            return;
        }

        filteredNewsList.clear();

        if (query.isEmpty()) {
            filteredNewsList.addAll(newsList);
        } else {
            for (News news : newsList) {
                if (news.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        news.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredNewsList.add(news);
                }
            }
        }

        newsAdapter.notifyDataSetChanged();
    }

    // Modelo de datos para las noticias
    static class News {
        private String title;
        private String description;
        private String urlToImage;

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getUrlToImage() { return urlToImage; }
    }

    // Respuesta de la API
    static class NewsResponse {
        private List<News> articles;

        public List<News> getArticles() { return articles; }
    }

    // Interfaz de Retrofit
    interface NewsApiService {
        @GET("top-headlines")
        Call<NewsResponse> getTopHeadlines(
                @Query("country") String country,
                @Query("apiKey") String apiKey,
                @Query("language") String language // Nuevo parámetro
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
            Picasso.get().load(news.getUrlToImage()).into(holder.newsImageView);
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

