package com.zubak.spacex.ui.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zubak.spacex.R
import com.zubak.spacex.adapter.LaunchAdapter
import com.zubak.spacex.api.LaunchesType
import com.zubak.spacex.core.DataManager
import com.zubak.spacex.data.Launch
import com.zubak.spacex.data.Launches
import com.zubak.spacex.ui.filters.Filters


class LaunchesFragment : Fragment(),
    LifecycleOwner {

    private lateinit var launchesViewModel: LaunchesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LaunchAdapter
    private lateinit var launchesType: LaunchesType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchesType =
            LaunchesType.valueOf(
                arguments?.getString(getString(R.string.launches_type_bundle))
                    ?: LaunchesType.ALL.name
            )
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vmFactory = LaunchesViewModelFactory(context, launchesType)
        launchesViewModel = ViewModelProviders
            .of(this, vmFactory)
            .get(LaunchesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_launches, container, false)

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_container)
        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener {
            launchesViewModel.refreshLaunches()
        }

        adapter = LaunchAdapter(launchesViewModel.launches.value ?: Launches(), context)

        launchesViewModel.launches.observe(viewLifecycleOwner, Observer {
            adapter.launches = launchesViewModel.launches.value ?: Launches()
            sortLaunches(adapter.launches)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        })

        recyclerView = root.findViewById(R.id.allLaunches) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            sortLaunches(adapter.launches)
            adapter.notifyDataSetChanged()
        }

        return root
    }

    private fun sortLaunches(launches: Launches) {
        launches.sortBy { launch: Launch ->
            context?.let {
                when (DataManager().getFilter(it)) {
                    Filters.MISSION_NAME -> launch.missionName
                    Filters.ROCKET_NAME -> launch.rocket?.rocketName
                }
            }
        }
    }
}

