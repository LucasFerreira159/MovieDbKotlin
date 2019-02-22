package com.app4fun.moviedb.ui.movieList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app4fun.moviedb.data.model.Movie
import com.app4fun.moviedb.listener.ClickListener
import com.app4fun.moviedb.util.ConstantsUtil
import com.app4fun.moviedb.util.FunUtil.Companion.formatData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_movies.view.*

class AdapterMovieList(private val movies: List<Movie>, var clickListener: ClickListener)
    : RecyclerView.Adapter<AdapterMovieList.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(com.app4fun.moviedb.R.layout.adapter_movies, viewGroup, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bindView(movie)

    }

    inner class ViewHolder(itemView: View, listener: ClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var listener:ClickListener? = null

        init {
            this.listener = listener
            itemView.setOnClickListener(this)
        }


        fun bindView(movie: Movie) {
            val title = itemView.text_title
            val cover = itemView.movie_poster
            val releaseDate = itemView.text_release_date
            val rating = itemView.text_rating
            val overview = itemView.text_overview

            title.text = movie.title
            releaseDate.text = formatData(movie.release_date)
            overview.text = movie.overview

            /*Verifica se existe vote_average atribuida*/
            if (movie.vote_average == 0.0){
                rating.text = "Sem informação"
            }else{
                rating.text = movie.vote_average.toString()
            }

            Picasso.get().load(ConstantsUtil.POSTER_URL + movie.poster_path)
                .error(com.app4fun.moviedb.R.drawable.noimage)
                .into(cover)
        }

        override fun onClick(v: View) {
            this.listener?.onClick(v, adapterPosition)
        }
    }

}