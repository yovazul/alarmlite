package com.yovazul.alarmlite.ui

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yovazul.alarmlite.R
import com.yovazul.alarmlite.data.Alarm

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewModel: AlarmViewModel
    private lateinit var adapter: AlarmAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Continue regardless of permission result
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        // Check for exact alarm permission
        checkExactAlarmPermission()
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        
        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAlarms)
        emptyView = findViewById(R.id.textViewEmpty)
        
        adapter = AlarmAdapter(
            onAlarmClick = { alarm ->
                openAlarmEditor(alarm)
            },
            onToggleClick = { alarm ->
                viewModel.toggleAlarmEnabled(alarm)
            },
            onDeleteClick = { alarm ->
                showDeleteConfirmation(alarm)
            }
        )
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Observe alarms
        viewModel.allAlarms.observe(this) { alarms ->
            adapter.submitList(alarms)
            updateEmptyView(alarms.isEmpty())
        }
        
        // Setup FAB
        findViewById<FloatingActionButton>(R.id.fabAddAlarm).setOnClickListener {
            openAlarmEditor(null)
        }
    }
    
    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed)
                    .setMessage(R.string.alarm_permission_rationale)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }
    
    private fun openAlarmEditor(alarm: Alarm?) {
        val intent = Intent(this, AlarmEditorActivity::class.java)
        alarm?.let {
            intent.putExtra("ALARM_ID", it.id)
        }
        startActivity(intent)
    }
    
    private fun showDeleteConfirmation(alarm: Alarm) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete)
            .setMessage("¿Eliminar esta alarma?")
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.delete(alarm)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    
    private fun updateEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
}
