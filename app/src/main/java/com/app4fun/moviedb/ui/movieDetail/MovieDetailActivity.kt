package com.app4fun.moviedb.ui.movieDetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Toast
import com.app4fun.moviedb.R
import com.app4fun.moviedb.data.model.Movie
import com.app4fun.moviedb.data.model.MovieDetail
import com.app4fun.moviedb.data.model.MovieGenre
import com.app4fun.moviedb.data.network.ServiceAPI
import com.app4fun.moviedb.util.ConstantsUtil
import com.app4fun.moviedb.util.FunUtil
import com.app4fun.moviedb.util.FunUtil.Companion.formatData
import com.app4fun.moviedb.util.FunUtil.Companion.getGenreFromList
import com.app4fun.moviedb.util.FunUtil.Companion.toMillionFormat
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_movie_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MovieDetailActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movie: Movie = intent.getSerializableExtra("selectedMovie") as Movie;

        /**
         * Verifica se há conexão com internet e chama API
         */
        if (FunUtil.isOnline(this)) {
            loadDetailAPI(movie.id)
        } else {
            Toast.makeText(applicationContext, getString(R.string.without_connection), Toast.LENGTH_LONG)
        }
    }

    /**
     * Método responsável por recuperar API
     */
    fun loadDetailAPI(id: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ConstantsUtil.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiDetail = retrofit.create(ServiceAPI::class.java)
        val call = apiDetail.getDetail(id)
        call.enqueue(object : Callback<MovieDetail> {

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                response?.let {
                    val movieDetail: MovieDetail? = it.body()
                    val listMovieGenre: List<MovieGenre> = movieDetail!!.genres
                    if (response.isSuccessful) {
                        buildLayout(movieDetail, listMovieGenre)
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.connection_fail), Toast.LENGTH_LONG)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                Toast.makeText(applicationContext, getString(R.string.connection_fail), Toast.LENGTH_SHORT);
            }
        })
    }

    /**
     * Método responsável por criar layout ao receber dados
     */
    fun buildLayout(movieDetail: MovieDetail, listaMovieGenre: List<MovieGenre>?) {

        /* Referencia views*/
        val backdrop = image_backdrop_detalhe
        val poster = image_poster_detalhe
        val title = text_title_detail
        val genre = text_genere_detail
        val range = text_range_detail
        val overview = text_overview_detail
        val releaseDate = text_release_datail
        val runtime = text_runtime_detail
        val budget = text_orcamento_detail
        val revenue = text_receita_detail

        /* Configura imagens do Layout */
        Picasso.get().load(ConstantsUtil.POSTER_URL + movieDetail.poster_path)
            .error(R.drawable.noimage)
            .into(poster)

        Picasso.get().load(ConstantsUtil.BACKDROP_URL + movieDetail.backdrop_path)
            .error(R.drawable.noimage)
            .into(backdrop)

        /**
         * Como estamos trabalhando com Labels separadas de valores, precisaremos deixa-las invisivel ao iniciar layout
         * e torna-la visivel junto com carregamento de dados. Precisaremos tambem esconder a progressBar asssim que
         * estes dados forem carregados.
         */
        progress_detail.visibility = View.GONE
        text_range_detail.visibility = View.VISIBLE
        image_icon_star.visibility = View.VISIBLE
        label_overview.visibility = View.VISIBLE
        label_release.visibility = View.VISIBLE
        label_time.visibility = View.VISIBLE
        label_orcamento.visibility = View.VISIBLE
        label_receita.visibility = View.VISIBLE

        /* Popula layout com datalhes do filme vindo do objeto serializado */
        title.text = movieDetail.title
        overview.text = movieDetail.overview
        releaseDate.text = formatData(movieDetail.release_date)
        runtime.text = movieDetail.runtime.toString() + " min"
        revenue.text = "$ " + toMillionFormat(movieDetail.revenue)
        budget.text = "$ " + toMillionFormat(movieDetail.budget)
        genre.text = getGenreFromList(listaMovieGenre)

        /* Valida se existe vote_average*/
        if (movieDetail.vote_average == 0.0) { range.text = "Não há avaliações" }
        else { range.text = movieDetail.vote_average.toString() }

        /* Configura toolbar com title do filme */
        toolbar.title = movieDetail.title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}
