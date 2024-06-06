package com.cinema.search.cinemasearch.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinema.search.cinemasearch.R
import com.cinema.search.cinemasearch.databinding.ItemsBinding
import com.squareup.picasso.Picasso

class MovieAdapter(var movies: List<Movie>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var filteredMovies: List<Movie> = movies

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {override fun getItemCount(): Int {
        return movies.size
    }

        fun submitList(newMovies: List<Movie>) {
            movies = newMovies
            filterMovies("")
        }

        fun filterMovies(query: String) {
            filteredMovies = if (query.isEmpty()) {
                movies
            } else {
                movies.filter { it.name.contains(query, ignoreCase = true) }
            }
            notifyDataSetChanged()
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = filteredMovies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(movie)
        }
    }



    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemsBinding.bind(itemView)
        fun bind(movie: Movie) {
            binding.name.text = movie.name
            binding.rating.text = "Рейтинг: ${movie.rating.kp}"
            binding.year.text = "Год выпуска: ${movie.year}"
            Picasso.get().load(movie.posterUrl).into(binding.image)
        }
    }

}