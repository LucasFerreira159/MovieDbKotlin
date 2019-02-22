package com.app4fun.moviedb.util

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import com.app4fun.moviedb.data.model.MovieGenre
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Classe com funções utilitarias
 */
class FunUtil {

    companion object {

        /**
         * Método responsável por formatar Dinheiro
         * Retorna uma String para popular TextView Orçamento e Receita do Movie
         */

        fun toMillionFormat(value: Double): String{
            val format = DecimalFormat("#0,000")
            val formattedNumber = format.format(value)
            return formattedNumber
        }

        /**
         * Método responsável por recuperar lista de generos
         * Retorna uma String para popular TextView MovieGenre
         */
        fun getGenreFromList(listGenre: List<MovieGenre>?): String{
            var value: String = ""
            listGenre?.forEach {
                Log.i("INFO", it.name)
                value += it.name + " "
            }
            return value
        }

        /**
         * Verifica conexão internet
         */
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        /**
         * Método responsável por formatar release_date
         * Recebe yyyy-mm-dd
         * Retorna dd/mm/yyyy
         */
        fun formatData(data:String): String{
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd / MM / yyyy");
            val date: Date = inputFormat.parse(data);
            val outputData:String = outputFormat.format(date)
            return outputData
        }

        fun getScreenSizeLayout(context: Context): RecyclerView.LayoutManager {
            val metrics = DisplayMetrics()

            val yInches = metrics.heightPixels / metrics.ydpi
            val xInches = metrics.widthPixels / metrics.xdpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())

            var mLayoutManager: RecyclerView.LayoutManager

            if (diagonalInches >= 6.5) {
                //Verifica qual a orientação de tela, se for vertical irá gerar grid com 2 colunas, caso contrário 4
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mLayoutManager = GridLayoutManager(context, 2)
                } else {
                    mLayoutManager = GridLayoutManager(context, 3)
                }
            } else {
                //Verifica qual a orientação de tela, se for vertical irá gerar grid com 2 colunas, caso contrário 4
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mLayoutManager = LinearLayoutManager(context)
                } else {
                    mLayoutManager = GridLayoutManager(context, 2)
                }
            }

            return mLayoutManager
        }
    }



}