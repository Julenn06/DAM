package com.example.settingapp

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.settingapp.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="settingsDB")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var firsTime:Boolean = true


    companion object{
        const val VOLUME:String = "volume"
        const val DARK_MODE:String = "dark_mode"
        const val VIBRATION:String = "vibration"
        const val BLUETHOOTH:String = "bluethoth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        CoroutineScope(Dispatchers.IO).launch{
            getSettings().filter { firsTime }.collect{
                //datos SettingValues()
                if(it != null){
                    runOnUiThread {
                        binding.switchVibration.isChecked = it.vibration
                        binding.switchBluetooth.isChecked = it.bluethooth
                        binding.switchDarkMode.isChecked = it.dark_mode
                        binding.rsVolume.setValues(it.volumen.toFloat())
                        firsTime = !firsTime
                    }
                }
            }
        }

        initUI()
    }

    private fun initUI() {
        binding.rsVolume.addOnChangeListener { rangeSlider, fl, b ->
            CoroutineScope(Dispatchers.IO).launch {
                saveVolume(fl.toInt())
            }
        }
        binding.switchBluetooth.setOnCheckedChangeListener { buttonView, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                saveSwitches(BLUETHOOTH,isChecked)
            }
        }
        binding.switchVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                saveSwitches(VIBRATION,isChecked)
            }
        }
        binding.switchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                enableDarkMode()
            }else{
                disableDarkMode()
            }
            CoroutineScope(Dispatchers.IO).launch {
                saveSwitches(DARK_MODE,isChecked)
            }
        }
    }

    private suspend fun saveVolume(value: Int){
        dataStore.edit{
            it[intPreferencesKey(VOLUME)] = value
        }
    }

    private suspend fun saveSwitches(key:String,value:Boolean){
        dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    private fun getSettings(): Flow<SettingsValues?> {
        return dataStore.data.map {
            SettingsValues(
            it[intPreferencesKey(VOLUME)] ?: 50,
            it[booleanPreferencesKey((DARK_MODE))] ?: true,
            it[booleanPreferencesKey((VIBRATION))] ?: false,
            it[booleanPreferencesKey((BLUETHOOTH))] ?: true)
        }
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
    }
}