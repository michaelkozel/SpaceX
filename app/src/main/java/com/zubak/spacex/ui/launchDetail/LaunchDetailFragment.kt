package com.zubak.spacex.ui.launchDetail

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.zubak.spacex.R
import com.zubak.spacex.data.Launch

class LaunchDetailFragment(private val launch: Launch) : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_launch_detail, container, false)

        val missionName: TextView = root.findViewById(R.id.mission_name)
        val launchImage: ImageView = root.findViewById(R.id.launch_image)
        val launchRocket: TextView = root.findViewById(R.id.launch_rocket)
        val launchTime: TextView = root.findViewById(R.id.launch_date)
        val launchSite: TextView = root.findViewById(R.id.location)

        missionName.text = launch.missionName
        launchRocket.text =
            context?.getText(R.string.launch_vehicle).toString() + " " + launch.rocket?.rocketName
        launchTime.text =
            context?.getText(R.string.launch_time).toString() + " " + launch.launchDateLocal
        launchSite.text = context?.getText(R.string.launch_location)
            .toString() + " " + launch.launchSite?.siteName

        context?.let {
            if (launch.links?.flickrImages?.size ?: 0 > 0) {
                Glide.with(it)
                    .load(Uri.parse(launch.links?.flickrImages?.get(0) as String))
                    .centerCrop()
                    .into(launchImage)
            } else {
                Glide.with(it)
                    .load(R.mipmap.rocket_holder)
                    .fitCenter()
                    .into(launchImage)
            }
        }

        return root
    }
}
