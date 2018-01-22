package pl.olszak.michal.beaconscanner

import android.app.Application
import org.altbeacon.beacon.powersave.BackgroundPowerSaver

/**
 * @author molszak
 *         created on 22.01.2018.
 */
class BeaconScanner : Application() {

    private var powerSaver : BackgroundPowerSaver? = null

    override fun onCreate() {
        super.onCreate()
        powerSaver = BackgroundPowerSaver(this)
    }
}