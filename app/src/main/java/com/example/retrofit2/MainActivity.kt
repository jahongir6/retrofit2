package com.example.retrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.example.retrofit2.databinding.ActivityMainBinding
import com.example.retrofit2.databinding.ItemDialogBinding
import com.example.retrofit2.models.AddTodoResponse
import com.example.retrofit2.models.TodoGEtRespose
import com.example.retrofit2.models.UserAdapter
import com.example.retrofit2.retrofit.ApiClient
import com.example.retrofit2.retrofit.RetrofitService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), UserAdapter.RvAction {
    private lateinit var userAdapter: UserAdapter
    private lateinit var list: ArrayList<TodoGEtRespose>
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoGEtRespose: TodoGEtRespose
    private lateinit var apiService: RetrofitService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.getRetrofitService()


        //GET
        apiService.getAllTodo()
            .enqueue(object : Callback<List<TodoGEtRespose>> {
                override fun onResponse(
                    call: Call<List<TodoGEtRespose>>,
                    response: Response<List<TodoGEtRespose>>
                ) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        // list as ArrayList<TodoGEtRespose> ko'rinishiga o'tkazib, adapterga berish
                        userAdapter =
                            UserAdapter(list as ArrayList<TodoGEtRespose>, this@MainActivity)
                        binding.rv.adapter = userAdapter
                    }
                }

                override fun onFailure(call: Call<List<TodoGEtRespose>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                }
            })
        binding.add.setOnClickListener {
            val user = AddTodoResponse(
                "jahongir",
                "salom",
                "2023-09-05",
                "retrofit2",
                true
            )
            val call = apiService.createUser(user)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        " Ma'lumotlar muvaffaqiyatli yuborildi",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "  Xatolik yuz berdi", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }


    }

    override fun itemClick(todoGEtRespose: TodoGEtRespose, imageView: ImageView, position: Int) {
        val popupMenu = PopupMenu(this, imageView)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    var todo = todoGEtRespose
                    val dialog = AlertDialog.Builder(this).create()
                    val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
                    dialog.setView(itemDialogBinding.root)
                    itemDialogBinding.sarlavha.setText(todoGEtRespose.sarlavha)
                    itemDialogBinding.sana.setText(todoGEtRespose.sana)
                    itemDialogBinding.batafsil.setText(todoGEtRespose.batafsil)
                    itemDialogBinding.bajarildi.setText(todoGEtRespose.bajarildi.toString())
                    itemDialogBinding.oxirgiMuddat.setText(todoGEtRespose.oxirgiMuddat)
                    itemDialogBinding.zarurlik.setText(todoGEtRespose.zarurlik)


                    itemDialogBinding.btnCansel.setOnClickListener {
                        val sana = itemDialogBinding.sana.text.toString()
                        val sarlavha = itemDialogBinding.sarlavha.text.toString()
                        val batafsil = itemDialogBinding.batafsil.text.toString()
                        val bajarildi = itemDialogBinding.bajarildi.text.toString().toBoolean()
                        val muddat = itemDialogBinding.oxirgiMuddat.text.toString()
                        val zarurlik = itemDialogBinding.zarurlik.text.toString()

                        todo = TodoGEtRespose(
                            bajarildi,
                            batafsil,
                            todoGEtRespose.id,
                            muddat,
                            sana,
                            sarlavha,
                            zarurlik
                        )
                        ApiClient.getRetrofitService().updateUser(todoGEtRespose.id, todo)
                            .enqueue(object : Callback<TodoGEtRespose> {
                                override fun onResponse(
                                    call: Call<TodoGEtRespose>,
                                    response: Response<TodoGEtRespose>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Edited",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<TodoGEtRespose>, t: Throwable) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error ${t.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                        dialog.cancel()

                    }

                    dialog.show()
                }
                R.id.delete -> {
                    ApiClient.getRetrofitService().deleteTodo(todoGEtRespose.id)
                        .enqueue(object : Callback<Int> {
                            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@MainActivity, "delete", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<Int>, t: Throwable) {
                                Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }
            }

            true
        }
        popupMenu.show()
    }
}