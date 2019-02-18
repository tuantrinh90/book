package com.dz.libraries.utilities

import android.os.StrictMode

class StrictModeUtility {
    companion object {
        /**
         * StrictMode help s us detect  sensitive activities, such  as disk  accesses or
        network  calls that  we are accidentally  performing  on  the main  thread. As you
        know, performing  heavy  or long  tasks on  the main  thread is a no-go, because
        the main  thread for Android apps is the UI  thread an dit  should be used only  for
        UI  related operations:  it's the only  way  to get  smooth  animations and a
        responsive app.
        We don't  want  it  enabled all  the time, so we limit  it  only  to debug  builds. This
        configuration  will  report  every  violation  about  the main  thread usage and every
        violation  concerning  possible memory  leaks:  Activities,
        BroadcastReceivers, Sqlite objects, and more.
         */
        fun setStrictMode(isDebug: Boolean) {
            if (isDebug) {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build())
                StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build())
            }
        }
    }
}