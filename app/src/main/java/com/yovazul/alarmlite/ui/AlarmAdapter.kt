package com.yovazul.alarmlite.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yovazul.alarmlite.R
import com.yovazul.alarmlite.data.Alarm

class AlarmAdapter(
    private val onAlarmClick: (Alarm) -> Unit,
    private val onToggleClick: (Alarm) -> Unit,
    private val onDeleteClick: (Alarm) -> Unit
) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(AlarmDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        private val textViewLabel: TextView = itemView.findViewById(R.id.textViewLabel)
        private val textViewDays: TextView = itemView.findViewById(R.id.textViewDays)
        private val switchEnabled: SwitchCompat = itemView.findViewById(R.id.switchEnabled)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
        
        fun bind(alarm: Alarm) {
            textViewTime.text = alarm.getTimeString()
            textViewLabel.text = if (alarm.label.isNotEmpty()) alarm.label else "Alarma"
            textViewDays.text = alarm.getDaysString()
            
            switchEnabled.setOnCheckedChangeListener(null)
            switchEnabled.isChecked = alarm.isEnabled
            
            itemView.setOnClickListener {
                onAlarmClick(alarm)
            }
            
            switchEnabled.setOnCheckedChangeListener { _, _ ->
                onToggleClick(alarm)
            }
            
            buttonDelete.setOnClickListener {
                onDeleteClick(alarm)
            }
        }
    }
    
    class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }
}
