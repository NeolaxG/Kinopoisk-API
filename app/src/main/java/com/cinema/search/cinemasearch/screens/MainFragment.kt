package com.cinema.search.cinemasearch.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.search.cinemasearch.R
import com.cinema.search.cinemasearch.api.KinopoiskApiService
import com.cinema.search.cinemasearch.api.Movie
import com.cinema.search.cinemasearch.api.MovieAdapter
import com.cinema.search.cinemasearch.api.OnItemClickListener
import com.cinema.search.cinemasearch.api.Rating
import com.cinema.search.cinemasearch.api.RetrofitClient
import com.cinema.search.cinemasearch.databinding.FragmentMainBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class MainFragment : Fragment(), OnItemClickListener {
    lateinit var binding: FragmentMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MovieAdapter
    lateinit var originalMovies: List<Movie> // Сохранение исходного списка фильмов

    private val apiService: KinopoiskApiService by lazy {
        RetrofitClient.instance.create(KinopoiskApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterMovies(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        apiService.getTopMovies().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val moviesJsonObject = response.body()
                    if (moviesJsonObject != null) {
                        val moviesArray = moviesJsonObject.getAsJsonArray("docs")
                        originalMovies = parseMoviesFromJson(moviesArray) // Сохранение исходного списка
                        Log.d("MainFragment", "Количество фильмов: ${originalMovies.size}")
                        adapter = MovieAdapter(originalMovies, this@MainFragment)
                        recyclerView.adapter = adapter
                    } else {
                        Log.d("MainFragment", "Список фильмов пуст")
                    }
                } else {
                    Log.d("MainFragment", "Не удалось получить данные: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("MainFragment", "Ошибка при получении данных: ${t.message}", t)
            }
        })
    }

    fun parseMoviesFromJson(jsonArray: JsonArray?): List<Movie> {
        val movies = mutableListOf<Movie>()
        jsonArray?.forEach { jsonElement ->
            val jsonObject = jsonElement.asJsonObject
            val name = jsonObject.getAsJsonPrimitive("name").asString
            val description = jsonObject.getAsJsonPrimitive("description").asString
            val year = jsonObject.getAsJsonPrimitive("year").asInt
            val posterUrl = jsonObject.getAsJsonObject("poster").getAsJsonPrimitive("url").asString
            val ratingObject = jsonObject.getAsJsonObject("rating")
            val rating = Rating(
                kp = ratingObject.getAsJsonPrimitive("kp").asDouble,
                imdb = ratingObject.getAsJsonPrimitive("imdb").asDouble,
                filmCritics = ratingObject.getAsJsonPrimitive("filmCritics").asDouble,
                russianFilmCritics = ratingObject.getAsJsonPrimitive("russianFilmCritics").asDouble
            )
            val movie = Movie(name, description, year, posterUrl, rating)
            movies.add(movie)
        }
        return movies
    }

    private fun filterMovies(query: String) {
        if (query.isEmpty()) {
            adapter.submitList(originalMovies) // Используем сохраненный исходный список
        } else {
            val filteredMovies = mutableListOf<Movie>()
            for (movie in originalMovies) {
                if (movie.name.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredMovies.add(movie)
                }
            }
            adapter.submitList(filteredMovies)
        }
    }

    override fun onItemClick(movie: Movie) {
        Log.d("Проверка", "НАЖАТИЕ")

        val detailFragment = DetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        detailFragment.arguments = bundle

        findNavController().navigate(R.id.detailFragment, bundle)
    }
}
