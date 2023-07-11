package com.example.retrofit2.models

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit2.databinding.ItemRvBinding

class UserAdapter(var list: ArrayList<TodoGEtRespose>, var rvAction: RvAction) :
    RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(todoGEtRespose: TodoGEtRespose) {
            itemRvBinding.id.text = todoGEtRespose.id.toString()
            itemRvBinding.sarlavha.text = todoGEtRespose.sarlavha
            itemRvBinding.sana.text = todoGEtRespose.sana
            itemRvBinding.bajarildi.text = todoGEtRespose.bajarildi.toString()
            itemRvBinding.oxirgiMuddat.text=todoGEtRespose.oxirgiMuddat
            itemRvBinding.batafsil.text=todoGEtRespose.batafsil
            itemRvBinding.zarurligi.text=todoGEtRespose.zarurlik

            itemRvBinding.imageView.setOnClickListener {
                rvAction.itemClick(todoGEtRespose, itemRvBinding.imageView, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


    interface RvAction {
        fun itemClick(todoGEtRespose: TodoGEtRespose, imageView: ImageView, position: Int)
    }

}