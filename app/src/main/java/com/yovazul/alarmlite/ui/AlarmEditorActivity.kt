package com.yovazul.alarmlite.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.yovazul.alarmlite.R
import com.yovazul.alarmlite.data.Alarm
import com.yovazul.alarmlite.database.AlarmDatabase
import com.yovazul.alarmlite.database.AlarmRepository
import kotlinx.coroutines.launch

class AlarmEditorActivity : AppCompatActivity() {
    
    private lateinit var viewModel: AlarmViewModel
    private lateinit var timePicker: TimePicker
    private lateinit var editTextLabel: TextInputEditText
    private lateinit var editTextDuration: TextInputEditText
    private lateinit var textViewSelectedSound: TextView
    private lateinit var checkBoxMonday: CheckBox
    private lateinit var checkBoxTuesday: CheckBox
    private lateinit var checkBoxWednesday: CheckBox
    private lateinit var checkBoxThursday: CheckBox
    private lateinit var checkBoxFriday: CheckBox
    private lateinit var checkBoxSaturday: CheckBox
    private lateinit var checkBoxSunday: CheckBox
    
    private var alarmId: Int = -1
    private var selectedSoundUri: String = ""
    
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openFilePicker()
        }
    }
    
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleSelectedFile(uri)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_editor)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        
        initializeViews()
        setupClickListeners()
        
        alarmId = intent.getIntExtra("ALARM_ID", -1)
        
        if (alarmId != -1) {
            supportActionBar?.title = getString(R.string.edit_alarm)
            loadAlarm()
        } else {
            supportActionBar?.title = getString(R.string.new_alarm)
        }
    }
    
    private fun initializeViews() {
        timePicker = findViewById(R.id.timePicker)
        editTextLabel = findViewById(R.id.editTextLabel)
        editTextDuration = findViewById(R.id.editTextDuration)
        textViewSelectedSound = findViewById(R.id.textViewSelectedSound)
        checkBoxMonday = findViewById(R.id.checkBoxMonday)
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday)
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday)
        checkBoxThursday = findViewById(R.id.checkBoxThursday)
        checkBoxFriday = findViewById(R.id.checkBoxFriday)
        checkBoxSaturday = findViewById(R.id.checkBoxSaturday)
        checkBoxSunday = findViewById(R.id.checkBoxSunday)
        
        timePicker.setIs24HourView(true)
    }
    
    private fun setupClickListeners() {
        findViewById<Button>(R.id.buttonSelectSound).setOnClickListener {
            requestStoragePermission()
        }
        
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveAlarm()
        }
        
        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            finish()
        }
    }
    
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                openFilePicker()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                openFilePicker()
            }
        }
    }
    
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("audio/mpeg", "audio/mp3"))
        }
        filePickerLauncher.launch(intent)
    }
    
    private fun handleSelectedFile(uri: Uri) {
        // Take persistable URI permission
        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
        
        selectedSoundUri = uri.toString()
        
        // Get file name
        var fileName = "Archivo de audio seleccionado"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                fileName = cursor.getString(nameIndex)
            }
        }
        
        textViewSelectedSound.text = fileName
    }
    
    private fun loadAlarm() {
        lifecycleScope.launch {
            val repository = AlarmRepository(AlarmDatabase.getDatabase(this@AlarmEditorActivity).alarmDao())
            val alarm = repository.getAlarmById(alarmId)
            
            alarm?.let {
                timePicker.hour = it.hour
                timePicker.minute = it.minute
                editTextLabel.setText(it.label)
                editTextDuration.setText(it.duration.toString())
                selectedSoundUri = it.soundUri
                
                if (it.soundUri.isNotEmpty()) {
                    // Extract file name from URI
                    try {
                        val uri = Uri.parse(it.soundUri)
                        var fileName = "Archivo de audio seleccionado"
                        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (cursor.moveToFirst() && nameIndex != -1) {
                                fileName = cursor.getString(nameIndex)
                            }
                        }
                        textViewSelectedSound.text = fileName
                    } catch (e: Exception) {
                        textViewSelectedSound.text = "Archivo de audio seleccionado"
                    }
                }
                
                checkBoxMonday.isChecked = it.monday
                checkBoxTuesday.isChecked = it.tuesday
                checkBoxWednesday.isChecked = it.wednesday
                checkBoxThursday.isChecked = it.thursday
                checkBoxFriday.isChecked = it.friday
                checkBoxSaturday.isChecked = it.saturday
                checkBoxSunday.isChecked = it.sunday
            }
        }
    }
    
    private fun saveAlarm() {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val label = editTextLabel.text?.toString() ?: ""
        val durationText = editTextDuration.text?.toString() ?: "60"
        val duration = durationText.toIntOrNull() ?: 60
        
        val alarm = Alarm(
            id = if (alarmId == -1) 0 else alarmId,
            hour = hour,
            minute = minute,
            label = label,
            soundUri = selectedSoundUri,
            duration = duration,
            monday = checkBoxMonday.isChecked,
            tuesday = checkBoxTuesday.isChecked,
            wednesday = checkBoxWednesday.isChecked,
            thursday = checkBoxThursday.isChecked,
            friday = checkBoxFriday.isChecked,
            saturday = checkBoxSaturday.isChecked,
            sunday = checkBoxSunday.isChecked,
            isEnabled = true
        )
        
        if (alarmId == -1) {
            viewModel.insert(alarm)
        } else {
            viewModel.update(alarm)
        }
        
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
