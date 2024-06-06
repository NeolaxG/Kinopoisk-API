package com.cinema.search.cinemasearch.screens

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cinema.search.cinemasearch.R
import com.cinema.search.cinemasearch.api.Movie
import com.cinema.search.cinemasearch.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = arguments?.getParcelable<Movie>("movie")

        movie?.let {
            binding.title.text = "Название: ${it.name}"
            binding.desc.text = it.description
            binding.year.text = "Год выпуска: ${it.year}"
            binding.ratingKp.text = "Рейтинг: ${it.rating.kp}"
            binding.ratingImdb.text = "Рейтинг: ${it.rating.imdb}"
            binding.ratingFilmCritics.text = "Рейтинг: ${it.rating.filmCritics}"
            binding.ratingRussianFilmCritics.text = "Рейтинг: ${it.rating.russianFilmCritics}"

            Picasso.get().load(it.posterUrl).into(binding.imageView)
        }
    }
}