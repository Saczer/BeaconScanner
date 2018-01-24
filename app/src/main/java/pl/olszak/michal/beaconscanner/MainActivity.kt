package pl.olszak.michal.beaconscanner

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), BeaconConsumer {

    private var beaconManager: BeaconManager? = null
    private val adapter: BeaconRecyclerAdapter = BeaconRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val messages: List<String> = savedInstanceState.getStringArrayList(MESSAGES)
            adapter.addAll(messages)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.VERTICAL
        }
        recyclerView.addItemDecoration(LinearSpacingItemDecoration())

        beaconManager = BeaconManager.getInstanceForApplication(applicationContext)
        beaconManager?.backgroundScanPeriod = TimeUnit.SECONDS.toMillis(1)
        beaconManager?.beaconParsers?.let {
            it.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        }
        beaconManager?.bind(this)
    }

    override fun onPause() {
        super.onPause()
        beaconManager?.let {
            if (it.isBound(this)) {
                it.backgroundMode = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        beaconManager?.let {
            if (it.isBound(this)) {
                it.backgroundMode = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.let { bundle ->
            val messages: List<String> = adapter.pullMessages()
            bundle.putStringArrayList(MESSAGES, ArrayList(messages))
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager?.let {
            it.removeMonitorNotifier(monitorNotifier)
            it.removeRangeNotifier(rangeNotifier)
            it.unbind(this)
        }
    }

    override fun onBeaconServiceConnect() {
        beaconManager?.let {
            it.addMonitorNotifier(monitorNotifier)
            it.addRangeNotifier(rangeNotifier)
            try {
//                it.startMonitoringBeaconsInRegion(Region("background", null, null, null))
                it.startRangingBeaconsInRegion(Region("background", null, null, null))
            } catch (e: RuntimeException) {
                Log.e("TAG", e.message, e)
            }
        }
    }

    private val rangeNotifier: RangeNotifier = object : RangeNotifier {
        override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
            beacons?.let { beaconsCollection ->
                if (beaconsCollection.isNotEmpty()) {
                    val list: List<String> = beaconsCollection.map { beacon ->
                        "Detected beacon: \n" + beacon.toString()
                    }
                    runOnUiThread {
                        adapter.addAll(list)
                    }
                }
            }
        }
    }

    private val monitorNotifier: MonitorNotifier = object : MonitorNotifier {
        override fun didDetermineStateForRegion(state: Int, region: Region?) {
            val message: String = when (state) {
                MonitorNotifier.INSIDE -> {
                    "Entered region : ${region?.toString()}"
                }
                else -> {
                    "Region exit : ${region?.toString()}"
                }
            }
            runOnUiThread {
                adapter.addMessage(message)
            }
        }

        override fun didEnterRegion(region: Region?) {
            val message = "Did enter region for the first time : ${region?.toString()}"
            runOnUiThread {
                adapter.addMessage(message)
            }
        }

        override fun didExitRegion(region: Region?) {
            val message = "Did exit region : ${region?.toString()}"
            runOnUiThread {
                adapter.addMessage(message)
            }
        }

    }

    companion object {
        const val MESSAGES = "messages_key"
    }
}
