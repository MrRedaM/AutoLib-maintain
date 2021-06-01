package com.clovertech.autolib.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.clovertech.autolib.R
import com.clovertech.autolib.model.Step
import com.clovertech.autolib.viewmodel.TacheViewModel

class TaskStepsAdapter(val context: Context) :
    RecyclerView.Adapter<ViewHolderTask>() {
    var data = listOf<Step>()
   
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        return ViewHolderTask(
            LayoutInflater.from(context)
                .inflate(R.layout.task_layout, parent, false)
        )

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.titreStep.text = data[position].step
        holder.titreStep.setChecked(data[position].completed)
        holder.itemView.setOnClickListener(View.OnClickListener {
            data[position].completed = !data[position].completed
            holder.titreStep.setChecked(data[position].completed)
        })

    }

    fun setListSteps(list: List<Step>) {
        data = list
        notifyDataSetChanged()

    }


}

class ViewHolderTask(view: View) : RecyclerView.ViewHolder(view) {
    val titreStep = view.findViewById<CheckBox>(R.id.todoCheckBox)
}
