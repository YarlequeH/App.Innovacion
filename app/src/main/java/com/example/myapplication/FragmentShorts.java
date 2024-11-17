package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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

public class FragmentShorts extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private static final String TAG = "FragmentShorts";
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shorts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración del RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialización de la lista y el adaptador
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        // Inicializar SearchView
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Llamada a la API para realizar la búsqueda
                loadNews(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Opcional: Podrías realizar la búsqueda a medida que el texto cambia.
                return false;
            }
        });

        // Llamada inicial para cargar las noticias
        loadNews(null);
    }

    private void loadNews(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService service = retrofit.create(NewsApiService.class);
        Call<NewsResponse> call;

        // Si hay un término de búsqueda, hacemos la búsqueda con ese término
        if (query != null && !query.isEmpty()) {
            call = service.searchNews(query, "9f8e175fa9f8474dbeb798a31e2cad9e");
        } else {
            // Si no hay término de búsqueda, se muestran las noticias principales
            call = service.getTopHeadlines("us", "9f8e175fa9f8474dbeb798a31e2cad9e");
        }

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> articles = response.body().getArticles();
                    if (articles != null) {
                        newsList.clear();  // Limpiar la lista antes de agregar los nuevos artículos
                        newsList.addAll(articles);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "La lista de artículos es nula");
                    }
                } else {
                    Log.e(TAG, "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, "Error al cargar noticias", t);
            }
        });
    }

    public static class News {
        private String title;
        private String description;
        private String urlToImage;

        public News(String title, String description, String urlToImage) {
            this.title = title;
            this.description = description;
            this.urlToImage = urlToImage;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrlToImage() {
            return urlToImage;
        }
    }

    public static class NewsResponse {
        private List<News> articles;

        public List<News> getArticles() {
            return articles;
        }
    }

    public interface NewsApiService {
        @GET("top-headlines")
        Call<NewsResponse> getTopHeadlines(
                @Query("country") String country,
                @Query("apiKey") String apiKey
        );

        @GET("everything")
        Call<NewsResponse> searchNews(
                @Query("q") String query,
                @Query("apiKey") String apiKey
        );
    }

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

            if (news.getUrlToImage() != null && !news.getUrlToImage().isEmpty()) {
                Picasso.get().load(news.getUrlToImage()).into(holder.newsImageView);
            } else {
                holder.newsImageView.setImageResource(R.drawable.placeholder); // Imagen predeterminada
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        public class NewsViewHolder extends RecyclerView.ViewHolder {
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
