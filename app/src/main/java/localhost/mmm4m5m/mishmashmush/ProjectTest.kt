package localhost.mmm4m5m.mishmashmush

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlin.system.measureTimeMillis
import timber.log.Timber


/** Helper Singleton */

open class ProjectTest {
    private val TOAST        = true

    private val LOG          = true
            val USE_Timber   = false
    private val LOG_Timber   = USE_Timber && false
    private val LOG_Prefix   = "_PRJTST"

    private val LOG_Activity = LOG && true
    private val LOG_Fragment = LOG && true
            val LOG_Tests    = LOG && false


    /** Project specific << */
    private val TEST         = true          // TODO disable all tests !!!
            val TEST_Old     = TEST && false // enable old things //??? todo remove Old - AboutActivity, menu and handlers
            val TEST_Dice    = TEST && true  // auto enter name
            val TEST_Game    = TEST && true  // do not shuffle, auto check answer
            val TEST_Intent  = TEST && false // intent without application
    /** Project specific >> */


    fun Toast(context: Context, msg: String) {
        if (!TOAST) return
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun Toast(context: Context, resId: Int) {
        if (!TOAST) return
        val t = context.resources.getResourceTypeName(resId)
        val e = context.resources.getResourceEntryName(resId)
        val n = context.resources.getResourceName(resId)
        //val p = context.resources.getResourcePackageName(resId)
        Toast(context, "%s=%s (%s)".format(t, e, n))
    }


    /** Log functions
    Example 1: Log(Who) is called from ClassWhere.Method (FileWhere.kt)
    Logcat: time PID/com.app.name I/TAG: ClassWho.Method (FileWhere.kt/ClassWhere.Method)
    Example 2: ClassThis extends ClassWhere. Log(This) is called from ClassWhere.Method (ClassWhere.kt)
    Logcat: time PID/com.app.name I/TAG: ClassThis.Method (ClassWhere)
    */
    fun Log(obj: Any?, callback: (() -> Any)?, vararg args: Any, performanceTest: Boolean = false) {
        //??? todo kotlinize, add comments
        if (!LOG) return
        val tc = obj?.javaClass?.name?.split('.')?.last()
        var tm = '.'+callback?.javaClass?.enclosingMethod?.name.toString()
        val st = Throwable().stackTrace // like Timber
        var wc: String
        var off = -1
        do {
            off++
            wc = st[2+off].toString().substringBeforeLast('(').substringBeforeLast('.')
        } while (wc == this::class.qualifiedName)
        wc = wc.substringAfterLast('.')
        var wf = st[2+off].fileName+'/'
        var wm = '.'+st[2+off].methodName
        if (performanceTest) return

        if (wf.substringBeforeLast('.') == wc) wf = ""
        if (tc == wc) wc = ""
        if (tm == wm) wm = ""
        if (wf+wc+wm != "") wm = " ("+wf+wc+wm+')'
        tm = tc+tm

        var ar = ""
        var ex: Throwable? = null
        for (i in args.indices) {
            if (args[i] is Throwable) {
                ar = ' '+(args[i] as Throwable).message.toString()
                if (ex == null) ex = args[i] as Throwable // first Throwable
                else if (ex::class == Throwable::class && args[i]::class != Throwable::class) ex = args[i] as Throwable // first Exception
            } else ar += ' '+args[i].toString()
        }

        if (LOG_Timber) {
            if (ex == null)                         Timber.i(LOG_Prefix+": "+tm+ar+wm)
            else if (ex::class == Throwable::class) Timber.e(LOG_Prefix+": "+tm+ar+wm)
            else                                    Timber.e(ex, LOG_Prefix+": "+tm+ar+wm)
        } else {
            if (ex == null)                         Log.i(LOG_Prefix, tm+ar+wm)
            else if (ex::class == Throwable::class) Log.e(LOG_Prefix, tm+ar+wm)
            else                                    Log.e(LOG_Prefix, tm+ar+wm, ex)
        }
    }

    fun Log(callback: () -> Any, vararg args: Any) {
        Log(callback(), callback = callback, *args)
    }

    fun Log(vararg args: Any) {
        Log(null, null, *args)
    }

    fun LogPerf(callback: () -> Any, msg1: String, time1: Long, msg2: String, time2: Long) {
        Log(callback(), callback = callback, "Performance: %s=%s ms VS %s=%s ms".format(msg1, time1, msg2, time2))
    }

    fun LogActivity(callback: () -> Any, vararg args: Any) {
        if (LOG_Activity) Log(callback(), callback = callback, *args)
    }

    fun LogFragment(callback: () -> Any, vararg args: Any) {
        if (LOG_Fragment) Log(callback(), callback = callback, *args)
    }

    private fun _Log_using_stackTrace(msg: String, performanceTest: Boolean = false) {
        if (!LOG) return
        //val t = Thread.currentThread().stackTrace[5]
        val t = Throwable().stackTrace[3] // like Timber
        var c = t.className.split(".").reversed()[0]
        //c = "("+Thread.currentThread().stackTrace[6].className.split(".").reversed()[0]+") "+c
        c = "("+Throwable().stackTrace[4].className.split(".").reversed()[0]+") "+c
        val m = t.methodName
        val l = " "+msg
        if (!performanceTest) Log.i(LOG_Prefix, c+"."+m+l)
    }

    fun _Log_tests(callback: () -> Any) {
        val obj = callback()
        //Log(obj, callback = callback, "msg", 123)
        //Log(callback, "msg", 123)

        Log(obj, null, "msg", 123)
        Log(obj, null, "msg")
        Log(obj, null)

        Log({}, "msg", 123)
        Log({}, "msg")
        Log({})

        Log("msg", 123)
        Log("msg")
        Log()

        Log(Throwable("msg"))
        Log("msg", Throwable("ops"), Exception("msg"))
    }

    fun _Log_check_performance(callback: () -> Any) {
        _Log_using_stackTrace("test")
        var t1: Long = 0; for (i in 0..10000) t1 += measureTimeMillis { Log (null, callback, "test", performanceTest = true) } // 80-120ms
        var t2: Long = 0; for (i in 0..10000) t2 += measureTimeMillis { _Log_using_stackTrace("test", performanceTest = true) } // 300-350ms
        LogPerf(callback, "Log", t1, "using_stackTrace", t2)
    }
}


/**
Activity Lifecycle (Unfocus - press Share/show Intent)
    Open +F+S   : onCreate -              - onStart - onResume
    Unfocus     :          -              -         -          - onPause
    Focus       :          -              -         - onResume
    Hide        :          -              -         -          - onPause - onStop
    Show        :          - onRestart    - onStart - onResume
    U+H+ Close  :          -              -         -          - onPause - onStop -                  onDestroy
Fragments Lifecycle (Unfocus - press Share/show Intent)
    Open +F+S  #: onAttach..onViewCreated - onStart - onResume
    Unfocus    #:                         -         -          - onPause
    Focus      #:                         -         - onResume
    Hide       #:                         -         -          - onPause - onStop
    Show       #:                         - onStart - onResume
    U+H+ Close #:                         -         -          - onPause - onStop - onDestroyView(#) onDestroy(#) onDetach(#) [onDestroy(1) onDetach(1)]
Fragments Navigation
    Fragment   1:                      + onAttach onCreate onCreateView..onStart  -                                           + onResume
    Fragment   2: onPause(1) onStop(1) + onAttach onCreate onCreateView..onStart  - onDestroyView(1)                          + onResume
    Fragment   3: onPause(2) onStop(2) + onAttach onCreate onCreateView..onStart  - onDestroyView(2) onDestroy(2) onDetach(2) + onResume
    Fragment   1: onPause(3) onStop(3) +                   onCreateView..onStart  - onDestroyView(3) onDestroy(3) onDetach(3) + onResume
    Fragments Events: onCreateView..onStart = onCreateView - onViewCreated - onStart
*/

/** Custom Overrides */

open class ProjectTest_AppCompatActivity : androidx.appcompat.app.AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        PRJTST?.LogActivity({this})
        super.onCreate(savedInstanceState)

        if (PRJTST?.LOG_Tests != true) return
        PRJTST._Log_tests({ this })
        PRJTST._Log_check_performance({ this })
    }

    override fun onDestroy() {
        PRJTST?.LogActivity({this})
        super.onDestroy()
    }

    override fun onStart() {
        PRJTST?.LogActivity({this})
        super.onStart()
    }

    override fun onRestart() {
        PRJTST?.LogActivity({this})
        super.onRestart()
    }

    override fun onStop() {
        PRJTST?.LogActivity({this})
        super.onStop()
    }

    override fun onResume() {
        PRJTST?.LogActivity({this})
        super.onResume()
    }

    override fun onPause() {
        PRJTST?.LogActivity({this})
        super.onPause()
    }
}


/** ***** ***** */

open class ProjectTest_Fragment : androidx.fragment.app.Fragment() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        PRJTST?.LogFragment({this})
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        PRJTST?.LogFragment({this})
        return super.onCreateView(inflater, container, savedInstanceState)
    }

//    override fun onDestroy() {
//        PRJTST?.LogFragment({this})
//        super.onDestroy()
//    }

    override fun onDestroyView() {
        PRJTST?.LogFragment({this})
        super.onDestroyView()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        PRJTST?.LogFragment({this})
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onAttach(context: Context) {
        PRJTST?.LogFragment({this})
        super.onAttach(context)
    }

//    override fun onAttach(activity: Activity) {
//        //PRJTST?.LogFragment({this}, "(Activity)")
//        super.onAttach(activity)
//    }

    override fun onDetach() {
        PRJTST?.LogFragment({this})
        super.onDetach()
    }

//    override fun onStart() {
//        PRJTST?.LogFragment({this})
//        super.onStart()
//    }

//    override fun onStop() {
//        PRJTST?.LogFragment({this})
//        super.onStop()
//    }

    override fun onResume() {
        PRJTST?.LogFragment({this})
        super.onResume()
    }

    override fun onPause() {
        PRJTST?.LogFragment({this})
        super.onPause()
    }
}
