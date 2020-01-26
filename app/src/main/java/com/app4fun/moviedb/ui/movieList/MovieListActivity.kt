package com.app4fun.moviedb.ui.movieList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_movie_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.view.Menu
import com.app4fun.moviedb.data.model.Movie
import com.app4fun.moviedb.data.network.response.MovieResponse
import com.app4fun.moviedb.listener.ClickListener
import com.app4fun.moviedb.ui.movieDetail.MovieDetailActivity
import com.app4fun.moviedb.util.ConstantsUtil
import com.app4fun.moviedb.util.FunUtil.Companion.isOnline
import android.view.animation.AnimationUtils
import android.support.v7.widget.RecyclerView
import com.app4fun.moviedb.data.network.ServiceAPI
import com.app4fun.moviedb.util.FunUtil.Companion.getScreenSizeLayout


class MovieListActivity : AppCompatActivity() {

    var movieList: MutableList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.app4fun.moviedb.R.layout.activity_movie_list)
        tb_main.title = "Próximos Filmes"
        setSupportActionBar(tb_main)

        //verifica conexao
        if (isOnline(this)) { callMovieAPI() }
        else { showErrorMsg("Não há conexão com internet") }
    }

    /**
     * Método genérico que irá exibir mensagens de erro
     * Pode ser utilizado:
     *  - quando não há conexão com internet
     *  - quando a Lista estiver vazia
     *  - quando a url estiver incorreta
     */
    fun showErrorMsg(msg: String) {
        text_no_data.visibility = View.VISIBLE
        text_no_data.text = msg
        progressBar.visibility = View.GONE
    }

    /**
     * Método responsável por buscar API de filmes
     */
    fun callMovieAPI() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ConstantsUtil.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieService = retrofit.create(ServiceAPI::class.java)
        val call = movieService.getResults()
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    if (response != null) {
                        val movieResponse: MovieResponse? = response.body()
                            if(movieResponse != null) { movieList = movieResponse.results }
                            buildRecyclerView(movieList)
                    }
                }else{
                    showErrorMsg(getString(com.app4fun.moviedb.R.string.server_error))
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                showErrorMsg(getString(com.app4fun.moviedb.R.string.server_error))
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
            }
        })
    }

    /**
     * Método responsável por regarregar recyclerView
     */
    fun buildRecyclerView(movies: MutableList<Movie>?) {

        //Esconde ProgressBar e Mensagem Erro
        progressBar.visibility = View.GONE
        text_no_data.visibility = View.GONE

        //Configura Recyclerview
        recycler_movies.layoutManager = getScreenSizeLayout(this)

        /*
         * Verifica se a lista está vazia
         * Caso estiver, retorna mensagem de lista vazia,
         * caso contrário, exibe lista de movies
         */
        if (!movies.isNullOrEmpty()) {
            //configura adapter com método de click
            recycler_movies.adapter = AdapterMovieList(movies, object : ClickListener {
                override fun onClick(view: View, pos: Int) {
                    //Pega objeto da posição da lista e envia para Tela Detalhes
                    if(movies != null){
                        val intent = Intent(applicationContext, MovieDetailActivity::class.java)
                        intent.putExtra("selectedMovie", movies[pos])
                        startActivity(intent)
                    }
                }
            })
        } else {
            showErrorMsg("Não há futuros lançamentos para sua região")
            recycler_movies.adapter = null
        }

        if(recycler_movies.adapter != null) { runLayoutAnimation(recycler_movies) }
    }

    /**
     * Este método faz com que a animação seja carregada ao iniciar o adapter do recyclerview
     */
    private fun runLayoutAnimation(recyclerView: RecyclerView) {

        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, com.app4fun.moviedb.R.anim.layout_animation_fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    /**
     * Método responsável por inflar e criar SearchView
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.app4fun.moviedb.R.menu.menu_main, menu)
        val searchItem = menu?.findItem(com.app4fun.moviedb.R.id.action_search)
        if (searchItem != null) {
            val searchItem = searchItem.actionView as SearchView
            searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                /* Busca query na movieList em tempo real*/
                override fun onQueryTextChange(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) { searchMovie(query.toLowerCase()) }
                    else{ buildRecyclerView(movieList) }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Método responsável por buscar filmes dentro do List
     * Este método chamará o configurarLista passando uma nova lista com filmes filtrados
     */
    fun searchMovie(query: String) {
        var filteredMovies: MutableList<Movie> = ArrayList()
        filteredMovies.clear()

        for (i in movieList) {
            if (i.title.toLowerCase().contains(query)) { filteredMovies.add(i) }
            else{ showErrorMsg("Filme não encontrado nos próximos lançamentos") }
        }

        buildRecyclerView(filteredMovies)
    }
}
