package com.example.mydiary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter (private var diaries:List<Diary>, context: Context):
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    private val db:DiaryDatabaseHelper = DiaryDatabaseHelper(context)


    class DiaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val titleTextView : TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView : TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton : ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton : ImageView = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diary, parent, false)
        return DiaryViewHolder(view)
    }

    override fun getItemCount(): Int = diaries.size

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val note = diaries[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateDiaryActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteDiary(note.id)
            refreshData( db.getAllDiary())
            Toast.makeText(holder.itemView.context,"Diary Deleted", Toast.LENGTH_SHORT).show()
        }
    }
    fun refreshData(newDiary:List<Diary>){
        diaries = newDiary
        notifyDataSetChanged()
    }
}