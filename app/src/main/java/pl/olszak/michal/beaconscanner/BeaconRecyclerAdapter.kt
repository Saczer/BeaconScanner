package pl.olszak.michal.beaconscanner

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.beacon_holder.view.*

/**
 * @author molszak
 *         created on 22.01.2018.
 */
class BeaconRecyclerAdapter : RecyclerView.Adapter<BeaconRecyclerAdapter.BeaconHolder>() {

    private val messages: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconHolder {
        return BeaconHolder(parent)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: BeaconHolder?, position: Int) {
        holder?.bind(messages[position])
    }

    fun addMessage(message: String) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun addAll(allMessages: List<String>) {
        messages.addAll(allMessages)
        notifyDataSetChanged()
    }

    fun pullMessages(): List<String> {
        return messages
    }


    class BeaconHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beacon_holder, parent, false)) {

        fun bind(text: String) {
            itemView.textView.text = text
        }

    }
}