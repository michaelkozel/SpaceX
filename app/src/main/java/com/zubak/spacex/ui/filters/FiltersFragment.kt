package com.zubak.spacex.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.zubak.spacex.R
import com.zubak.spacex.core.DataManager

class FiltersFragment : Fragment() {

    private lateinit var missionNameCheckBox: CheckBox
    private lateinit var rocketNameCheckBox: CheckBox
    private lateinit var cacheFirstSwitch: Switch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_filters, container, false)

        missionNameCheckBox = root.findViewById(R.id.mission_name_checkbox)
        rocketNameCheckBox = root.findViewById(R.id.rocket_name_checkbox)
        cacheFirstSwitch = root.findViewById(R.id.cache_first_switch)

        context?.let {
            when (DataManager().getFilter(it)) {
                Filters.ROCKET_NAME -> rocketNameCheckBox.isChecked = true
                Filters.MISSION_NAME -> missionNameCheckBox.isChecked = true
            }
            cacheFirstSwitch.isChecked = DataManager().cacheFirst(it)
        }

        missionNameCheckBox.setOnClickListener {
            if (missionNameCheckBox.isChecked && rocketNameCheckBox.isChecked) {
                rocketNameCheckBox.isChecked = false
            }
            context?.let { DataManager().storeFilter(Filters.MISSION_NAME, it) }
        }

        rocketNameCheckBox.setOnClickListener {
            if (rocketNameCheckBox.isChecked && missionNameCheckBox.isChecked) {
                missionNameCheckBox.isChecked = false
            }
            context?.let { DataManager().storeFilter(Filters.ROCKET_NAME, it) }
        }

        cacheFirstSwitch.setOnClickListener {
            context?.let { DataManager().setCacheFirst(it, cacheFirstSwitch.isChecked) }
        }

        return root
    }
}
