package fr.isen.Nicolas_LEBON.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.Nicolas_LEBON.R
import fr.isen.Nicolas_LEBON.databinding.ActivityCategoryBinding
import fr.isen.Nicolas_LEBON.detail.DetailActivity
import fr.isen.Nicolas_LEBON.network.Dish
import fr.isen.Nicolas_LEBON.network.MenuResult
import com.google.gson.GsonBuilder
import fr.isen.Nicolas_LEBON.BaseActivity
import fr.isen.Nicolas_LEBON.HomeActivity
import org.json.JSONObject

enum class ItemType {
    STARTER, MAIN, DESSERT;

    companion object {
        fun categoryTitle(item: ItemType?) : String {
            return when(item) {
                STARTER -> "Entrées"
                MAIN -> "Plats"
                DESSERT -> "Desserts"
                else -> ""
            }
        }
    }
}

class CategoryActivity : BaseActivity() {

    private lateinit var bindind: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        val selectedItem = intent.getSerializableExtra(HomeActivity.CATEGORY_NAME) as? ItemType

        bindind.swipeLayout.setOnRefreshListener {
            resetCache()
            makeRequest(selectedItem)
        }

        bindind.categoryTitle.text = getCategoryTitle(selectedItem)

        loadList(listOf<Dish>())

        makeRequest(selectedItem)
        Log.d("lifecycle", "onCreate")
    }

    private fun makeRequest(selectedItem: ItemType?) {

        val BASE_URL = "http://test.api.catering.bluecodegames.com/"
        val PATH_MENU = "menu"
        val ID_SHOP = "id_shop"
        resultFromCache()?.let {
            // La requete est en cache
            parseResult(it, selectedItem)
        } ?: run {
            // La requete n'est pas en cache
            val queue = Volley.newRequestQueue(this)
            val url = BASE_URL + PATH_MENU

            val jsonData = JSONObject()
            jsonData.put(ID_SHOP, "1")

            var request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonData,
                    { response ->

                        bindind.swipeLayout.isRefreshing = false
                        cacheResult(response.toString())
                        parseResult(response.toString(), selectedItem)
                    },
                    { error ->

                        bindind.swipeLayout.isRefreshing = false
                        error.message?.let {
                            Log.d("request", it)
                        } ?: run {
                            Log.d("request", error.toString())
                        }
                    }
            )
            queue.add(request)
        }
    }

    private fun cacheResult(response: String) {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(REQUEST_CACHE, response)
        editor.apply()
    }

    private fun resetCache() {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(REQUEST_CACHE)
        editor.apply()
    }

    private fun resultFromCache(): String? {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(REQUEST_CACHE, null)
    }

    private fun parseResult(response: String, selectedItem: ItemType?) {
        val menuResult = GsonBuilder().create().fromJson(response, MenuResult::class.java)
        val items = menuResult.data.firstOrNull { it.name == ItemType.categoryTitle(selectedItem) }
        loadList(items?.items)
    }

    private fun loadList(dishes: List<Dish>?) {
        dishes?.let {
            val adapter =
                CategoryAdapter(it) { dish ->
                    val intent = Intent(
                        this,
                        DetailActivity::class.java
                    )
                    intent.putExtra(
                        DetailActivity.DISH_EXTRA,
                        dish
                    )
                    startActivity(intent)
                }
            bindind.recyclerView.layoutManager = LinearLayoutManager(this)
            bindind.recyclerView.adapter = adapter
        }
    }

    private fun getCategoryTitle(item: ItemType?): String {
        return when(item) {
            ItemType.STARTER -> getString(R.string.starter)
            ItemType.MAIN -> getString(R.string.main)
            ItemType.DESSERT -> getString(R.string.dessert)
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "onRestart")
    }

    override fun onDestroy() {
        Log.d("lifecycle", "onDestroy")
        super.onDestroy()
    }

    companion object {
        const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
        const val REQUEST_CACHE = "REQUEST_CACHE"
    }
}
