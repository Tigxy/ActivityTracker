package com.chiru.tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Check out https://developer.android.com/guide/topics/ui/layout/recyclerview
class DayAdapter(
    private var days: List<Day>,
    private var itemClickListener: (Int) -> Unit,
    private val updateItemView: Boolean = false
) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(
        view: View,
        clickAtPosition: (Int) -> Unit,
        itemChangedEvent: (Int) -> Unit = {}
    ) :
        RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.info_text)

        init {
            textView.setOnClickListener {
                clickAtPosition(adapterPosition)
                itemChangedEvent(adapterPosition)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_item, parent, false)

        return ViewHolder(
            view,
            { itemClickListener(it) },
            { if (updateItemView) notifyItemChanged(it) })
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DayAdapter.ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.textView.text = days[position].nr.toString()
        holder.textView.visibility = if (!days[position].isPlaceholder) View.VISIBLE else View.GONE
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return days.size
    }
}